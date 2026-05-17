// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.mips;

import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.ir.com.*;
import org.nc0.prjcomp.support.Errors;

import java.util.Collections;
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
    public List<String> visit(Label label) {
        return new LinkedList<>(Collections.singleton(Asm.label(label.toString()))); // label
    }

    @Override
    public List<String> visit(WriteReg write) {
        List<String> expressionAssembly = write.getExp().accept(exprVisitor);
        Register register = write.getReg();
        int offset = regAlloc.get(register);
        var operation = Asm.save(Asm.sizeOf(register.getType()));
        var assembly = new LinkedList<>(expressionAssembly); // evaluate expression
        assembly.addAll(Asm.pop("$t0")); // pop from the stack
        assembly.add(Asm.command(operation + "$t0, " + offset + "($fp)")); // write to memory
        return assembly;
    }

    @Override
    public List<String> visit(CJump conditionalJump) {
        List<String> expressionAssembly = conditionalJump.getCondition().accept(exprVisitor);
        var assembly = new LinkedList<>(expressionAssembly); // evaluate expression
        assembly.addAll(Asm.pop("$t0")); // pop from the stack
        assembly.add(Asm.command("beq $zero, $t0, " + conditionalJump.getFalseLabel())); // test expression + else
        assembly.add(Asm.command("j " + conditionalJump.getTrueLabel())); // then
        return assembly;
    }

    @Override
    public List<String> visit(Jump jump) {
        return new LinkedList<>(Collections.singleton(Asm.command("j " + jump.getGotoLabel()))); // jump
    }

    @Override
    public List<String> visit(FunCall functionCall) {
        //On fait le passage d’arguments.
        //Ensuite on saute au point d’entrée de la fonction en sauvegardant
        //l’adresse de l’instruction suivante dans $ra.
        //
        //On récupère le décalage associé a registre de l’instruction functionCall.
        //On stocke v0 à cette adresse (Pour rappel, en MIPS, c’est le registre dédié
        //au retour des fonctions)
        Register register = functionCall.getRegister();
        int registerSize = Asm.sizeOf(register.getType());
        org.nc0.prjcomp.ir.Frame frame = functionCall.getFrame();
        int offset = regAlloc.get(register);
        String op = Asm.save(registerSize);
        var arguments = functionCall.getArguments();
        assert arguments.size() <= 4; // NOTE(nico): see Frame.java


        // Pop function parameters (limited to four) from the stack
        var assembly = new LinkedList<String>();
        var pops = new LinkedList<String>();
        int counter = arguments.size() - 1;
        for (var argument : arguments) {
            assembly.addAll(argument.accept(exprVisitor)); // evaluate argument's expression
            pops.addAll(Asm.pop("$a" + counter)); // pop from the stack into argument register
            counter -= 1;
        }
        assembly.addAll(pops);

        // Procedural call
        assembly.add(Asm.command("jal " + frame.getEntryPoint().toString())); // jump to procedure while saving return address
        assembly.add(Asm.command(op + " $v0, " + offset + "($fp)")); // NOTE(nico): c.f. the end of the calling convention defined within Frame.java
        return assembly;
    }
}
