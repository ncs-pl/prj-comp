// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.expr;

public interface Visitor<T> {
    T visit(Byte exp);

    T visit(Int exp);

    T visit(ReadReg exp);

    T visit(Unary exp);

    T visit(Binary exp);
}

