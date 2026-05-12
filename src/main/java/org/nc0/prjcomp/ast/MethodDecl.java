// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

import java.util.List;

public class MethodDecl extends Node {
    private final List<Formal> formalList;
    private final Type type;
    private final Id id;
    private final Block block;

    public MethodDecl(Position pos, Type t, Id i, List<Formal> fl, Block b) {
        this.position = pos;
        this.type = t;
        this.id = i;
        this.formalList = fl;
        this.block = b;
    }

    public List<Formal> getFormal() {
        return formalList;
    }

    public Id getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Block getBlock() {
        return block;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
