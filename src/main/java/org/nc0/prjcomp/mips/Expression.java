// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.mips;

import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.ir.expr.*;
import org.nc0.prjcomp.ir.expr.Byte;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Expression implements Visitor<List<String>> {
    private final Map<Register, Integer> regAddress;

    public Expression(Map<Register, Integer> regAddress) {
        this.regAddress = regAddress;
    }

    @Override
    public List<String> visit(Byte aByte) {
        var assembly = new LinkedList<>(Collections.singleton(Asm.command("li $t0, " + aByte.getValue()))); // Load immediate
        assembly.addAll(Asm.push("$t0")); // Push on the stack
        return assembly;
    }

    @Override
    public List<String> visit(Int integer) {
        var assembly = new LinkedList<>(Collections.singleton(Asm.command("li $t0, " + integer.getValue()))); // Load immediate
        assembly.addAll(Asm.push("$t0")); // Push on the stack
        return assembly;
    }

    @Override
    public List<String> visit(ReadReg read) {
        int offset = regAddress.get(read.getRegister());
        var assembly = new LinkedList<>(Collections.singleton(Asm.command("lw $t0, " + offset + "($fp)"))); // Load from calculated offset
        assembly.addAll(Asm.push("$t0")); // Push on the stack
        return assembly;
    }

    @Override
    public List<String> visit(Unary unary) {
        List<String> operandAssembly = unary.getExp().accept(this);
        var assembly = new LinkedList<>(operandAssembly); // Evaluate expression
        assembly.addAll(Asm.pop("$t0")); // Pop from the stack
        switch (unary.getOp()) {
            case MIN -> assembly.add(Asm.command("subu $t0, $zero, $t0")); // Inverse integer
            case NOT -> {
                assembly.add(Asm.command("li $t1, 4294967294")); // Load max int
                assembly.add(Asm.command("nor $t0, $t1, $t0")); // Negate
            }
        }
        assembly.addAll(Asm.push("$t0")); // Push on the stack
        return assembly;
    }

    @Override
    public List<String> visit(Binary binary) {
        List<String> leftAssembly = binary.getLeft().accept(this);
        List<String> rightAssembly = binary.getRight().accept(this);
        var assembly = new LinkedList<>(leftAssembly); // Evaluate left expression
        assembly.addAll(rightAssembly); // Evaluate right expression
        assembly.addAll(Asm.pop("$t1")); // Pop from the stack
        assembly.addAll(Asm.pop("$t0")); // Pop from the stack
        assembly.add(switch (binary.getOp()) {
            case ADD -> "add $t0, $t0, $t1"; // Addition
            case MIN -> "sub $t0, $t0, $t1"; // Subtraction
            case MULT -> "mul $t0, $t0, $t1"; // Multiplication
            case DIV -> "div $t0, $t0, $t1"; // Division
            case AND -> "and $t0, $t0, $t1"; // Logical and
            case OR -> "or $t0, $t0, $t1"; // Logical or
            case LT -> "slt $t0, $t0, $t1"; // Lesser than
            case LEQ -> "sle $t0, $t0, $t1"; // Lesser equal than
            case GT -> "sgt $t0, $t0, $t1"; // Greater than
            case GEQ -> "sge $t0, $t0, $t1"; // Greater or equal than
            case EQ -> "seq $t0, $t0, $t1"; // Equality
            case NEQ -> "sne $t0, $t0, $t1"; // Inequality
        });
        assembly.addAll(Asm.push("$t0")); // Push on the stack
        return assembly;
    }
}
