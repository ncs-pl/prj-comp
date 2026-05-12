package org.nc0.prjcomp.ir.com;

import org.nc0.prjcomp.ir.expr.Expression;

public class CJump extends Command {
    final private Expression condition;
    final private Label trueLabel;
    final private Label falseLabel;

    public Expression getCondition() {
        return condition;
    }

    public Label getTrueLabel() {
        return trueLabel;
    }

    public Label getFalseLabel() {
        return falseLabel;
    }

    public CJump(Expression condition, Label trueLabel, Label falseLabel) {
        this.condition = condition;
        this.trueLabel = trueLabel;
        this.falseLabel = falseLabel;
    }

    @Override
    public String toString() {
        return "CJump (" + condition + ", "
                + trueLabel + ", " + falseLabel + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
