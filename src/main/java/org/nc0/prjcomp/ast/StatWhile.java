// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public class StatWhile extends Statement {
    private final Expression expression;
    private final Block block;

    public StatWhile(Position pos, Expression exp, Block b) {
        this.expression = exp;
        this.block = b;
        this.position = pos;
    }

    public Expression getExpression() {
        return expression;
    }

    public Block getBlock() {
        return block;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
