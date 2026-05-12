package org.nc0.prjcomp.ast;

public abstract class Type extends Node {
    public boolean equals(Type t) {
        return t.toString().equals(this.toString());
    }

    public abstract String toString();

    public abstract Type copy();
}
