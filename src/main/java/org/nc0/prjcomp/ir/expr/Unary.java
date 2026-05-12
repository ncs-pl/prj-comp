// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.expr;

import org.nc0.prjcomp.ast.UnOp;
import org.nc0.prjcomp.ir.Type;

public class Unary extends Expression {
    final private Expression exp;
    final private UnOp op;

    public Unary(Expression exp, UnOp op) {
        this.exp = exp;
        this.op = op;
    }

    public Expression getExp() {
        return exp;
    }

    public UnOp getOp() {
        return op;
    }

    @Override
    public String toString() {
        return op + "(" + exp + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        return switch (op) {
            case MIN -> Type.INT;
            default -> Type.BYTE;
        };
    }
}
