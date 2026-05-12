// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public interface VisitorExp<T> {
    T visit(ExpBin e);

    T visit(ExpCallMethod e);

    T visit(ExpCons e);

    T visit(ExpId e);

    T visit(ExpInt e);

    T visit(ExpUn e);

    T visit(ExpRead e);
}
