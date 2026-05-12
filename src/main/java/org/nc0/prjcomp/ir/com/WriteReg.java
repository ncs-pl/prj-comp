package org.nc0.prjcomp.ir.com;

import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.ir.expr.Expression;

public class WriteReg extends Command
{
    final private Register reg;
    final private Expression exp;

    public Register getReg() {
        return reg;
    }

    public Expression getExp() {
        return exp;
    }

    public WriteReg(Register reg, Expression exp) {
        this.reg = reg;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return reg + " := " + exp;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
