package org.nc0.prjcomp.ast;

public interface Visitor<T> extends VisitorExp<T> {
    T visit(Formal f);

    T visit(Id e);

    T visit(MethodDecl m);

    T visit(Program p);

    T visit(StatAff a);

    T visit(StatIf s);

    T visit(StatList s);

    T visit(StatPrint s);

    T visit(StatWhile s);

    T visit(TypePrim t);

    T visit(StatVarDecl t);

    T visit(StatReturn t);

    T visit(Block b);

}

