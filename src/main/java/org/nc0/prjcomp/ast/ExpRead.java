// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public class ExpRead extends Expression {
    private int value;

    public ExpRead(Position position) {
        this.position = position;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return "read()";
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
