// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public class ExpUn extends Expression {
    private final Expression expression;
    private final UnOp unOp;

    public ExpUn(Position position, Expression expression, UnOp unOp) {
        this.position = position;
        this.expression = expression;
        this.unOp = unOp;
    }

    public Expression getExp() {
        return expression;
    }

    public UnOp getOp() {
        return unOp;
    }

    public String toString() {
        return unOp.toString() + expression.toString();
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
