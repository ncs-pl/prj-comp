package org.nc0.prjcomp.ir.expr;

import org.nc0.prjcomp.ir.Type;

public class Byte extends Expression {
    final private byte value;

    public Byte(byte value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        return Type.BYTE;
    }
}
