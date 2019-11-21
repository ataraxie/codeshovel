package com.felixgrund.codeshovel.parser.antlr.c;
// Generated from SPL/codeshovel/src/main/java/com/felixgrund/codeshovel/parser/antlr/c/C.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AntlrCParser}.
 */
public interface CListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(AntlrCParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(AntlrCParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#genericSelection}.
	 * @param ctx the parse tree
	 */
	void enterGenericSelection(AntlrCParser.GenericSelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#genericSelection}.
	 * @param ctx the parse tree
	 */
	void exitGenericSelection(AntlrCParser.GenericSelectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#genericAssocList}.
	 * @param ctx the parse tree
	 */
	void enterGenericAssocList(AntlrCParser.GenericAssocListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#genericAssocList}.
	 * @param ctx the parse tree
	 */
	void exitGenericAssocList(AntlrCParser.GenericAssocListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#genericAssociation}.
	 * @param ctx the parse tree
	 */
	void enterGenericAssociation(AntlrCParser.GenericAssociationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#genericAssociation}.
	 * @param ctx the parse tree
	 */
	void exitGenericAssociation(AntlrCParser.GenericAssociationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(AntlrCParser.PostfixExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(AntlrCParser.PostfixExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#argumentExpressionList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentExpressionList(AntlrCParser.ArgumentExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#argumentExpressionList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentExpressionList(AntlrCParser.ArgumentExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(AntlrCParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(AntlrCParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(AntlrCParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(AntlrCParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(AntlrCParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(AntlrCParser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(AntlrCParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(AntlrCParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(AntlrCParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(AntlrCParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterShiftExpression(AntlrCParser.ShiftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitShiftExpression(AntlrCParser.ShiftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(AntlrCParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(AntlrCParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(AntlrCParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(AntlrCParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(AntlrCParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(AntlrCParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterExclusiveOrExpression(AntlrCParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitExclusiveOrExpression(AntlrCParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterInclusiveOrExpression(AntlrCParser.InclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitInclusiveOrExpression(AntlrCParser.InclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAndExpression(AntlrCParser.LogicalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAndExpression(AntlrCParser.LogicalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOrExpression(AntlrCParser.LogicalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOrExpression(AntlrCParser.LogicalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(AntlrCParser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(AntlrCParser.ConditionalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentExpression(AntlrCParser.AssignmentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentExpression(AntlrCParser.AssignmentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(AntlrCParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(AntlrCParser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(AntlrCParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(AntlrCParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(AntlrCParser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(AntlrCParser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(AntlrCParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(AntlrCParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#declarationSpecifiers}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationSpecifiers(AntlrCParser.DeclarationSpecifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#declarationSpecifiers}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationSpecifiers(AntlrCParser.DeclarationSpecifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#declarationSpecifiers2}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationSpecifiers2(AntlrCParser.DeclarationSpecifiers2Context ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#declarationSpecifiers2}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationSpecifiers2(AntlrCParser.DeclarationSpecifiers2Context ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#declarationSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationSpecifier(AntlrCParser.DeclarationSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#declarationSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationSpecifier(AntlrCParser.DeclarationSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#initDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterInitDeclaratorList(AntlrCParser.InitDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#initDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitInitDeclaratorList(AntlrCParser.InitDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#initDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterInitDeclarator(AntlrCParser.InitDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#initDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitInitDeclarator(AntlrCParser.InitDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#storageClassSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterStorageClassSpecifier(AntlrCParser.StorageClassSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#storageClassSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitStorageClassSpecifier(AntlrCParser.StorageClassSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeSpecifier(AntlrCParser.TypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeSpecifier(AntlrCParser.TypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#structOrUnionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterStructOrUnionSpecifier(AntlrCParser.StructOrUnionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#structOrUnionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitStructOrUnionSpecifier(AntlrCParser.StructOrUnionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#structOrUnion}.
	 * @param ctx the parse tree
	 */
	void enterStructOrUnion(AntlrCParser.StructOrUnionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#structOrUnion}.
	 * @param ctx the parse tree
	 */
	void exitStructOrUnion(AntlrCParser.StructOrUnionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#structDeclarationList}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclarationList(AntlrCParser.StructDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#structDeclarationList}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclarationList(AntlrCParser.StructDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#structDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclaration(AntlrCParser.StructDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#structDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclaration(AntlrCParser.StructDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#specifierQualifierList}.
	 * @param ctx the parse tree
	 */
	void enterSpecifierQualifierList(AntlrCParser.SpecifierQualifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#specifierQualifierList}.
	 * @param ctx the parse tree
	 */
	void exitSpecifierQualifierList(AntlrCParser.SpecifierQualifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#structDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclaratorList(AntlrCParser.StructDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#structDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclaratorList(AntlrCParser.StructDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#structDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclarator(AntlrCParser.StructDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#structDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclarator(AntlrCParser.StructDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#enumSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterEnumSpecifier(AntlrCParser.EnumSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#enumSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitEnumSpecifier(AntlrCParser.EnumSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#enumeratorList}.
	 * @param ctx the parse tree
	 */
	void enterEnumeratorList(AntlrCParser.EnumeratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#enumeratorList}.
	 * @param ctx the parse tree
	 */
	void exitEnumeratorList(AntlrCParser.EnumeratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#enumerator}.
	 * @param ctx the parse tree
	 */
	void enterEnumerator(AntlrCParser.EnumeratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#enumerator}.
	 * @param ctx the parse tree
	 */
	void exitEnumerator(AntlrCParser.EnumeratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#enumerationConstant}.
	 * @param ctx the parse tree
	 */
	void enterEnumerationConstant(AntlrCParser.EnumerationConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#enumerationConstant}.
	 * @param ctx the parse tree
	 */
	void exitEnumerationConstant(AntlrCParser.EnumerationConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#atomicTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterAtomicTypeSpecifier(AntlrCParser.AtomicTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#atomicTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitAtomicTypeSpecifier(AntlrCParser.AtomicTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#typeQualifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeQualifier(AntlrCParser.TypeQualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#typeQualifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeQualifier(AntlrCParser.TypeQualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#functionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterFunctionSpecifier(AntlrCParser.FunctionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#functionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitFunctionSpecifier(AntlrCParser.FunctionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#alignmentSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterAlignmentSpecifier(AntlrCParser.AlignmentSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#alignmentSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitAlignmentSpecifier(AntlrCParser.AlignmentSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#declarator}.
	 * @param ctx the parse tree
	 */
	void enterDeclarator(AntlrCParser.DeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#declarator}.
	 * @param ctx the parse tree
	 */
	void exitDeclarator(AntlrCParser.DeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#directDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterDirectDeclarator(AntlrCParser.DirectDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#directDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitDirectDeclarator(AntlrCParser.DirectDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#gccDeclaratorExtension}.
	 * @param ctx the parse tree
	 */
	void enterGccDeclaratorExtension(AntlrCParser.GccDeclaratorExtensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#gccDeclaratorExtension}.
	 * @param ctx the parse tree
	 */
	void exitGccDeclaratorExtension(AntlrCParser.GccDeclaratorExtensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#gccAttributeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterGccAttributeSpecifier(AntlrCParser.GccAttributeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#gccAttributeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitGccAttributeSpecifier(AntlrCParser.GccAttributeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#gccAttributeList}.
	 * @param ctx the parse tree
	 */
	void enterGccAttributeList(AntlrCParser.GccAttributeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#gccAttributeList}.
	 * @param ctx the parse tree
	 */
	void exitGccAttributeList(AntlrCParser.GccAttributeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#gccAttribute}.
	 * @param ctx the parse tree
	 */
	void enterGccAttribute(AntlrCParser.GccAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#gccAttribute}.
	 * @param ctx the parse tree
	 */
	void exitGccAttribute(AntlrCParser.GccAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#nestedParenthesesBlock}.
	 * @param ctx the parse tree
	 */
	void enterNestedParenthesesBlock(AntlrCParser.NestedParenthesesBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#nestedParenthesesBlock}.
	 * @param ctx the parse tree
	 */
	void exitNestedParenthesesBlock(AntlrCParser.NestedParenthesesBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#pointer}.
	 * @param ctx the parse tree
	 */
	void enterPointer(AntlrCParser.PointerContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#pointer}.
	 * @param ctx the parse tree
	 */
	void exitPointer(AntlrCParser.PointerContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#typeQualifierList}.
	 * @param ctx the parse tree
	 */
	void enterTypeQualifierList(AntlrCParser.TypeQualifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#typeQualifierList}.
	 * @param ctx the parse tree
	 */
	void exitTypeQualifierList(AntlrCParser.TypeQualifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#parameterTypeList}.
	 * @param ctx the parse tree
	 */
	void enterParameterTypeList(AntlrCParser.ParameterTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#parameterTypeList}.
	 * @param ctx the parse tree
	 */
	void exitParameterTypeList(AntlrCParser.ParameterTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(AntlrCParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(AntlrCParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclaration(AntlrCParser.ParameterDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclaration(AntlrCParser.ParameterDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(AntlrCParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(AntlrCParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(AntlrCParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(AntlrCParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#abstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterAbstractDeclarator(AntlrCParser.AbstractDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#abstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitAbstractDeclarator(AntlrCParser.AbstractDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#directAbstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterDirectAbstractDeclarator(AntlrCParser.DirectAbstractDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#directAbstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitDirectAbstractDeclarator(AntlrCParser.DirectAbstractDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#typedefName}.
	 * @param ctx the parse tree
	 */
	void enterTypedefName(AntlrCParser.TypedefNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#typedefName}.
	 * @param ctx the parse tree
	 */
	void exitTypedefName(AntlrCParser.TypedefNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#initializer}.
	 * @param ctx the parse tree
	 */
	void enterInitializer(AntlrCParser.InitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#initializer}.
	 * @param ctx the parse tree
	 */
	void exitInitializer(AntlrCParser.InitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#initializerList}.
	 * @param ctx the parse tree
	 */
	void enterInitializerList(AntlrCParser.InitializerListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#initializerList}.
	 * @param ctx the parse tree
	 */
	void exitInitializerList(AntlrCParser.InitializerListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#designation}.
	 * @param ctx the parse tree
	 */
	void enterDesignation(AntlrCParser.DesignationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#designation}.
	 * @param ctx the parse tree
	 */
	void exitDesignation(AntlrCParser.DesignationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#designatorList}.
	 * @param ctx the parse tree
	 */
	void enterDesignatorList(AntlrCParser.DesignatorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#designatorList}.
	 * @param ctx the parse tree
	 */
	void exitDesignatorList(AntlrCParser.DesignatorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#designator}.
	 * @param ctx the parse tree
	 */
	void enterDesignator(AntlrCParser.DesignatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#designator}.
	 * @param ctx the parse tree
	 */
	void exitDesignator(AntlrCParser.DesignatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#staticAssertDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStaticAssertDeclaration(AntlrCParser.StaticAssertDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#staticAssertDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStaticAssertDeclaration(AntlrCParser.StaticAssertDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(AntlrCParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(AntlrCParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void enterLabeledStatement(AntlrCParser.LabeledStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void exitLabeledStatement(AntlrCParser.LabeledStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(AntlrCParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(AntlrCParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#blockItemList}.
	 * @param ctx the parse tree
	 */
	void enterBlockItemList(AntlrCParser.BlockItemListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#blockItemList}.
	 * @param ctx the parse tree
	 */
	void exitBlockItemList(AntlrCParser.BlockItemListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void enterBlockItem(AntlrCParser.BlockItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void exitBlockItem(AntlrCParser.BlockItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(AntlrCParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(AntlrCParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectionStatement(AntlrCParser.SelectionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectionStatement(AntlrCParser.SelectionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterationStatement(AntlrCParser.IterationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterationStatement(AntlrCParser.IterationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void enterForCondition(AntlrCParser.ForConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void exitForCondition(AntlrCParser.ForConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#forDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterForDeclaration(AntlrCParser.ForDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#forDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitForDeclaration(AntlrCParser.ForDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#forExpression}.
	 * @param ctx the parse tree
	 */
	void enterForExpression(AntlrCParser.ForExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#forExpression}.
	 * @param ctx the parse tree
	 */
	void exitForExpression(AntlrCParser.ForExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void enterJumpStatement(AntlrCParser.JumpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void exitJumpStatement(AntlrCParser.JumpStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(AntlrCParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(AntlrCParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#translationUnit}.
	 * @param ctx the parse tree
	 */
	void enterTranslationUnit(AntlrCParser.TranslationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#translationUnit}.
	 * @param ctx the parse tree
	 */
	void exitTranslationUnit(AntlrCParser.TranslationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#externalDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterExternalDeclaration(AntlrCParser.ExternalDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#externalDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitExternalDeclaration(AntlrCParser.ExternalDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinition(AntlrCParser.FunctionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinition(AntlrCParser.FunctionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AntlrCParser#declarationList}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationList(AntlrCParser.DeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntlrCParser#declarationList}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationList(AntlrCParser.DeclarationListContext ctx);
}