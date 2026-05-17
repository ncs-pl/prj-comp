// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.mips;

import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Frame {
    private final Errors errorReporter;

    public Frame(Errors errorReporter) {
        this.errorReporter = errorReporter;
    }

    List<String> generate(Pair<org.nc0.prjcomp.ir.Frame, List<org.nc0.prjcomp.ir.com.Command>> fragment) {
        //Pour la génération du code associé à une fonction :
        //- on récupère le frame
        //- on s’assure que la fonction n’ait pas plus de 4 arguments, sinon
        //on envoie une erreur.
        //- on crée une nouvelle map regAlloc pour l’allocation des registres sur la pile.
        //- on la met à jour avec les données du frame (implémenté plus haut)
        //- on renvoie le code composé du prologue, du corps (qui utilise la
        //liste de commandes du fragment), et de
        // l’épilogue.

        org.nc0.prjcomp.ir.Frame frame = fragment.fst();
        List<org.nc0.prjcomp.ir.com.Command> commands = fragment.snd();

        // NOTE(nico): limit to 4 parameters per instructions, as to stay on the limited reserved registers.
        if (frame.getParameters().size() > 4) {
            errorReporter.add("Erreur: La fonction " + frame.getEntryPoint() + " possède trop de param (max 4).");
        }

        // Simple registers allocation
        var regAlloc = new HashMap<Register, Integer>();
        //Cette fonction prend tous les registres locaux du cadre, et leur
        //associe un décalage dans la map regAlloc (0, puis 4, puis 8,
        //etc…).
        //
        //Cette étape est importante, elle permet d’associer toutes les
        //données temporaires à un emplacement sur la pile (on n’utilisera
        //pas les registres MIPS32 dans ce compilateur, il faudrait faire de
        //l’allocation, ce qui est trop complexe pour le temps imparti).

        List<Register> registers = allFromFrame(frame);
        int offset = 0;
        for (Register register : registers) {
            regAlloc.put(register, offset);
            offset -= Program.DEFAULT_SIZE;
        }
        //La taille du cadre correspond donc à une case par donnée temporaire,
        //plus les deux cases pour stocker l’adresse de retour et le pointeur de
        //cadre.

        frame.setSize(Program.DEFAULT_SIZE * (registers.size() + 2));

        // Generate the instructions
        List<Register> parameters = frame.getParameters();
        var assembly = new LinkedList<String>();

        // Generate the instructions for the function's prologue
        // NOTE(nico): calling conventions from instructions, verbatim.
        //Algo :
        //-> on enregistre l’adresse de retour ainsi que le pointeur de
        //      cadre dans des registres (t0, t1)
        //-> on met à jour fp et sp
        //-> on stocke t0 et t1 sur la pile (à 4($sp) et 8($sp)
        //    respectivement)
        //-> on empile aussi les paramètres de la fonction
        //  + on les lit dans les registres a0…a3,
        //  + chacun est stocké en offset($fp)$, où offset est
        //    la valeur associé au registre dans la map regAlloc.

        assembly.add(Asm.label(frame.getEntryPoint().toString()));

        // Caller/callee registers convention
        assembly.add(Asm.command("move $t0, $ra")); // remember caller's call return point
        assembly.add(Asm.command("move $t1, $fp")); // remember caller's stack frame

        // Update the stack pointers
        assembly.add(Asm.command("move $fp, $sp")); // update fp
        assembly.add(Asm.command("subu $sp, $sp, " + frame.getSize())); // stack is downward, entry goes down

        // Initialize the function's registers
        assembly.add("sw $t0, 4($sp)"); // per calling conventions
        assembly.add("sw $t1, 8($sp)"); // per calling conventions

        // Function's body (only a part, everything else is done within Command.java)
        int counter = 0;
        assert parameters.size() <= 4; // NOTE(nico): simplification per instructions
        for (var parameter : parameters) {
            int parameterOffset = regAlloc.get(parameter);
            int parameterSize = Asm.sizeOf(parameter.getType());
            String instruction = Asm.save(parameterSize);
            assembly.add(Asm.command(instruction + " $a" + counter + ", " + parameterOffset + "($fp)"));
            counter++;
        }

        // Generate the instructions for the function's body
        var visitor = new Command(errorReporter, regAlloc);
        for (var command : commands) {
            assembly.addAll((command.accept(visitor)));
        }

        // Generate the instructions for the function's epilogue
        // NOTE(nico): end of calling conventions, verbatim:
        //Algo :
        // - On récupère le décalage pour obtenir l’adresse où sont
        // enregistrés les anciens fp et ra. (ce sont deux entiers négatifs, que
        // l’on devra soustraire à l’actuel fp pour accéder aux valeurs
        // voulues).
        // - On crée une liste d’instructions commençant par un label (le point
        // de sortie de la fonction, utiliser Asm.label)
        // - On récupère le registre du frame associé au retour de la fonction,
        // ainsi que son décalage (via regAlloc). On ajoute au code le
        // chargement du contenu de ce registre dans $v0.
        // - On remet à jour $sp
        // - On remet à jour ra et fp grâce aux décalages récupérés plus tôt.
        // - On saute à l’instruction enregistrée en $ra.
        assembly.add(Asm.label(frame.getExitPoint().toString()));

        // Clean up registers and states
        assembly.add(Asm.command("lw $t0, 4($sp)")); // per calling conventions
        assembly.add(Asm.command("lw $t1, 8($sp)")); // per calling conventions

        // Handle return value
        if (frame.getResult() != null) {
            int resultOffset = regAlloc.get(frame.getResult());
            assembly.add(Asm.command("lw $v0, " + resultOffset + "($fp)")); // load return value
        }

        // Back on tracks
        assembly.add(Asm.command("move $ra, $t0")); // remember caller's call return point
        assembly.add(Asm.command("move $fp, $t1")); // remember caller's stack frame
        assembly.add(Asm.command(("addu $sp, $sp, " + frame.getSize())));  // the stack is downwards, exit goes up
        assembly.add(Asm.command("jr $ra"));

        return assembly;
    }

    List<Register> allFromFrame(org.nc0.prjcomp.ir.Frame frame) {
        //Cette fonction récupère tous les registres temporaires du cadre,
        //plus celui pour le retour de la fonction.
        List<Register> registers = new LinkedList<>(frame.getParameters());
        registers.addAll(frame.getLocals());
        if (frame.getResult() == null) {
            errorReporter.add("frame sans résultat");
        } else {
            registers.add(frame.getResult());
        }
        return registers;
    }
}
