package org.nc0.prjcomp.ir.translation;

import org.nc0.prjcomp.ir.com.Command;
import org.nc0.prjcomp.ir.expr.Expression;

import java.util.LinkedList;
import java.util.List;

public class Result
{
    private Expression expression;
    private List<Command> code;

    public Expression getExp() {
        return expression;
    }

    public List<Command> getCode() {
        return code;
    }

    public Result(Expression expression, List<Command> code) {
        this.expression = expression;
        this.code = code;
    }

    public Result(Expression expression) {
        this(expression, new LinkedList<>());
    }

    public Result(List<Command> code) {
        this(null, code);
    }
}
