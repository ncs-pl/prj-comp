package org.nc0.prjcomp.mips;

import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Frame {
    private final Errors errorReporter;
    public Frame(Errors errorReporter) {
        this.errorReporter = errorReporter;
    }

    List<Register> allFromFrame(org.nc0.prjcomp.ir.Frame frame) {
	    //Cette fonction récupère tous les registres temporaires du cadre,
	    //plus celui pour le retour de la fonction.
        List<Register> registers = new LinkedList<>(frame.getParameters());
        registers.addAll(frame.getLocals());
        if (frame.getResult()==null){
		errorReporter.add("frame sans résultat");
	}
	else registers.add(frame.getResult());
        return registers;
    }
    private void updateRegAlloc(org.nc0.prjcomp.ir.Frame frame, Map<Register, Integer> regAlloc) {
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
    }
    private List<String> generateBody(Map<Register, Integer> regAlloc,
                                      List<org.nc0.prjcomp.ir.com.Command> fragment) {
	    //Pour générer le corps de la fonction, facile.
	    //On crée un visiteur de commandes (class org.nc0.prjcomp.mips.Command)
	    //et on renvoie la liste des instructions assembleur.
        List<String> asmCode = new LinkedList<>();
	//TODO
        return asmCode;
    }
    private List<String> generatePrologue(Map<Register, Integer> regAlloc,
                                          org.nc0.prjcomp.ir.Frame frame) {
	    //Que se passe-t-il à l’entrée dans une fonction ?
	    //- On met un nouveau cadre sur la pile. 	    
	    //Le nouveau pointeur de cadre (frame pointer = fp)
	    // correspondra au sommet de la pile (stack pointer = sp).
	    // Schématiquement : 
	    //		----------fp		…
	    //		…		-> 	…
	    //		…			…
	    //		----------sp		---------fp
	    //					 ^
	    //					 |
	    //					 | (taille du cadre) 
	    //					 |
	    //					 v
	    //					----------sp
	    //
	    //(il faut connaître la taille du nouveau cadre, pour savoir où
	    //mettre le pointeur de pile)
	    //Bien sûr, il faudra se rappeler de là où était le pointeur de cadre 
	    //avant qu’on le mette à jour.
	    //
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
	    //c’est tout, on devrait donc avoir ça en sortie :
	    //
	    //	…
	    //	…
	    //	--------------fp (ancien sp)
	    //	…
	    //	[contenu des ai (si utilisés)]
	    //	…
	    //
	    //	…
	    //	[ancien ra]
	    //  [ancien sp]
	    //  --------------sp 
        List<String> asmCode = new LinkedList<>();
        asmCode.add(Asm.label(frame.getEntryPoint().toString()));

  	//TODO	
	return asmCode;
    }
    private List<String> generateEpilogue(Map<Register, Integer> regAlloc,
                                          org.nc0.prjcomp.ir.Frame frame) {
	//Que se passe-t-il à la sortie d’une fonction ?
	//
	//On doit récupérer l’adresse de retour et le pointeur de cadre qu’il y
	//avait avant d’entrer dans la fonction, pour les restaurer.
	//
	//Mais où sont-ils ? Si on les a empilés à 4 et 8 octets de la pile lors
	//de l’entrée, alors ils sont là :
	//	
	//	…
	//	…
	//	
	//	--------fp(n’a pas bougé)
	//	^ …
	//	| …
	//	| [ra]		(taille du frame)
	//	v [fp]
	//	
	//	…
	//	…
	//	--------sp	
	//
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
        List<String> asmCode = new LinkedList<>();
        
	//TODO
	return asmCode;
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

	    return new LinkedList<>();
    }
}
