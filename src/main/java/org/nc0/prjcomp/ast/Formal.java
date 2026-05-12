// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public class Formal extends Node {
    private final Type type;
    private final Id id;

    public Formal(Position pos, Type t, Id i) {
        this.type = t;
        this.id = i;
    }

    public Id getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
