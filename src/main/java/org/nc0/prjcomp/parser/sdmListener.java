// Generated from sdm.g4 by ANTLR 4.13.1

// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link sdmParser}.
 */
public interface sdmListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link sdmParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(sdmParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link sdmParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(sdmParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link sdmParser#mainMethod}.
	 * @param ctx the parse tree
	 */
	void enterMainMethod(sdmParser.MainMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link sdmParser#mainMethod}.
	 * @param ctx the parse tree
	 */
	void exitMainMethod(sdmParser.MainMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link sdmParser#methodDecl}.
	 * @param ctx the parse tree
	 */
	void enterMethodDecl(sdmParser.MethodDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link sdmParser#methodDecl}.
	 * @param ctx the parse tree
	 */
	void exitMethodDecl(sdmParser.MethodDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link sdmParser#formal}.
	 * @param ctx the parse tree
	 */
	void enterFormal(sdmParser.FormalContext ctx);
	/**
	 * Exit a parse tree produced by {@link sdmParser#formal}.
	 * @param ctx the parse tree
	 */
	void exitFormal(sdmParser.FormalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intType}
	 * labeled alternative in {@link sdmParser#type}.
	 * @param ctx the parse tree
	 */
	void enterIntType(sdmParser.IntTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intType}
	 * labeled alternative in {@link sdmParser#type}.
	 * @param ctx the parse tree
	 */
	void exitIntType(sdmParser.IntTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolType}
	 * labeled alternative in {@link sdmParser#type}.
	 * @param ctx the parse tree
	 */
	void enterBoolType(sdmParser.BoolTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolType}
	 * labeled alternative in {@link sdmParser#type}.
	 * @param ctx the parse tree
	 */
	void exitBoolType(sdmParser.BoolTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statList}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatList(sdmParser.StatListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statList}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatList(sdmParser.StatListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statIf}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatIf(sdmParser.StatIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statIf}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatIf(sdmParser.StatIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statWhile}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatWhile(sdmParser.StatWhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statWhile}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatWhile(sdmParser.StatWhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statFor}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatFor(sdmParser.StatForContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statFor}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatFor(sdmParser.StatForContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statPrint}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatPrint(sdmParser.StatPrintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statPrint}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatPrint(sdmParser.StatPrintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statAff}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatAff(sdmParser.StatAffContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statAff}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatAff(sdmParser.StatAffContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statIncr}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatIncr(sdmParser.StatIncrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statIncr}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatIncr(sdmParser.StatIncrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statReturn}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatReturn(sdmParser.StatReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statReturn}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatReturn(sdmParser.StatReturnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statVarDecl}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatVarDecl(sdmParser.StatVarDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statVarDecl}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatVarDecl(sdmParser.StatVarDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statVarDeclAff}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatVarDeclAff(sdmParser.StatVarDeclAffContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statVarDeclAff}
	 * labeled alternative in {@link sdmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatVarDeclAff(sdmParser.StatVarDeclAffContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exUnop}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExUnop(sdmParser.ExUnopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exUnop}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExUnop(sdmParser.ExUnopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exId}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExId(sdmParser.ExIdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exId}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExId(sdmParser.ExIdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exRead}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExRead(sdmParser.ExReadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exRead}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExRead(sdmParser.ExReadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exFalse}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExFalse(sdmParser.ExFalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exFalse}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExFalse(sdmParser.ExFalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exCall}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExCall(sdmParser.ExCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exCall}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExCall(sdmParser.ExCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exParenthesis}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExParenthesis(sdmParser.ExParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exParenthesis}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExParenthesis(sdmParser.ExParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exInt}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExInt(sdmParser.ExIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exInt}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExInt(sdmParser.ExIntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exBinop}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExBinop(sdmParser.ExBinopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exBinop}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExBinop(sdmParser.ExBinopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exTrue}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExTrue(sdmParser.ExTrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exTrue}
	 * labeled alternative in {@link sdmParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExTrue(sdmParser.ExTrueContext ctx);
}