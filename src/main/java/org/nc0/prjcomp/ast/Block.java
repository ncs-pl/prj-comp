// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public class Block extends Statement {
    private final Statement statement;

    public Block(Position position, Statement s) {
        this.position = position;
        this.statement = s;
    }

    public Statement getStatement() {
        return statement;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
