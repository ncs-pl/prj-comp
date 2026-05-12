// Generated from sdm.g4 by ANTLR 4.13.1

// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link sdmParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface sdmVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link sdmParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(sdmParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link sdmParser#mainMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainMethod(sdmParser.MainMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link sdmParser#methodDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDecl(sdmParser.MethodDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link sdmParser#formal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormal(sdmParser.FormalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intType}
	 * labeled alternative in {@link sdmParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntType(sdmParser.IntTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolType}
	 * labeled alternative in {@link sdmParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolType(sdmParser.BoolTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statList}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatList(sdmParser.StatListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statIf}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatIf(sdmParser.StatIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statWhile}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatWhile(sdmParser.StatWhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statFor}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatFor(sdmParser.StatForContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statPrint}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatPrint(sdmParser.StatPrintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statAff}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatAff(sdmParser.StatAffContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statIncr}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatIncr(sdmParser.StatIncrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statReturn}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatReturn(sdmParser.StatReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statVarDecl}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatVarDecl(sdmParser.StatVarDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statVarDeclAff}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatVarDeclAff(sdmParser.StatVarDeclAffContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exUnop}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExUnop(sdmParser.ExUnopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exId}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExId(sdmParser.ExIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exRead}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExRead(sdmParser.ExReadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exFalse}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExFalse(sdmParser.ExFalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exCall}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExCall(sdmParser.ExCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exParenthesis}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExParenthesis(sdmParser.ExParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exInt}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExInt(sdmParser.ExIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exBinop}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExBinop(sdmParser.ExBinopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exTrue}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExTrue(sdmParser.ExTrueContext ctx);
}