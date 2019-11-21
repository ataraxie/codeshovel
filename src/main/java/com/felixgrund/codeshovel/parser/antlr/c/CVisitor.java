package com.felixgrund.codeshovel.parser.antlr.c;
// Generated from SPL/codeshovel/src/main/java/com/felixgrund/codeshovel/parser/antlr/c/C.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AntlrCParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(AntlrCParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#genericSelection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericSelection(AntlrCParser.GenericSelectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#genericAssocList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericAssocList(AntlrCParser.GenericAssocListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#genericAssociation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericAssociation(AntlrCParser.GenericAssociationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#postfixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpression(AntlrCParser.PostfixExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#argumentExpressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentExpressionList(AntlrCParser.ArgumentExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(AntlrCParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#unaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(AntlrCParser.UnaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#castExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpression(AntlrCParser.CastExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(AntlrCParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(AntlrCParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftExpression(AntlrCParser.ShiftExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#relationalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(AntlrCParser.RelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(AntlrCParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(AntlrCParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusiveOrExpression(AntlrCParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclusiveOrExpression(AntlrCParser.InclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndExpression(AntlrCParser.LogicalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOrExpression(AntlrCParser.LogicalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#conditionalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalExpression(AntlrCParser.ConditionalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#assignmentExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExpression(AntlrCParser.AssignmentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(AntlrCParser.AssignmentOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(AntlrCParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#constantExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpression(AntlrCParser.ConstantExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(AntlrCParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#declarationSpecifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationSpecifiers(AntlrCParser.DeclarationSpecifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#declarationSpecifiers2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationSpecifiers2(AntlrCParser.DeclarationSpecifiers2Context ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#declarationSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationSpecifier(AntlrCParser.DeclarationSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#initDeclaratorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitDeclaratorList(AntlrCParser.InitDeclaratorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#initDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitDeclarator(AntlrCParser.InitDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#storageClassSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorageClassSpecifier(AntlrCParser.StorageClassSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#typeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpecifier(AntlrCParser.TypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#structOrUnionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructOrUnionSpecifier(AntlrCParser.StructOrUnionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#structOrUnion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructOrUnion(AntlrCParser.StructOrUnionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#structDeclarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDeclarationList(AntlrCParser.StructDeclarationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#structDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDeclaration(AntlrCParser.StructDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#specifierQualifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecifierQualifierList(AntlrCParser.SpecifierQualifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#structDeclaratorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDeclaratorList(AntlrCParser.StructDeclaratorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#structDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDeclarator(AntlrCParser.StructDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#enumSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumSpecifier(AntlrCParser.EnumSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#enumeratorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumeratorList(AntlrCParser.EnumeratorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#enumerator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumerator(AntlrCParser.EnumeratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#enumerationConstant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumerationConstant(AntlrCParser.EnumerationConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#atomicTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicTypeSpecifier(AntlrCParser.AtomicTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#typeQualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeQualifier(AntlrCParser.TypeQualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#functionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionSpecifier(AntlrCParser.FunctionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#alignmentSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlignmentSpecifier(AntlrCParser.AlignmentSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarator(AntlrCParser.DeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#directDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectDeclarator(AntlrCParser.DirectDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#gccDeclaratorExtension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGccDeclaratorExtension(AntlrCParser.GccDeclaratorExtensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#gccAttributeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGccAttributeSpecifier(AntlrCParser.GccAttributeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#gccAttributeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGccAttributeList(AntlrCParser.GccAttributeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#gccAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGccAttribute(AntlrCParser.GccAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#nestedParenthesesBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedParenthesesBlock(AntlrCParser.NestedParenthesesBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#pointer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointer(AntlrCParser.PointerContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#typeQualifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeQualifierList(AntlrCParser.TypeQualifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#parameterTypeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterTypeList(AntlrCParser.ParameterTypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(AntlrCParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclaration(AntlrCParser.ParameterDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(AntlrCParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(AntlrCParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#abstractDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstractDeclarator(AntlrCParser.AbstractDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#directAbstractDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectAbstractDeclarator(AntlrCParser.DirectAbstractDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#typedefName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefName(AntlrCParser.TypedefNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#initializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializer(AntlrCParser.InitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#initializerList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializerList(AntlrCParser.InitializerListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#designation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDesignation(AntlrCParser.DesignationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#designatorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDesignatorList(AntlrCParser.DesignatorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#designator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDesignator(AntlrCParser.DesignatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#staticAssertDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticAssertDeclaration(AntlrCParser.StaticAssertDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(AntlrCParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#labeledStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabeledStatement(AntlrCParser.LabeledStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(AntlrCParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#blockItemList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockItemList(AntlrCParser.BlockItemListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#blockItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockItem(AntlrCParser.BlockItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(AntlrCParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#selectionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectionStatement(AntlrCParser.SelectionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#iterationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterationStatement(AntlrCParser.IterationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#forCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForCondition(AntlrCParser.ForConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#forDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForDeclaration(AntlrCParser.ForDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#forExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForExpression(AntlrCParser.ForExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#jumpStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpStatement(AntlrCParser.JumpStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(AntlrCParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#translationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTranslationUnit(AntlrCParser.TranslationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#externalDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternalDeclaration(AntlrCParser.ExternalDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#functionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDefinition(AntlrCParser.FunctionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrCParser#declarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationList(AntlrCParser.DeclarationListContext ctx);
}