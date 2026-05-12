package org.nc0.prjcomp.ast;

import java.util.List;

public class ExpCallMethod extends Expression {
    private final Id method;
    private final List<Expression> args;

    public ExpCallMethod(Position position, Id method, List<Expression> args) {

        this.position = position;
        this.method = method;
        this.args = args;
    }

    public Id getMethod() {
        return method;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public String toString() {
        StringBuilder res = new StringBuilder(method.toString() + "(");
        for (Expression a : args) {
            res.append(a.toString()).append(",");
        }
        return res + ")";
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

}

	
