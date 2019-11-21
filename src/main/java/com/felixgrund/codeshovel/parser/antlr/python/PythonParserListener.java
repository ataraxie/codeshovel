package com.felixgrund.codeshovel.parser.antlr.python;
// Generated from SPL/codeshovel/src/main/antlr4/PythonParser.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AntlrPythonParser}.
 */
public interface PythonParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(AntlrPythonParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(AntlrPythonParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#single_input}.
	 * @param ctx the parse tree
	 */
	void enterSingle_input(AntlrPythonParser.Single_inputContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#single_input}.
	 * @param ctx the parse tree
	 */
	void exitSingle_input(AntlrPythonParser.Single_inputContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#file_input}.
	 * @param ctx the parse tree
	 */
	void enterFile_input(AntlrPythonParser.File_inputContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#file_input}.
	 * @param ctx the parse tree
	 */
	void exitFile_input(AntlrPythonParser.File_inputContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#eval_input}.
	 * @param ctx the parse tree
	 */
	void enterEval_input(AntlrPythonParser.Eval_inputContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#eval_input}.
	 * @param ctx the parse tree
	 */
	void exitEval_input(AntlrPythonParser.Eval_inputContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(AntlrPythonParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(AntlrPythonParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(AntlrPythonParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(AntlrPythonParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stmt(AntlrPythonParser.While_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stmt(AntlrPythonParser.While_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code for_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_stmt(AntlrPythonParser.For_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code for_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_stmt(AntlrPythonParser.For_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code try_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterTry_stmt(AntlrPythonParser.Try_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code try_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitTry_stmt(AntlrPythonParser.Try_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code with_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWith_stmt(AntlrPythonParser.With_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code with_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWith_stmt(AntlrPythonParser.With_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code class_or_func_def_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterClass_or_func_def_stmt(AntlrPythonParser.Class_or_func_def_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code class_or_func_def_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitClass_or_func_def_stmt(AntlrPythonParser.Class_or_func_def_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#suite}.
	 * @param ctx the parse tree
	 */
	void enterSuite(AntlrPythonParser.SuiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#suite}.
	 * @param ctx the parse tree
	 */
	void exitSuite(AntlrPythonParser.SuiteContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#decorator}.
	 * @param ctx the parse tree
	 */
	void enterDecorator(AntlrPythonParser.DecoratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#decorator}.
	 * @param ctx the parse tree
	 */
	void exitDecorator(AntlrPythonParser.DecoratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#elif_clause}.
	 * @param ctx the parse tree
	 */
	void enterElif_clause(AntlrPythonParser.Elif_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#elif_clause}.
	 * @param ctx the parse tree
	 */
	void exitElif_clause(AntlrPythonParser.Elif_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#else_clause}.
	 * @param ctx the parse tree
	 */
	void enterElse_clause(AntlrPythonParser.Else_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#else_clause}.
	 * @param ctx the parse tree
	 */
	void exitElse_clause(AntlrPythonParser.Else_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#finally_clause}.
	 * @param ctx the parse tree
	 */
	void enterFinally_clause(AntlrPythonParser.Finally_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#finally_clause}.
	 * @param ctx the parse tree
	 */
	void exitFinally_clause(AntlrPythonParser.Finally_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#with_item}.
	 * @param ctx the parse tree
	 */
	void enterWith_item(AntlrPythonParser.With_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#with_item}.
	 * @param ctx the parse tree
	 */
	void exitWith_item(AntlrPythonParser.With_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#except_clause}.
	 * @param ctx the parse tree
	 */
	void enterExcept_clause(AntlrPythonParser.Except_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#except_clause}.
	 * @param ctx the parse tree
	 */
	void exitExcept_clause(AntlrPythonParser.Except_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#classdef}.
	 * @param ctx the parse tree
	 */
	void enterClassdef(AntlrPythonParser.ClassdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#classdef}.
	 * @param ctx the parse tree
	 */
	void exitClassdef(AntlrPythonParser.ClassdefContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#funcdef}.
	 * @param ctx the parse tree
	 */
	void enterFuncdef(AntlrPythonParser.FuncdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#funcdef}.
	 * @param ctx the parse tree
	 */
	void exitFuncdef(AntlrPythonParser.FuncdefContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#typedargslist}.
	 * @param ctx the parse tree
	 */
	void enterTypedargslist(AntlrPythonParser.TypedargslistContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#typedargslist}.
	 * @param ctx the parse tree
	 */
	void exitTypedargslist(AntlrPythonParser.TypedargslistContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(AntlrPythonParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(AntlrPythonParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#kwargs}.
	 * @param ctx the parse tree
	 */
	void enterKwargs(AntlrPythonParser.KwargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#kwargs}.
	 * @param ctx the parse tree
	 */
	void exitKwargs(AntlrPythonParser.KwargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#def_parameters}.
	 * @param ctx the parse tree
	 */
	void enterDef_parameters(AntlrPythonParser.Def_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#def_parameters}.
	 * @param ctx the parse tree
	 */
	void exitDef_parameters(AntlrPythonParser.Def_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#def_parameter}.
	 * @param ctx the parse tree
	 */
	void enterDef_parameter(AntlrPythonParser.Def_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#def_parameter}.
	 * @param ctx the parse tree
	 */
	void exitDef_parameter(AntlrPythonParser.Def_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#named_parameter}.
	 * @param ctx the parse tree
	 */
	void enterNamed_parameter(AntlrPythonParser.Named_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#named_parameter}.
	 * @param ctx the parse tree
	 */
	void exitNamed_parameter(AntlrPythonParser.Named_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#simple_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSimple_stmt(AntlrPythonParser.Simple_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#simple_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSimple_stmt(AntlrPythonParser.Simple_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expr_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpr_stmt(AntlrPythonParser.Expr_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expr_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpr_stmt(AntlrPythonParser.Expr_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code print_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterPrint_stmt(AntlrPythonParser.Print_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code print_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitPrint_stmt(AntlrPythonParser.Print_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code del_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDel_stmt(AntlrPythonParser.Del_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code del_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDel_stmt(AntlrPythonParser.Del_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pass_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterPass_stmt(AntlrPythonParser.Pass_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pass_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitPass_stmt(AntlrPythonParser.Pass_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code break_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreak_stmt(AntlrPythonParser.Break_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code break_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreak_stmt(AntlrPythonParser.Break_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continue_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterContinue_stmt(AntlrPythonParser.Continue_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continue_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitContinue_stmt(AntlrPythonParser.Continue_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code return_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(AntlrPythonParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code return_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(AntlrPythonParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code raise_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRaise_stmt(AntlrPythonParser.Raise_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code raise_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRaise_stmt(AntlrPythonParser.Raise_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code yield_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterYield_stmt(AntlrPythonParser.Yield_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code yield_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitYield_stmt(AntlrPythonParser.Yield_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code import_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterImport_stmt(AntlrPythonParser.Import_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code import_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitImport_stmt(AntlrPythonParser.Import_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code from_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFrom_stmt(AntlrPythonParser.From_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code from_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFrom_stmt(AntlrPythonParser.From_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code global_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterGlobal_stmt(AntlrPythonParser.Global_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code global_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitGlobal_stmt(AntlrPythonParser.Global_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exec_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExec_stmt(AntlrPythonParser.Exec_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exec_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExec_stmt(AntlrPythonParser.Exec_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assert_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAssert_stmt(AntlrPythonParser.Assert_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assert_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAssert_stmt(AntlrPythonParser.Assert_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonlocal_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterNonlocal_stmt(AntlrPythonParser.Nonlocal_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonlocal_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitNonlocal_stmt(AntlrPythonParser.Nonlocal_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#testlist_star_expr}.
	 * @param ctx the parse tree
	 */
	void enterTestlist_star_expr(AntlrPythonParser.Testlist_star_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#testlist_star_expr}.
	 * @param ctx the parse tree
	 */
	void exitTestlist_star_expr(AntlrPythonParser.Testlist_star_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#star_expr}.
	 * @param ctx the parse tree
	 */
	void enterStar_expr(AntlrPythonParser.Star_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#star_expr}.
	 * @param ctx the parse tree
	 */
	void exitStar_expr(AntlrPythonParser.Star_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#assign_part}.
	 * @param ctx the parse tree
	 */
	void enterAssign_part(AntlrPythonParser.Assign_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#assign_part}.
	 * @param ctx the parse tree
	 */
	void exitAssign_part(AntlrPythonParser.Assign_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#exprlist}.
	 * @param ctx the parse tree
	 */
	void enterExprlist(AntlrPythonParser.ExprlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#exprlist}.
	 * @param ctx the parse tree
	 */
	void exitExprlist(AntlrPythonParser.ExprlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#import_as_names}.
	 * @param ctx the parse tree
	 */
	void enterImport_as_names(AntlrPythonParser.Import_as_namesContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#import_as_names}.
	 * @param ctx the parse tree
	 */
	void exitImport_as_names(AntlrPythonParser.Import_as_namesContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#import_as_name}.
	 * @param ctx the parse tree
	 */
	void enterImport_as_name(AntlrPythonParser.Import_as_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#import_as_name}.
	 * @param ctx the parse tree
	 */
	void exitImport_as_name(AntlrPythonParser.Import_as_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#dotted_as_names}.
	 * @param ctx the parse tree
	 */
	void enterDotted_as_names(AntlrPythonParser.Dotted_as_namesContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#dotted_as_names}.
	 * @param ctx the parse tree
	 */
	void exitDotted_as_names(AntlrPythonParser.Dotted_as_namesContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#dotted_as_name}.
	 * @param ctx the parse tree
	 */
	void enterDotted_as_name(AntlrPythonParser.Dotted_as_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#dotted_as_name}.
	 * @param ctx the parse tree
	 */
	void exitDotted_as_name(AntlrPythonParser.Dotted_as_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#test}.
	 * @param ctx the parse tree
	 */
	void enterTest(AntlrPythonParser.TestContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#test}.
	 * @param ctx the parse tree
	 */
	void exitTest(AntlrPythonParser.TestContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#varargslist}.
	 * @param ctx the parse tree
	 */
	void enterVarargslist(AntlrPythonParser.VarargslistContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#varargslist}.
	 * @param ctx the parse tree
	 */
	void exitVarargslist(AntlrPythonParser.VarargslistContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#vardef_parameters}.
	 * @param ctx the parse tree
	 */
	void enterVardef_parameters(AntlrPythonParser.Vardef_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#vardef_parameters}.
	 * @param ctx the parse tree
	 */
	void exitVardef_parameters(AntlrPythonParser.Vardef_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#vardef_parameter}.
	 * @param ctx the parse tree
	 */
	void enterVardef_parameter(AntlrPythonParser.Vardef_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#vardef_parameter}.
	 * @param ctx the parse tree
	 */
	void exitVardef_parameter(AntlrPythonParser.Vardef_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#varargs}.
	 * @param ctx the parse tree
	 */
	void enterVarargs(AntlrPythonParser.VarargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#varargs}.
	 * @param ctx the parse tree
	 */
	void exitVarargs(AntlrPythonParser.VarargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#varkwargs}.
	 * @param ctx the parse tree
	 */
	void enterVarkwargs(AntlrPythonParser.VarkwargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#varkwargs}.
	 * @param ctx the parse tree
	 */
	void exitVarkwargs(AntlrPythonParser.VarkwargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#logical_test}.
	 * @param ctx the parse tree
	 */
	void enterLogical_test(AntlrPythonParser.Logical_testContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#logical_test}.
	 * @param ctx the parse tree
	 */
	void exitLogical_test(AntlrPythonParser.Logical_testContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(AntlrPythonParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(AntlrPythonParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(AntlrPythonParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(AntlrPythonParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(AntlrPythonParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(AntlrPythonParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#dictorsetmaker}.
	 * @param ctx the parse tree
	 */
	void enterDictorsetmaker(AntlrPythonParser.DictorsetmakerContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#dictorsetmaker}.
	 * @param ctx the parse tree
	 */
	void exitDictorsetmaker(AntlrPythonParser.DictorsetmakerContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#testlist_comp}.
	 * @param ctx the parse tree
	 */
	void enterTestlist_comp(AntlrPythonParser.Testlist_compContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#testlist_comp}.
	 * @param ctx the parse tree
	 */
	void exitTestlist_comp(AntlrPythonParser.Testlist_compContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#testlist}.
	 * @param ctx the parse tree
	 */
	void enterTestlist(AntlrPythonParser.TestlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#testlist}.
	 * @param ctx the parse tree
	 */
	void exitTestlist(AntlrPythonParser.TestlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#dotted_name}.
	 * @param ctx the parse tree
	 */
	void enterDotted_name(AntlrPythonParser.Dotted_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#dotted_name}.
	 * @param ctx the parse tree
	 */
	void exitDotted_name(AntlrPythonParser.Dotted_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(AntlrPythonParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(AntlrPythonParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(AntlrPythonParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(AntlrPythonParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterInteger(AntlrPythonParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitInteger(AntlrPythonParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#yield_expr}.
	 * @param ctx the parse tree
	 */
	void enterYield_expr(AntlrPythonParser.Yield_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#yield_expr}.
	 * @param ctx the parse tree
	 */
	void exitYield_expr(AntlrPythonParser.Yield_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#yield_arg}.
	 * @param ctx the parse tree
	 */
	void enterYield_arg(AntlrPythonParser.Yield_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#yield_arg}.
	 * @param ctx the parse tree
	 */
	void exitYield_arg(AntlrPythonParser.Yield_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#trailer}.
	 * @param ctx the parse tree
	 */
	void enterTrailer(AntlrPythonParser.TrailerContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#trailer}.
	 * @param ctx the parse tree
	 */
	void exitTrailer(AntlrPythonParser.TrailerContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(AntlrPythonParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(AntlrPythonParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#arglist}.
	 * @param ctx the parse tree
	 */
	void enterArglist(AntlrPythonParser.ArglistContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#arglist}.
	 * @param ctx the parse tree
	 */
	void exitArglist(AntlrPythonParser.ArglistContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(AntlrPythonParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(AntlrPythonParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#subscriptlist}.
	 * @param ctx the parse tree
	 */
	void enterSubscriptlist(AntlrPythonParser.SubscriptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#subscriptlist}.
	 * @param ctx the parse tree
	 */
	void exitSubscriptlist(AntlrPythonParser.SubscriptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#subscript}.
	 * @param ctx the parse tree
	 */
	void enterSubscript(AntlrPythonParser.SubscriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#subscript}.
	 * @param ctx the parse tree
	 */
	void exitSubscript(AntlrPythonParser.SubscriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#sliceop}.
	 * @param ctx the parse tree
	 */
	void enterSliceop(AntlrPythonParser.SliceopContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#sliceop}.
	 * @param ctx the parse tree
	 */
	void exitSliceop(AntlrPythonParser.SliceopContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#comp_for}.
	 * @param ctx the parse tree
	 */
	void enterComp_for(AntlrPythonParser.Comp_forContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#comp_for}.
	 * @param ctx the parse tree
	 */
	void exitComp_for(AntlrPythonParser.Comp_forContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrPythonParser#comp_iter}.
	 * @param ctx the parse tree
	 */
	void enterComp_iter(AntlrPythonParser.Comp_iterContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrPythonParser#comp_iter}.
	 * @param ctx the parse tree
	 */
	void exitComp_iter(AntlrPythonParser.Comp_iterContext ctx);
}