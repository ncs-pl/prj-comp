// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public class Position {
    public static final Position dummy = new Position(-1, -1);
    private final int line;
    private final int column;

    public Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public Position copy() {
        return new Position(line, column);
    }

    public String toString() {
        return "[ligne=" + line + ", colonne=" + column + "]";
    }

    public String line() {
        return "ligne " + line;
    }
}
