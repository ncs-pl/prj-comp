package org.nc0.prjcomp.ir.expr;

import org.nc0.prjcomp.ir.Type;

abstract public class Expression {
    public abstract <T> T accept(Visitor<T> visitor);
    public abstract Type getType();
}
