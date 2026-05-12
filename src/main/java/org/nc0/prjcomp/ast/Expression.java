// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public abstract class Expression extends Node {
    abstract public String toString();

    abstract public <T> T accept(Visitor<T> visitor);
}

