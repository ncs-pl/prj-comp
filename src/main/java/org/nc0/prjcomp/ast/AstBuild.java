// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.nc0.prjcomp.parser.sdmBaseVisitor;
import org.nc0.prjcomp.parser.sdmParser;
import org.nc0.prjcomp.parser.sdmParser.*;
import org.nc0.prjcomp.parser.sdmVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AstBuild extends sdmBaseVisitor<Node> implements sdmVisitor<Node> {
    @Override
    public Node visitProgram(ProgramContext context) {
        var methods = Stream.concat(context.methodDecl().stream().map(method -> (MethodDecl) visit(method)), Stream.of((MethodDecl) visit(context.mainMethod()))).toList();
        return new Program(position(context), methods);
    }

    private static Position position(ParserRuleContext context) {
        return new Position(context.start.getLine(), context.start.getCharPositionInLine());
    }

    @Override
    public Node visitMainMethod(sdmParser.MainMethodContext context) {
        Position position = position(context);
        Statement i = new StatReturn(position, new ExpInt(position, 0));
        List<Statement> statements = new ArrayList<>();
        statements.add((Statement) context.statement().accept(this));
        statements.add(i);
        Statement sls = new StatList(position, statements);
        var type = new TypePrim(position, TypePrim.Prim.INT);
        var id = new Id(position, "main");
        var body = new Block(position, sls);
        var formals = new ArrayList<Formal>();
        return new MethodDecl(position, type, id, formals, body);
    }

    @Override
    public Node visitMethodDecl(MethodDeclContext context) {
        var type = (Type) visit(context.type());
        var id = new Id(position(context), context.Id().getText());
        var body = new Block(position(context), (Statement) visit(context.statement()));
        List<Formal> formals = context.formal().stream().map(formal -> (Formal) visit(formal)).toList();
        return new MethodDecl(position(context), type, id, formals, body);
    }

    @Override
    public Node visitFormal(FormalContext context) {
        var type = (Type) visit(context.type());
        var id = new Id(position(context), context.Id().getText());
        return new Formal(position(context), type, id);
    }

    @Override
    public Node visitIntType(IntTypeContext context) {
        return new TypePrim(position(context), TypePrim.Prim.INT);
    }

    @Override
    public Node visitBoolType(BoolTypeContext context) {
        return new TypePrim(position(context), TypePrim.Prim.BOOL);
    }

    @Override
    public Node visitStatList(StatListContext context) {
        List<Statement> statements = context.statement().stream().map(statement -> (Statement) visit(statement)).toList();
        return new StatList(position(context), statements);
    }

    @Override
    public Node visitStatIf(StatIfContext context) {
        var expression = (Expression) visit(context.exp());
        var branchTrue = new Block(position(context), (Statement) visit(context.statement(0)));
        var branchFalse = new Block(position(context), (Statement) visit(context.statement(1)));
        return new StatIf(position(context), expression, branchTrue, branchFalse);
    }

    @Override
    public Node visitStatWhile(StatWhileContext context) {
        var expression = (Expression) visit(context.exp());
        var body = new Block(position(context), (Statement) visit(context.statement()));
        return new StatWhile(position(context), expression, body);
    }

    @Override
    public Node visitStatFor(StatForContext context) {
        Position pos = position(context);
        var statements = new ArrayList<Statement>();
        var condition = (Expression) visit(context.exp());
        var initialisation = (Statement) visit(context.statement(0));
        var action = (Statement) visit(context.statement(1));
        var body = (Statement) visit(context.statement(2));
        var whileBody = new ArrayList<Statement>();
        whileBody.add(body);
        whileBody.add(action);
        var block = new Block(pos, new StatList(pos, whileBody));
        var whileStatement = new StatWhile(pos, condition, block);
        statements.add(initialisation);
        statements.add(whileStatement);
        return new StatList(position(context), statements);
    }

    @Override
    public Node visitStatPrint(StatPrintContext context) {
        var expression = (Expression) visit(context.exp());
        return new StatPrint(position(context), expression);
    }

    @Override
    public Node visitStatAff(StatAffContext context) {
        var id = new Id(position(context), context.Id().getText());
        return new StatAff(position(context), id, (Expression) visit(context.exp()));
    }

    @Override
    public Node visitStatIncr(StatIncrContext context) {
        Position position = position(context);
        var id = new Id(position, context.Id().getText());
        var variable = new ExpId(position, context.Id().getText());
        var one = new ExpInt(position, 1);
        var expression = new ExpBin(position, variable, BinOp.ADD, one);
        return new StatAff(position, id, expression);
    }

    @Override
    public Node visitStatReturn(StatReturnContext context) {
        var expression = (Expression) visit(context.exp());
        return new StatReturn(position(context), expression);
    }

    @Override
    public Node visitStatVarDecl(StatVarDeclContext context) {
        var type = (Type) visit(context.type());
        var id = new Id(position(context), context.Id().getText());
        return new StatVarDecl(position(context), id, type);
    }

    @Override
    public Node visitStatVarDeclAff(StatVarDeclAffContext context) {
        var statements = new ArrayList<Statement>();
        var expression = (Expression) visit(context.exp());
        var id = new Id(position(context), context.Id().getText());
        var type = (Type) visit(context.type());
        statements.add(new StatVarDecl(position(context), id, type));
        statements.add(new StatAff(position(context), id, expression));
        return new StatList(position(context), statements);
    }

    @Override
    public Node visitExUnop(ExUnopContext context) {
        var expression = (Expression) visit(context.exp());
        var operator = switch (context.op.getText()) {
            case "!" -> UnOp.NOT;
            case "-" -> UnOp.MIN;
            default -> throw new IllegalStateException("Unexpected unary operator expression");
        };
        return new ExpUn(position(context), expression, operator);
    }

    @Override
    public Node visitExId(ExIdContext context) {
        return new ExpId(position(context), context.Id().getText());
    }

    @Override
    public Node visitExRead(ExReadContext context) {
        return new ExpRead(position(context));
    }

    @Override
    public Node visitExFalse(ExFalseContext context) {
        return new ExpCons(position(context), Constant.FALSE);
    }

    @Override
    public Node visitExCall(ExCallContext context) {
        var method = new Id(position(context), context.Id().getText());
        List<Expression> arguments = context.exp().stream().map(expression -> (Expression) visit(expression)).toList();
        return new ExpCallMethod(position(context), method, arguments);
    }

    @Override
    public Node visitExParenthesis(ExParenthesisContext context) {
        return visit(context.exp());
    }

    @Override
    public Node visitExInt(ExIntContext context) {
        return new ExpInt(position(context), Integer.parseInt(context.Int().getText()));
    }

    @Override
    public Node visitExBinop(ExBinopContext context) {
        var left = (Expression) visit(context.exp(0));
        var right = (Expression) visit(context.exp(1));
        var operator = switch (context.op.getText()) {
            case "+" -> BinOp.ADD;
            case "-" -> BinOp.MIN;
            case "*" -> BinOp.MULT;
            case "<" -> BinOp.LT;
            case "<=" -> BinOp.LEQ;
            case ">=" -> BinOp.GEQ;
            case ">" -> BinOp.GT;
            case "&&" -> BinOp.AND;
            case "==" -> BinOp.EQ;
            case "!=" -> BinOp.NEQ;
            case "||" -> BinOp.OR;
            case "/" -> BinOp.DIV;
            default -> throw new IllegalStateException("Unexpected binary operator expression");
        };
        return new ExpBin(position(context), left, operator, right);
    }

    @Override
    public Node visitExTrue(ExTrueContext context) {
        return new ExpCons(position(context), Constant.TRUE);
    }
}
