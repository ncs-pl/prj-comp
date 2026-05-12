package org.nc0.prjcomp.ir.expr;

import org.nc0.prjcomp.ast.BinOp;
import org.nc0.prjcomp.ir.Type;

public class Binary extends Expression {
    final private Expression left;
    final private Expression right;
    final private BinOp op;

    public Binary(Expression left, Expression right, BinOp op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public BinOp getOp() {
        return op;
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        return switch (op) {
            case ADD, MIN -> Type.INT;
            default -> Type.BYTE;
        };
    }
}
