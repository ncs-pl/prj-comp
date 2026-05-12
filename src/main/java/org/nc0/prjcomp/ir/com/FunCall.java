// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.com;

import org.nc0.prjcomp.ir.Frame;
import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.ir.expr.Expression;

import java.util.List;

public class FunCall extends Command {
    final private Register register;
    final private Frame frame;
    final private List<Expression> arguments;

    public FunCall(Register register, Frame frame, List<Expression> arguments) {
        this.register = register;
        this.frame = frame;
        this.arguments = arguments;
    }

    public Frame getFrame() {
        return frame;
    }

    public Register getRegister() {
        return register;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return register + " := call " + frame.getEntryPoint().toString().replace(":", "") + arguments;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
