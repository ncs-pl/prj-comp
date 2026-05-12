// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public abstract class Node {
    protected Position position;

    public Position getPosition() {
        return position;
    }

    public abstract <T> T accept(Visitor<T> visitor);
}
