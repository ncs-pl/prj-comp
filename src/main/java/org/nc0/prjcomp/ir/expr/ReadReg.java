// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.expr;

import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.ir.Type;

public class ReadReg extends Expression {
    final private Register register;

    public ReadReg(Register register) {
        this.register = register;
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public String toString() {
        return register.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        return register.getType();
    }
}
