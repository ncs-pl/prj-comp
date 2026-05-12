// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.mips;

import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.ir.com.*;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.ListTools;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Command implements Visitor<List<String>> {
    final private Expression exprVisitor;
    final private Map<Register, Integer> regAlloc;

    public Command(Errors errorReporter, Map<Register, Integer> regAlloc) {
        this.regAlloc = regAlloc;
        this.exprVisitor = new Expression(regAlloc);
    }

    @Override
    public List<String> visit(Label com) {
        return ListTools.mklist(Asm.label(com.toString()));
    }

    @Override
    public List<String> visit(WriteReg com) {
        //On compile l’expression à écrire. Puis on dépile dans un registre.
        //On récupère le décalage du registre à écrire, puis on écrit  à
        //cette adresse la valeur dépilée.
        List<String> asmCode = new LinkedList<>();
        //TODO
        return asmCode;
    }

    @Override
    public List<String> visit(CJump com) {
        //Saut conditionnel : si l’expression s’évalue à zero, on saute au
        //label "faux", sinon au label "vrai".
        //(Les noms des labels sont ceux de l’org.nc0.prjcomp.ir).
        List<String> asmCode = com.getCondition().accept(exprVisitor);
        //TODO
        //
        return asmCode;
    }

    @Override
    public List<String> visit(Jump com) {
        //Saut non conditionnel au Label adapté.
        return ListTools.mklist(Asm.command("j " + com.getGotoLabel()));
    }

    @Override
    public List<String> visit(FunCall com) {
        //On fait le passage d’arguments.
        //Ensuite on saute au point d’entrée de la fonction en sauvegardant
        //l’adresse de l’instruction suivante dans $ra.
        //
        //On récupère le décalage associé a registre de l’instruction com.
        //On stocke v0 à cette adresse (Pour rappel, en MIPS, c’est le registre dédié
        //au retour des fonctions)
        List<String> asmCode = passArguments(com.getArguments());
        //TODO…
        return asmCode;
    }

    private List<String> passArguments(List<org.nc0.prjcomp.ir.expr.Expression> exps) {
        //Cette fonction prend une liste d’expressions (on supposera qu’il n’y
        //en a pas plus de 4 par construction), puis les compile avec le
        //visiteur exprVisitor (org.nc0.prjcomp.mips.Expression) en une suite d’instructions.
        //On suppose que le code généré par la visite de chaque expression
        //stocke le résultat sur le sommet de la pile (dans un TP précédent, on
        //faisait le même type d’hypothèse avec v0 par exemple).

        //Pour chacune de ces expressions, on fait en sorte que son résultat
        //soit stocké le registre adapté (après avoir compilé la première, on
        //dépile dans a0, etc…).

        //n.b: Asm.pop(reg) stocke le sommet de sp dans reg.

        List<String> asmCode = new LinkedList<>();
        List<String> popAndCopy = new LinkedList<>();
        int counter = exps.size() - 1;
        for (org.nc0.prjcomp.ir.expr.Expression exp : exps) {
            asmCode.addAll(exp.accept(exprVisitor));
            popAndCopy.addAll(Asm.pop("$a" + counter));
            counter -= 1;
        }
        asmCode.addAll(popAndCopy);
        return asmCode;
    }
}
