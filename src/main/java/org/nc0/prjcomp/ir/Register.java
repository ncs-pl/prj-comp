// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir;

public class Register {
    static private int lastRegister;
    final private int id;
    final private Type type;

    public Register(Type type) {
        this.type = type;
        this.id = lastRegister;
        lastRegister += 1;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "reg" + id;
    }
}
