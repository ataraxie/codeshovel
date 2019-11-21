package com.felixgrund.codeshovel.parser.antlr.python;
// Generated from SPL/codeshovel/src/main/antlr4/PythonParser.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AntlrPythonParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PythonParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(AntlrPythonParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#single_input}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_input(AntlrPythonParser.Single_inputContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#file_input}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_input(AntlrPythonParser.File_inputContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#eval_input}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEval_input(AntlrPythonParser.Eval_inputContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(AntlrPythonParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code if_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stmt(AntlrPythonParser.If_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_stmt(AntlrPythonParser.While_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code for_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_stmt(AntlrPythonParser.For_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code try_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTry_stmt(AntlrPythonParser.Try_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code with_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_stmt(AntlrPythonParser.With_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code class_or_func_def_stmt}
	 * labeled alternative in {@link AntlrPythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_or_func_def_stmt(AntlrPythonParser.Class_or_func_def_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#suite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuite(AntlrPythonParser.SuiteContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#decorator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecorator(AntlrPythonParser.DecoratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#elif_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElif_clause(AntlrPythonParser.Elif_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#else_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_clause(AntlrPythonParser.Else_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#finally_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFinally_clause(AntlrPythonParser.Finally_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#with_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_item(AntlrPythonParser.With_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#except_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExcept_clause(AntlrPythonParser.Except_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#classdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassdef(AntlrPythonParser.ClassdefContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#funcdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncdef(AntlrPythonParser.FuncdefContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#typedargslist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedargslist(AntlrPythonParser.TypedargslistContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(AntlrPythonParser.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#kwargs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKwargs(AntlrPythonParser.KwargsContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#def_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_parameters(AntlrPythonParser.Def_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#def_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_parameter(AntlrPythonParser.Def_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#named_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamed_parameter(AntlrPythonParser.Named_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#simple_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_stmt(AntlrPythonParser.Simple_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expr_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_stmt(AntlrPythonParser.Expr_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code print_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint_stmt(AntlrPythonParser.Print_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code del_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDel_stmt(AntlrPythonParser.Del_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pass_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPass_stmt(AntlrPythonParser.Pass_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code break_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_stmt(AntlrPythonParser.Break_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continue_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_stmt(AntlrPythonParser.Continue_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code return_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(AntlrPythonParser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code raise_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaise_stmt(AntlrPythonParser.Raise_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code yield_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYield_stmt(AntlrPythonParser.Yield_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code import_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_stmt(AntlrPythonParser.Import_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code from_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom_stmt(AntlrPythonParser.From_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code global_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal_stmt(AntlrPythonParser.Global_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exec_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExec_stmt(AntlrPythonParser.Exec_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assert_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssert_stmt(AntlrPythonParser.Assert_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nonlocal_stmt}
	 * labeled alternative in {@link AntlrPythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonlocal_stmt(AntlrPythonParser.Nonlocal_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#testlist_star_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestlist_star_expr(AntlrPythonParser.Testlist_star_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#star_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStar_expr(AntlrPythonParser.Star_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#assign_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_part(AntlrPythonParser.Assign_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#exprlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprlist(AntlrPythonParser.ExprlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#import_as_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_as_names(AntlrPythonParser.Import_as_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#import_as_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_as_name(AntlrPythonParser.Import_as_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#dotted_as_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotted_as_names(AntlrPythonParser.Dotted_as_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#dotted_as_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotted_as_name(AntlrPythonParser.Dotted_as_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest(AntlrPythonParser.TestContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#varargslist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarargslist(AntlrPythonParser.VarargslistContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#vardef_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardef_parameters(AntlrPythonParser.Vardef_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#vardef_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardef_parameter(AntlrPythonParser.Vardef_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#varargs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarargs(AntlrPythonParser.VarargsContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#varkwargs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarkwargs(AntlrPythonParser.VarkwargsContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#logical_test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_test(AntlrPythonParser.Logical_testContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(AntlrPythonParser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(AntlrPythonParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(AntlrPythonParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#dictorsetmaker}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictorsetmaker(AntlrPythonParser.DictorsetmakerContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#testlist_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestlist_comp(AntlrPythonParser.Testlist_compContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#testlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestlist(AntlrPythonParser.TestlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#dotted_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotted_name(AntlrPythonParser.Dotted_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(AntlrPythonParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(AntlrPythonParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(AntlrPythonParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#yield_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYield_expr(AntlrPythonParser.Yield_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#yield_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYield_arg(AntlrPythonParser.Yield_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#trailer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailer(AntlrPythonParser.TrailerContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(AntlrPythonParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#arglist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArglist(AntlrPythonParser.ArglistContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(AntlrPythonParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#subscriptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscriptlist(AntlrPythonParser.SubscriptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#subscript}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscript(AntlrPythonParser.SubscriptContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#sliceop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSliceop(AntlrPythonParser.SliceopContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#comp_for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_for(AntlrPythonParser.Comp_forContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrPythonParser#comp_iter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_iter(AntlrPythonParser.Comp_iterContext ctx);
}