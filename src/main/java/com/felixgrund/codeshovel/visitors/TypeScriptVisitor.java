package com.felixgrund.codeshovel.visitors;

import com.eclipsesource.v8.*;
import com.eclipsesource.v8.utils.MemoryManager;
import com.felixgrund.codeshovel.util.TypeScript;

import java.util.HashMap;
import java.util.Map;

public class TypeScriptVisitor {

	protected final V8Object ts;
	private V8Object syntaxKind;
	private MemoryManager scope;

	private static final Map<String, Integer> syntaxKindCache = new HashMap<String, Integer>();

	public TypeScriptVisitor() {
		ts = TypeScript.getInstance().getTS();
		init();
	}

	public void visit(String source) {
		visit(getSource(source));
		clear();
	}

	protected V8Object getSource(String source) {
		V8Object scriptTargetEnum = ts.getObject("ScriptTarget");
		V8Object scriptKindEnum = ts.getObject("ScriptKind");

		String name = "temp.ts";
		// TODO is this the script target we want to be using?
		int scriptTarget = scriptTargetEnum.getInteger("ES2015");
		int scriptKind = scriptKindEnum.getInteger("TS");

		// const sourceFile = createSourceFile(name, source, scriptTarget, setParentNodes, scriptKind);
		V8Array parameters = new V8Array(ts.getRuntime())
				.push(name)
				.push(source)
				.push(scriptTarget)
				.push(true)
				.push(scriptKind);
		return ts.executeObjectFunction("createSourceFile", parameters);
	}

	private void init() {
		scope = new MemoryManager(TypeScript.getInstance().getRuntime());
		syntaxKind = ts.getObject("SyntaxKind");
	}

	protected void clear() {
		scope.release();
		init();
	}

	protected void visit(V8Object node) {
		if (isKind(node, "Unknown")) {
			visitUnknown(node);
		} else if (isKind(node, "EndOfFileToken")) {
			visitEndOfFileToken(node);
		} else if (isKind(node, "SingleLineCommentTrivia")) {
			visitSingleLineCommentTrivia(node);
		} else if (isKind(node, "MultiLineCommentTrivia")) {
			visitMultiLineCommentTrivia(node);
		} else if (isKind(node, "NewLineTrivia")) {
			visitNewLineTrivia(node);
		} else if (isKind(node, "WhitespaceTrivia")) {
			visitWhitespaceTrivia(node);
		} else if (isKind(node, "ShebangTrivia")) {
			visitShebangTrivia(node);
		} else if (isKind(node, "ConflictMarkerTrivia")) {
			visitConflictMarkerTrivia(node);
		} else if (isKind(node, "NumericLiteral")) {
			visitNumericLiteral(node);
		} else if (isKind(node, "BigIntLiteral")) {
			visitBigIntLiteral(node);
		} else if (isKind(node, "StringLiteral")) {
			visitStringLiteral(node);
		} else if (isKind(node, "JsxText")) {
			visitJsxText(node);
		} else if (isKind(node, "JsxTextAllWhiteSpaces")) {
			visitJsxTextAllWhiteSpaces(node);
		} else if (isKind(node, "RegularExpressionLiteral")) {
			visitRegularExpressionLiteral(node);
		} else if (isKind(node, "NoSubstitutionTemplateLiteral")) {
			visitNoSubstitutionTemplateLiteral(node);
		} else if (isKind(node, "TemplateHead")) {
			visitTemplateHead(node);
		} else if (isKind(node, "TemplateMiddle")) {
			visitTemplateMiddle(node);
		} else if (isKind(node, "TemplateTail")) {
			visitTemplateTail(node);
		} else if (isKind(node, "OpenBraceToken")) {
			visitOpenBraceToken(node);
		} else if (isKind(node, "CloseBraceToken")) {
			visitCloseBraceToken(node);
		} else if (isKind(node, "OpenParenToken")) {
			visitOpenParenToken(node);
		} else if (isKind(node, "CloseParenToken")) {
			visitCloseParenToken(node);
		} else if (isKind(node, "OpenBracketToken")) {
			visitOpenBracketToken(node);
		} else if (isKind(node, "CloseBracketToken")) {
			visitCloseBracketToken(node);
		} else if (isKind(node, "DotToken")) {
			visitDotToken(node);
		} else if (isKind(node, "DotDotDotToken")) {
			visitDotDotDotToken(node);
		} else if (isKind(node, "SemicolonToken")) {
			visitSemicolonToken(node);
		} else if (isKind(node, "CommaToken")) {
			visitCommaToken(node);
		} else if (isKind(node, "QuestionDotToken")) {
			visitQuestionDotToken(node);
		} else if (isKind(node, "LessThanToken")) {
			visitLessThanToken(node);
		} else if (isKind(node, "LessThanSlashToken")) {
			visitLessThanSlashToken(node);
		} else if (isKind(node, "GreaterThanToken")) {
			visitGreaterThanToken(node);
		} else if (isKind(node, "LessThanEqualsToken")) {
			visitLessThanEqualsToken(node);
		} else if (isKind(node, "GreaterThanEqualsToken")) {
			visitGreaterThanEqualsToken(node);
		} else if (isKind(node, "EqualsEqualsToken")) {
			visitEqualsEqualsToken(node);
		} else if (isKind(node, "ExclamationEqualsToken")) {
			visitExclamationEqualsToken(node);
		} else if (isKind(node, "EqualsEqualsEqualsToken")) {
			visitEqualsEqualsEqualsToken(node);
		} else if (isKind(node, "ExclamationEqualsEqualsToken")) {
			visitExclamationEqualsEqualsToken(node);
		} else if (isKind(node, "EqualsGreaterThanToken")) {
			visitEqualsGreaterThanToken(node);
		} else if (isKind(node, "PlusToken")) {
			visitPlusToken(node);
		} else if (isKind(node, "MinusToken")) {
			visitMinusToken(node);
		} else if (isKind(node, "AsteriskToken")) {
			visitAsteriskToken(node);
		} else if (isKind(node, "AsteriskAsteriskToken")) {
			visitAsteriskAsteriskToken(node);
		} else if (isKind(node, "SlashToken")) {
			visitSlashToken(node);
		} else if (isKind(node, "PercentToken")) {
			visitPercentToken(node);
		} else if (isKind(node, "PlusPlusToken")) {
			visitPlusPlusToken(node);
		} else if (isKind(node, "MinusMinusToken")) {
			visitMinusMinusToken(node);
		} else if (isKind(node, "LessThanLessThanToken")) {
			visitLessThanLessThanToken(node);
		} else if (isKind(node, "GreaterThanGreaterThanToken")) {
			visitGreaterThanGreaterThanToken(node);
		} else if (isKind(node, "GreaterThanGreaterThanGreaterThanToken")) {
			visitGreaterThanGreaterThanGreaterThanToken(node);
		} else if (isKind(node, "AmpersandToken")) {
			visitAmpersandToken(node);
		} else if (isKind(node, "BarToken")) {
			visitBarToken(node);
		} else if (isKind(node, "CaretToken")) {
			visitCaretToken(node);
		} else if (isKind(node, "ExclamationToken")) {
			visitExclamationToken(node);
		} else if (isKind(node, "TildeToken")) {
			visitTildeToken(node);
		} else if (isKind(node, "AmpersandAmpersandToken")) {
			visitAmpersandAmpersandToken(node);
		} else if (isKind(node, "BarBarToken")) {
			visitBarBarToken(node);
		} else if (isKind(node, "QuestionToken")) {
			visitQuestionToken(node);
		} else if (isKind(node, "ColonToken")) {
			visitColonToken(node);
		} else if (isKind(node, "AtToken")) {
			visitAtToken(node);
		} else if (isKind(node, "QuestionQuestionToken")) {
			visitQuestionQuestionToken(node);
		} else if (isKind(node, "BacktickToken")) {
			visitBacktickToken(node);
		} else if (isKind(node, "EqualsToken")) {
			visitEqualsToken(node);
		} else if (isKind(node, "PlusEqualsToken")) {
			visitPlusEqualsToken(node);
		} else if (isKind(node, "MinusEqualsToken")) {
			visitMinusEqualsToken(node);
		} else if (isKind(node, "AsteriskEqualsToken")) {
			visitAsteriskEqualsToken(node);
		} else if (isKind(node, "AsteriskAsteriskEqualsToken")) {
			visitAsteriskAsteriskEqualsToken(node);
		} else if (isKind(node, "SlashEqualsToken")) {
			visitSlashEqualsToken(node);
		} else if (isKind(node, "PercentEqualsToken")) {
			visitPercentEqualsToken(node);
		} else if (isKind(node, "LessThanLessThanEqualsToken")) {
			visitLessThanLessThanEqualsToken(node);
		} else if (isKind(node, "GreaterThanGreaterThanEqualsToken")) {
			visitGreaterThanGreaterThanEqualsToken(node);
		} else if (isKind(node, "GreaterThanGreaterThanGreaterThanEqualsToken")) {
			visitGreaterThanGreaterThanGreaterThanEqualsToken(node);
		} else if (isKind(node, "AmpersandEqualsToken")) {
			visitAmpersandEqualsToken(node);
		} else if (isKind(node, "BarEqualsToken")) {
			visitBarEqualsToken(node);
		} else if (isKind(node, "BarBarEqualsToken")) {
			visitBarBarEqualsToken(node);
		} else if (isKind(node, "AmpersandAmpersandEqualsToken")) {
			visitAmpersandAmpersandEqualsToken(node);
		} else if (isKind(node, "QuestionQuestionEqualsToken")) {
			visitQuestionQuestionEqualsToken(node);
		} else if (isKind(node, "CaretEqualsToken")) {
			visitCaretEqualsToken(node);
		} else if (isKind(node, "Identifier")) {
			visitIdentifier(node);
		} else if (isKind(node, "PrivateIdentifier")) {
			visitPrivateIdentifier(node);
		} else if (isKind(node, "BreakKeyword")) {
			visitBreakKeyword(node);
		} else if (isKind(node, "CaseKeyword")) {
			visitCaseKeyword(node);
		} else if (isKind(node, "CatchKeyword")) {
			visitCatchKeyword(node);
		} else if (isKind(node, "ClassKeyword")) {
			visitClassKeyword(node);
		} else if (isKind(node, "ConstKeyword")) {
			visitConstKeyword(node);
		} else if (isKind(node, "ContinueKeyword")) {
			visitContinueKeyword(node);
		} else if (isKind(node, "DebuggerKeyword")) {
			visitDebuggerKeyword(node);
		} else if (isKind(node, "DefaultKeyword")) {
			visitDefaultKeyword(node);
		} else if (isKind(node, "DeleteKeyword")) {
			visitDeleteKeyword(node);
		} else if (isKind(node, "DoKeyword")) {
			visitDoKeyword(node);
		} else if (isKind(node, "ElseKeyword")) {
			visitElseKeyword(node);
		} else if (isKind(node, "EnumKeyword")) {
			visitEnumKeyword(node);
		} else if (isKind(node, "ExportKeyword")) {
			visitExportKeyword(node);
		} else if (isKind(node, "ExtendsKeyword")) {
			visitExtendsKeyword(node);
		} else if (isKind(node, "FalseKeyword")) {
			visitFalseKeyword(node);
		} else if (isKind(node, "FinallyKeyword")) {
			visitFinallyKeyword(node);
		} else if (isKind(node, "ForKeyword")) {
			visitForKeyword(node);
		} else if (isKind(node, "FunctionKeyword")) {
			visitFunctionKeyword(node);
		} else if (isKind(node, "IfKeyword")) {
			visitIfKeyword(node);
		} else if (isKind(node, "ImportKeyword")) {
			visitImportKeyword(node);
		} else if (isKind(node, "InKeyword")) {
			visitInKeyword(node);
		} else if (isKind(node, "InstanceOfKeyword")) {
			visitInstanceOfKeyword(node);
		} else if (isKind(node, "NewKeyword")) {
			visitNewKeyword(node);
		} else if (isKind(node, "NullKeyword")) {
			visitNullKeyword(node);
		} else if (isKind(node, "ReturnKeyword")) {
			visitReturnKeyword(node);
		} else if (isKind(node, "SuperKeyword")) {
			visitSuperKeyword(node);
		} else if (isKind(node, "SwitchKeyword")) {
			visitSwitchKeyword(node);
		} else if (isKind(node, "ThisKeyword")) {
			visitThisKeyword(node);
		} else if (isKind(node, "ThrowKeyword")) {
			visitThrowKeyword(node);
		} else if (isKind(node, "TrueKeyword")) {
			visitTrueKeyword(node);
		} else if (isKind(node, "TryKeyword")) {
			visitTryKeyword(node);
		} else if (isKind(node, "TypeOfKeyword")) {
			visitTypeOfKeyword(node);
		} else if (isKind(node, "VarKeyword")) {
			visitVarKeyword(node);
		} else if (isKind(node, "VoidKeyword")) {
			visitVoidKeyword(node);
		} else if (isKind(node, "WhileKeyword")) {
			visitWhileKeyword(node);
		} else if (isKind(node, "WithKeyword")) {
			visitWithKeyword(node);
		} else if (isKind(node, "ImplementsKeyword")) {
			visitImplementsKeyword(node);
		} else if (isKind(node, "InterfaceKeyword")) {
			visitInterfaceKeyword(node);
		} else if (isKind(node, "LetKeyword")) {
			visitLetKeyword(node);
		} else if (isKind(node, "PackageKeyword")) {
			visitPackageKeyword(node);
		} else if (isKind(node, "PrivateKeyword")) {
			visitPrivateKeyword(node);
		} else if (isKind(node, "ProtectedKeyword")) {
			visitProtectedKeyword(node);
		} else if (isKind(node, "PublicKeyword")) {
			visitPublicKeyword(node);
		} else if (isKind(node, "StaticKeyword")) {
			visitStaticKeyword(node);
		} else if (isKind(node, "YieldKeyword")) {
			visitYieldKeyword(node);
		} else if (isKind(node, "AbstractKeyword")) {
			visitAbstractKeyword(node);
		} else if (isKind(node, "AsKeyword")) {
			visitAsKeyword(node);
		} else if (isKind(node, "AssertsKeyword")) {
			visitAssertsKeyword(node);
		} else if (isKind(node, "AnyKeyword")) {
			visitAnyKeyword(node);
		} else if (isKind(node, "AsyncKeyword")) {
			visitAsyncKeyword(node);
		} else if (isKind(node, "AwaitKeyword")) {
			visitAwaitKeyword(node);
		} else if (isKind(node, "BooleanKeyword")) {
			visitBooleanKeyword(node);
		} else if (isKind(node, "ConstructorKeyword")) {
			visitConstructorKeyword(node);
		} else if (isKind(node, "DeclareKeyword")) {
			visitDeclareKeyword(node);
		} else if (isKind(node, "GetKeyword")) {
			visitGetKeyword(node);
		} else if (isKind(node, "InferKeyword")) {
			visitInferKeyword(node);
		} else if (isKind(node, "IsKeyword")) {
			visitIsKeyword(node);
		} else if (isKind(node, "KeyOfKeyword")) {
			visitKeyOfKeyword(node);
		} else if (isKind(node, "ModuleKeyword")) {
			visitModuleKeyword(node);
		} else if (isKind(node, "NamespaceKeyword")) {
			visitNamespaceKeyword(node);
		} else if (isKind(node, "NeverKeyword")) {
			visitNeverKeyword(node);
		} else if (isKind(node, "ReadonlyKeyword")) {
			visitReadonlyKeyword(node);
		} else if (isKind(node, "RequireKeyword")) {
			visitRequireKeyword(node);
		} else if (isKind(node, "NumberKeyword")) {
			visitNumberKeyword(node);
		} else if (isKind(node, "ObjectKeyword")) {
			visitObjectKeyword(node);
		} else if (isKind(node, "SetKeyword")) {
			visitSetKeyword(node);
		} else if (isKind(node, "StringKeyword")) {
			visitStringKeyword(node);
		} else if (isKind(node, "SymbolKeyword")) {
			visitSymbolKeyword(node);
		} else if (isKind(node, "TypeKeyword")) {
			visitTypeKeyword(node);
		} else if (isKind(node, "UndefinedKeyword")) {
			visitUndefinedKeyword(node);
		} else if (isKind(node, "UniqueKeyword")) {
			visitUniqueKeyword(node);
		} else if (isKind(node, "UnknownKeyword")) {
			visitUnknownKeyword(node);
		} else if (isKind(node, "FromKeyword")) {
			visitFromKeyword(node);
		} else if (isKind(node, "GlobalKeyword")) {
			visitGlobalKeyword(node);
		} else if (isKind(node, "BigIntKeyword")) {
			visitBigIntKeyword(node);
		} else if (isKind(node, "OfKeyword")) {
			visitOfKeyword(node);
		} else if (isKind(node, "QualifiedName")) {
			visitQualifiedName(node);
		} else if (isKind(node, "ComputedPropertyName")) {
			visitComputedPropertyName(node);
		} else if (isKind(node, "TypeParameter")) {
			visitTypeParameter(node);
		} else if (isKind(node, "Parameter")) {
			visitParameter(node);
		} else if (isKind(node, "Decorator")) {
			visitDecorator(node);
		} else if (isKind(node, "PropertySignature")) {
			visitPropertySignature(node);
		} else if (isKind(node, "PropertyDeclaration")) {
			visitPropertyDeclaration(node);
		} else if (isKind(node, "MethodSignature")) {
			visitMethodSignature(node);
		} else if (isKind(node, "MethodDeclaration")) {
			visitMethodDeclaration(node);
		} else if (isKind(node, "Constructor")) {
			visitConstructor(node);
		} else if (isKind(node, "GetAccessor")) {
			visitGetAccessor(node);
		} else if (isKind(node, "SetAccessor")) {
			visitSetAccessor(node);
		} else if (isKind(node, "CallSignature")) {
			visitCallSignature(node);
		} else if (isKind(node, "ConstructSignature")) {
			visitConstructSignature(node);
		} else if (isKind(node, "IndexSignature")) {
			visitIndexSignature(node);
		} else if (isKind(node, "TypePredicate")) {
			visitTypePredicate(node);
		} else if (isKind(node, "TypeReference")) {
			visitTypeReference(node);
		} else if (isKind(node, "FunctionType")) {
			visitFunctionType(node);
		} else if (isKind(node, "ConstructorType")) {
			visitConstructorType(node);
		} else if (isKind(node, "TypeQuery")) {
			visitTypeQuery(node);
		} else if (isKind(node, "TypeLiteral")) {
			visitTypeLiteral(node);
		} else if (isKind(node, "ArrayType")) {
			visitArrayType(node);
		} else if (isKind(node, "TupleType")) {
			visitTupleType(node);
		} else if (isKind(node, "OptionalType")) {
			visitOptionalType(node);
		} else if (isKind(node, "RestType")) {
			visitRestType(node);
		} else if (isKind(node, "UnionType")) {
			visitUnionType(node);
		} else if (isKind(node, "IntersectionType")) {
			visitIntersectionType(node);
		} else if (isKind(node, "ConditionalType")) {
			visitConditionalType(node);
		} else if (isKind(node, "InferType")) {
			visitInferType(node);
		} else if (isKind(node, "ParenthesizedType")) {
			visitParenthesizedType(node);
		} else if (isKind(node, "ThisType")) {
			visitThisType(node);
		} else if (isKind(node, "TypeOperator")) {
			visitTypeOperator(node);
		} else if (isKind(node, "IndexedAccessType")) {
			visitIndexedAccessType(node);
		} else if (isKind(node, "MappedType")) {
			visitMappedType(node);
		} else if (isKind(node, "LiteralType")) {
			visitLiteralType(node);
		} else if (isKind(node, "NamedTupleMember")) {
			visitNamedTupleMember(node);
		} else if (isKind(node, "ImportType")) {
			visitImportType(node);
		} else if (isKind(node, "ObjectBindingPattern")) {
			visitObjectBindingPattern(node);
		} else if (isKind(node, "ArrayBindingPattern")) {
			visitArrayBindingPattern(node);
		} else if (isKind(node, "BindingElement")) {
			visitBindingElement(node);
		} else if (isKind(node, "ArrayLiteralExpression")) {
			visitArrayLiteralExpression(node);
		} else if (isKind(node, "ObjectLiteralExpression")) {
			visitObjectLiteralExpression(node);
		} else if (isKind(node, "PropertyAccessExpression")) {
			visitPropertyAccessExpression(node);
		} else if (isKind(node, "ElementAccessExpression")) {
			visitElementAccessExpression(node);
		} else if (isKind(node, "CallExpression")) {
			visitCallExpression(node);
		} else if (isKind(node, "NewExpression")) {
			visitNewExpression(node);
		} else if (isKind(node, "TaggedTemplateExpression")) {
			visitTaggedTemplateExpression(node);
		} else if (isKind(node, "TypeAssertionExpression")) {
			visitTypeAssertionExpression(node);
		} else if (isKind(node, "ParenthesizedExpression")) {
			visitParenthesizedExpression(node);
		} else if (isKind(node, "FunctionExpression")) {
			visitFunctionExpression(node);
		} else if (isKind(node, "ArrowFunction")) {
			visitArrowFunction(node);
		} else if (isKind(node, "DeleteExpression")) {
			visitDeleteExpression(node);
		} else if (isKind(node, "TypeOfExpression")) {
			visitTypeOfExpression(node);
		} else if (isKind(node, "VoidExpression")) {
			visitVoidExpression(node);
		} else if (isKind(node, "AwaitExpression")) {
			visitAwaitExpression(node);
		} else if (isKind(node, "PrefixUnaryExpression")) {
			visitPrefixUnaryExpression(node);
		} else if (isKind(node, "PostfixUnaryExpression")) {
			visitPostfixUnaryExpression(node);
		} else if (isKind(node, "BinaryExpression")) {
			visitBinaryExpression(node);
		} else if (isKind(node, "ConditionalExpression")) {
			visitConditionalExpression(node);
		} else if (isKind(node, "TemplateExpression")) {
			visitTemplateExpression(node);
		} else if (isKind(node, "YieldExpression")) {
			visitYieldExpression(node);
		} else if (isKind(node, "SpreadElement")) {
			visitSpreadElement(node);
		} else if (isKind(node, "ClassExpression")) {
			visitClassExpression(node);
		} else if (isKind(node, "OmittedExpression")) {
			visitOmittedExpression(node);
		} else if (isKind(node, "ExpressionWithTypeArguments")) {
			visitExpressionWithTypeArguments(node);
		} else if (isKind(node, "AsExpression")) {
			visitAsExpression(node);
		} else if (isKind(node, "NonNullExpression")) {
			visitNonNullExpression(node);
		} else if (isKind(node, "MetaProperty")) {
			visitMetaProperty(node);
		} else if (isKind(node, "SyntheticExpression")) {
			visitSyntheticExpression(node);
		} else if (isKind(node, "TemplateSpan")) {
			visitTemplateSpan(node);
		} else if (isKind(node, "SemicolonClassElement")) {
			visitSemicolonClassElement(node);
		} else if (isKind(node, "Block")) {
			visitBlock(node);
		} else if (isKind(node, "EmptyStatement")) {
			visitEmptyStatement(node);
		} else if (isKind(node, "VariableStatement")) {
			visitVariableStatement(node);
		} else if (isKind(node, "ExpressionStatement")) {
			visitExpressionStatement(node);
		} else if (isKind(node, "IfStatement")) {
			visitIfStatement(node);
		} else if (isKind(node, "DoStatement")) {
			visitDoStatement(node);
		} else if (isKind(node, "WhileStatement")) {
			visitWhileStatement(node);
		} else if (isKind(node, "ForStatement")) {
			visitForStatement(node);
		} else if (isKind(node, "ForInStatement")) {
			visitForInStatement(node);
		} else if (isKind(node, "ForOfStatement")) {
			visitForOfStatement(node);
		} else if (isKind(node, "ContinueStatement")) {
			visitContinueStatement(node);
		} else if (isKind(node, "BreakStatement")) {
			visitBreakStatement(node);
		} else if (isKind(node, "ReturnStatement")) {
			visitReturnStatement(node);
		} else if (isKind(node, "WithStatement")) {
			visitWithStatement(node);
		} else if (isKind(node, "SwitchStatement")) {
			visitSwitchStatement(node);
		} else if (isKind(node, "LabeledStatement")) {
			visitLabeledStatement(node);
		} else if (isKind(node, "ThrowStatement")) {
			visitThrowStatement(node);
		} else if (isKind(node, "TryStatement")) {
			visitTryStatement(node);
		} else if (isKind(node, "DebuggerStatement")) {
			visitDebuggerStatement(node);
		} else if (isKind(node, "VariableDeclaration")) {
			visitVariableDeclaration(node);
		} else if (isKind(node, "VariableDeclarationList")) {
			visitVariableDeclarationList(node);
		} else if (isKind(node, "FunctionDeclaration")) {
			visitFunctionDeclaration(node);
		} else if (isKind(node, "ClassDeclaration")) {
			visitClassDeclaration(node);
		} else if (isKind(node, "InterfaceDeclaration")) {
			visitInterfaceDeclaration(node);
		} else if (isKind(node, "TypeAliasDeclaration")) {
			visitTypeAliasDeclaration(node);
		} else if (isKind(node, "EnumDeclaration")) {
			visitEnumDeclaration(node);
		} else if (isKind(node, "ModuleDeclaration")) {
			visitModuleDeclaration(node);
		} else if (isKind(node, "ModuleBlock")) {
			visitModuleBlock(node);
		} else if (isKind(node, "CaseBlock")) {
			visitCaseBlock(node);
		} else if (isKind(node, "NamespaceExportDeclaration")) {
			visitNamespaceExportDeclaration(node);
		} else if (isKind(node, "ImportEqualsDeclaration")) {
			visitImportEqualsDeclaration(node);
		} else if (isKind(node, "ImportDeclaration")) {
			visitImportDeclaration(node);
		} else if (isKind(node, "ImportClause")) {
			visitImportClause(node);
		} else if (isKind(node, "NamespaceImport")) {
			visitNamespaceImport(node);
		} else if (isKind(node, "NamedImports")) {
			visitNamedImports(node);
		} else if (isKind(node, "ImportSpecifier")) {
			visitImportSpecifier(node);
		} else if (isKind(node, "ExportAssignment")) {
			visitExportAssignment(node);
		} else if (isKind(node, "ExportDeclaration")) {
			visitExportDeclaration(node);
		} else if (isKind(node, "NamedExports")) {
			visitNamedExports(node);
		} else if (isKind(node, "NamespaceExport")) {
			visitNamespaceExport(node);
		} else if (isKind(node, "ExportSpecifier")) {
			visitExportSpecifier(node);
		} else if (isKind(node, "MissingDeclaration")) {
			visitMissingDeclaration(node);
		} else if (isKind(node, "ExternalModuleReference")) {
			visitExternalModuleReference(node);
		} else if (isKind(node, "JsxElement")) {
			visitJsxElement(node);
		} else if (isKind(node, "JsxSelfClosingElement")) {
			visitJsxSelfClosingElement(node);
		} else if (isKind(node, "JsxOpeningElement")) {
			visitJsxOpeningElement(node);
		} else if (isKind(node, "JsxClosingElement")) {
			visitJsxClosingElement(node);
		} else if (isKind(node, "JsxFragment")) {
			visitJsxFragment(node);
		} else if (isKind(node, "JsxOpeningFragment")) {
			visitJsxOpeningFragment(node);
		} else if (isKind(node, "JsxClosingFragment")) {
			visitJsxClosingFragment(node);
		} else if (isKind(node, "JsxAttribute")) {
			visitJsxAttribute(node);
		} else if (isKind(node, "JsxAttributes")) {
			visitJsxAttributes(node);
		} else if (isKind(node, "JsxSpreadAttribute")) {
			visitJsxSpreadAttribute(node);
		} else if (isKind(node, "JsxExpression")) {
			visitJsxExpression(node);
		} else if (isKind(node, "CaseClause")) {
			visitCaseClause(node);
		} else if (isKind(node, "DefaultClause")) {
			visitDefaultClause(node);
		} else if (isKind(node, "HeritageClause")) {
			visitHeritageClause(node);
		} else if (isKind(node, "CatchClause")) {
			visitCatchClause(node);
		} else if (isKind(node, "PropertyAssignment")) {
			visitPropertyAssignment(node);
		} else if (isKind(node, "ShorthandPropertyAssignment")) {
			visitShorthandPropertyAssignment(node);
		} else if (isKind(node, "SpreadAssignment")) {
			visitSpreadAssignment(node);
		} else if (isKind(node, "EnumMember")) {
			visitEnumMember(node);
		} else if (isKind(node, "UnparsedPrologue")) {
			visitUnparsedPrologue(node);
		} else if (isKind(node, "UnparsedPrepend")) {
			visitUnparsedPrepend(node);
		} else if (isKind(node, "UnparsedText")) {
			visitUnparsedText(node);
		} else if (isKind(node, "UnparsedInternalText")) {
			visitUnparsedInternalText(node);
		} else if (isKind(node, "UnparsedSyntheticReference")) {
			visitUnparsedSyntheticReference(node);
		} else if (isKind(node, "SourceFile")) {
			visitSourceFile(node);
		} else if (isKind(node, "Bundle")) {
			visitBundle(node);
		} else if (isKind(node, "UnparsedSource")) {
			visitUnparsedSource(node);
		} else if (isKind(node, "InputFiles")) {
			visitInputFiles(node);
		} else if (isKind(node, "JSDocTypeExpression")) {
			visitJSDocTypeExpression(node);
		} else if (isKind(node, "JSDocAllType")) {
			visitJSDocAllType(node);
		} else if (isKind(node, "JSDocUnknownType")) {
			visitJSDocUnknownType(node);
		} else if (isKind(node, "JSDocNullableType")) {
			visitJSDocNullableType(node);
		} else if (isKind(node, "JSDocNonNullableType")) {
			visitJSDocNonNullableType(node);
		} else if (isKind(node, "JSDocOptionalType")) {
			visitJSDocOptionalType(node);
		} else if (isKind(node, "JSDocFunctionType")) {
			visitJSDocFunctionType(node);
		} else if (isKind(node, "JSDocVariadicType")) {
			visitJSDocVariadicType(node);
		} else if (isKind(node, "JSDocNamepathType")) {
			visitJSDocNamepathType(node);
		} else if (isKind(node, "JSDocComment")) {
			visitJSDocComment(node);
		} else if (isKind(node, "JSDocTypeLiteral")) {
			visitJSDocTypeLiteral(node);
		} else if (isKind(node, "JSDocSignature")) {
			visitJSDocSignature(node);
		} else if (isKind(node, "JSDocTag")) {
			visitJSDocTag(node);
		} else if (isKind(node, "JSDocAugmentsTag")) {
			visitJSDocAugmentsTag(node);
		} else if (isKind(node, "JSDocImplementsTag")) {
			visitJSDocImplementsTag(node);
		} else if (isKind(node, "JSDocAuthorTag")) {
			visitJSDocAuthorTag(node);
		} else if (isKind(node, "JSDocDeprecatedTag")) {
			visitJSDocDeprecatedTag(node);
		} else if (isKind(node, "JSDocClassTag")) {
			visitJSDocClassTag(node);
		} else if (isKind(node, "JSDocPublicTag")) {
			visitJSDocPublicTag(node);
		} else if (isKind(node, "JSDocPrivateTag")) {
			visitJSDocPrivateTag(node);
		} else if (isKind(node, "JSDocProtectedTag")) {
			visitJSDocProtectedTag(node);
		} else if (isKind(node, "JSDocReadonlyTag")) {
			visitJSDocReadonlyTag(node);
		} else if (isKind(node, "JSDocCallbackTag")) {
			visitJSDocCallbackTag(node);
		} else if (isKind(node, "JSDocEnumTag")) {
			visitJSDocEnumTag(node);
		} else if (isKind(node, "JSDocParameterTag")) {
			visitJSDocParameterTag(node);
		} else if (isKind(node, "JSDocReturnTag")) {
			visitJSDocReturnTag(node);
		} else if (isKind(node, "JSDocThisTag")) {
			visitJSDocThisTag(node);
		} else if (isKind(node, "JSDocTypeTag")) {
			visitJSDocTypeTag(node);
		} else if (isKind(node, "JSDocTemplateTag")) {
			visitJSDocTemplateTag(node);
		} else if (isKind(node, "JSDocTypedefTag")) {
			visitJSDocTypedefTag(node);
		} else if (isKind(node, "JSDocPropertyTag")) {
			visitJSDocPropertyTag(node);
		} else if (isKind(node, "SyntaxList")) {
			visitSyntaxList(node);
		} else if (isKind(node, "NotEmittedStatement")) {
			visitNotEmittedStatement(node);
		} else if (isKind(node, "PartiallyEmittedExpression")) {
			visitPartiallyEmittedExpression(node);
		} else if (isKind(node, "CommaListExpression")) {
			visitCommaListExpression(node);
		} else if (isKind(node, "MergeDeclarationMarker")) {
			visitMergeDeclarationMarker(node);
		} else if (isKind(node, "EndOfDeclarationMarker")) {
			visitEndOfDeclarationMarker(node);
		} else if (isKind(node, "SyntheticReferenceExpression")) {
			visitSyntheticReferenceExpression(node);
		} else if (isKind(node, "Count")) {
			visitCount(node);
//		} else if (isKind(node, "FirstAssignment")) {
//			visitFirstAssignment(node);
//		} else if (isKind(node, "LastAssignment")) {
//			visitLastAssignment(node);
//		} else if (isKind(node, "FirstCompoundAssignment")) {
//			visitFirstCompoundAssignment(node);
//		} else if (isKind(node, "LastCompoundAssignment")) {
//			visitLastCompoundAssignment(node);
//		} else if (isKind(node, "FirstReservedWord")) {
//			visitFirstReservedWord(node);
//		} else if (isKind(node, "LastReservedWord")) {
//			visitLastReservedWord(node);
//		} else if (isKind(node, "FirstKeyword")) {
//			visitFirstKeyword(node);
//		} else if (isKind(node, "LastKeyword")) {
//			visitLastKeyword(node);
//		} else if (isKind(node, "FirstFutureReservedWord")) {
//			visitFirstFutureReservedWord(node);
//		} else if (isKind(node, "LastFutureReservedWord")) {
//			visitLastFutureReservedWord(node);
//		} else if (isKind(node, "FirstTypeNode")) {
//			visitFirstTypeNode(node);
//		} else if (isKind(node, "LastTypeNode")) {
//			visitLastTypeNode(node);
//		} else if (isKind(node, "FirstPunctuation")) {
//			visitFirstPunctuation(node);
//		} else if (isKind(node, "LastPunctuation")) {
//			visitLastPunctuation(node);
//		} else if (isKind(node, "FirstToken")) {
//			visitFirstToken(node);
//		} else if (isKind(node, "LastToken")) {
//			visitLastToken(node);
//		} else if (isKind(node, "FirstTriviaToken")) {
//			visitFirstTriviaToken(node);
//		} else if (isKind(node, "LastTriviaToken")) {
//			visitLastTriviaToken(node);
//		} else if (isKind(node, "FirstLiteralToken")) {
//			visitFirstLiteralToken(node);
//		} else if (isKind(node, "LastLiteralToken")) {
//			visitLastLiteralToken(node);
//		} else if (isKind(node, "FirstTemplateToken")) {
//			visitFirstTemplateToken(node);
//		} else if (isKind(node, "LastTemplateToken")) {
//			visitLastTemplateToken(node);
//		} else if (isKind(node, "FirstBinaryOperator")) {
//			visitFirstBinaryOperator(node);
//		} else if (isKind(node, "LastBinaryOperator")) {
//			visitLastBinaryOperator(node);
//		} else if (isKind(node, "FirstStatement")) {
//			visitFirstStatement(node);
//		} else if (isKind(node, "LastStatement")) {
//			visitLastStatement(node);
//		} else if (isKind(node, "FirstNode")) {
//			visitFirstNode(node);
//		} else if (isKind(node, "FirstJSDocNode")) {
//			visitFirstJSDocNode(node);
//		} else if (isKind(node, "LastJSDocNode")) {
//			visitLastJSDocNode(node);
//		} else if (isKind(node, "FirstJSDocTagNode")) {
//			visitFirstJSDocTagNode(node);
//		} else if (isKind(node, "LastJSDocTagNode")) {
//			visitLastJSDocTagNode(node);
		} else {
			visitChildren(node);
		}
	}

	public boolean isKind(V8Object node, String kind) {
		if (!syntaxKindCache.containsKey(kind) && syntaxKind.contains(kind)) {
			syntaxKindCache.put(kind, syntaxKind.getInteger(kind));
		}
		return node.contains("kind") && syntaxKindCache.containsKey(kind) &&
				node.getInteger("kind") == syntaxKindCache.get(kind);
	}

	protected void visitChildren(V8Object node) {
		V8Function callback = new V8Function(ts.getRuntime(), (receiver, parameters) -> {
			final V8Object child = parameters.getObject(0);
			visit(child);
			child.release();
			return null;
		});
		ts.executeJSFunction("forEachChild", node, callback);
		callback.release();
	}

	protected void visitUnknown(V8Object unknown) {
		visitChildren(unknown);
	}

	protected void visitEndOfFileToken(V8Object endOfFileToken) {
		visitChildren(endOfFileToken);
	}

	protected void visitSingleLineCommentTrivia(V8Object singleLineCommentTrivia) {
		visitChildren(singleLineCommentTrivia);
	}

	protected void visitMultiLineCommentTrivia(V8Object multiLineCommentTrivia) {
		visitChildren(multiLineCommentTrivia);
	}

	protected void visitNewLineTrivia(V8Object newLineTrivia) {
		visitChildren(newLineTrivia);
	}

	protected void visitWhitespaceTrivia(V8Object whitespaceTrivia) {
		visitChildren(whitespaceTrivia);
	}

	protected void visitShebangTrivia(V8Object shebangTrivia) {
		visitChildren(shebangTrivia);
	}

	protected void visitConflictMarkerTrivia(V8Object conflictMarkerTrivia) {
		visitChildren(conflictMarkerTrivia);
	}

	protected void visitNumericLiteral(V8Object numericLiteral) {
		visitChildren(numericLiteral);
	}

	protected void visitBigIntLiteral(V8Object bigIntLiteral) {
		visitChildren(bigIntLiteral);
	}

	protected void visitStringLiteral(V8Object stringLiteral) {
		visitChildren(stringLiteral);
	}

	protected void visitJsxText(V8Object jsxText) {
		visitChildren(jsxText);
	}

	protected void visitJsxTextAllWhiteSpaces(V8Object jsxTextAllWhiteSpaces) {
		visitChildren(jsxTextAllWhiteSpaces);
	}

	protected void visitRegularExpressionLiteral(V8Object regularExpressionLiteral) {
		visitChildren(regularExpressionLiteral);
	}

	protected void visitNoSubstitutionTemplateLiteral(V8Object noSubstitutionTemplateLiteral) {
		visitChildren(noSubstitutionTemplateLiteral);
	}

	protected void visitTemplateHead(V8Object templateHead) {
		visitChildren(templateHead);
	}

	protected void visitTemplateMiddle(V8Object templateMiddle) {
		visitChildren(templateMiddle);
	}

	protected void visitTemplateTail(V8Object templateTail) {
		visitChildren(templateTail);
	}

	protected void visitOpenBraceToken(V8Object openBraceToken) {
		visitChildren(openBraceToken);
	}

	protected void visitCloseBraceToken(V8Object closeBraceToken) {
		visitChildren(closeBraceToken);
	}

	protected void visitOpenParenToken(V8Object openParenToken) {
		visitChildren(openParenToken);
	}

	protected void visitCloseParenToken(V8Object closeParenToken) {
		visitChildren(closeParenToken);
	}

	protected void visitOpenBracketToken(V8Object openBracketToken) {
		visitChildren(openBracketToken);
	}

	protected void visitCloseBracketToken(V8Object closeBracketToken) {
		visitChildren(closeBracketToken);
	}

	protected void visitDotToken(V8Object dotToken) {
		visitChildren(dotToken);
	}

	protected void visitDotDotDotToken(V8Object dotDotDotToken) {
		visitChildren(dotDotDotToken);
	}

	protected void visitSemicolonToken(V8Object semicolonToken) {
		visitChildren(semicolonToken);
	}

	protected void visitCommaToken(V8Object commaToken) {
		visitChildren(commaToken);
	}

	protected void visitQuestionDotToken(V8Object questionDotToken) {
		visitChildren(questionDotToken);
	}

	protected void visitLessThanToken(V8Object lessThanToken) {
		visitChildren(lessThanToken);
	}

	protected void visitLessThanSlashToken(V8Object lessThanSlashToken) {
		visitChildren(lessThanSlashToken);
	}

	protected void visitGreaterThanToken(V8Object greaterThanToken) {
		visitChildren(greaterThanToken);
	}

	protected void visitLessThanEqualsToken(V8Object lessThanEqualsToken) {
		visitChildren(lessThanEqualsToken);
	}

	protected void visitGreaterThanEqualsToken(V8Object greaterThanEqualsToken) {
		visitChildren(greaterThanEqualsToken);
	}

	protected void visitEqualsEqualsToken(V8Object equalsEqualsToken) {
		visitChildren(equalsEqualsToken);
	}

	protected void visitExclamationEqualsToken(V8Object exclamationEqualsToken) {
		visitChildren(exclamationEqualsToken);
	}

	protected void visitEqualsEqualsEqualsToken(V8Object equalsEqualsEqualsToken) {
		visitChildren(equalsEqualsEqualsToken);
	}

	protected void visitExclamationEqualsEqualsToken(V8Object exclamationEqualsEqualsToken) {
		visitChildren(exclamationEqualsEqualsToken);
	}

	protected void visitEqualsGreaterThanToken(V8Object equalsGreaterThanToken) {
		visitChildren(equalsGreaterThanToken);
	}

	protected void visitPlusToken(V8Object plusToken) {
		visitChildren(plusToken);
	}

	protected void visitMinusToken(V8Object minusToken) {
		visitChildren(minusToken);
	}

	protected void visitAsteriskToken(V8Object asteriskToken) {
		visitChildren(asteriskToken);
	}

	protected void visitAsteriskAsteriskToken(V8Object asteriskAsteriskToken) {
		visitChildren(asteriskAsteriskToken);
	}

	protected void visitSlashToken(V8Object slashToken) {
		visitChildren(slashToken);
	}

	protected void visitPercentToken(V8Object percentToken) {
		visitChildren(percentToken);
	}

	protected void visitPlusPlusToken(V8Object plusPlusToken) {
		visitChildren(plusPlusToken);
	}

	protected void visitMinusMinusToken(V8Object minusMinusToken) {
		visitChildren(minusMinusToken);
	}

	protected void visitLessThanLessThanToken(V8Object lessThanLessThanToken) {
		visitChildren(lessThanLessThanToken);
	}

	protected void visitGreaterThanGreaterThanToken(V8Object greaterThanGreaterThanToken) {
		visitChildren(greaterThanGreaterThanToken);
	}

	protected void visitGreaterThanGreaterThanGreaterThanToken(V8Object greaterThanGreaterThanGreaterThanToken) {
		visitChildren(greaterThanGreaterThanGreaterThanToken);
	}

	protected void visitAmpersandToken(V8Object ampersandToken) {
		visitChildren(ampersandToken);
	}

	protected void visitBarToken(V8Object barToken) {
		visitChildren(barToken);
	}

	protected void visitCaretToken(V8Object caretToken) {
		visitChildren(caretToken);
	}

	protected void visitExclamationToken(V8Object exclamationToken) {
		visitChildren(exclamationToken);
	}

	protected void visitTildeToken(V8Object tildeToken) {
		visitChildren(tildeToken);
	}

	protected void visitAmpersandAmpersandToken(V8Object ampersandAmpersandToken) {
		visitChildren(ampersandAmpersandToken);
	}

	protected void visitBarBarToken(V8Object barBarToken) {
		visitChildren(barBarToken);
	}

	protected void visitQuestionToken(V8Object questionToken) {
		visitChildren(questionToken);
	}

	protected void visitColonToken(V8Object colonToken) {
		visitChildren(colonToken);
	}

	protected void visitAtToken(V8Object atToken) {
		visitChildren(atToken);
	}

	protected void visitQuestionQuestionToken(V8Object questionQuestionToken) {
		visitChildren(questionQuestionToken);
	}

	protected void visitBacktickToken(V8Object backtickToken) {
		visitChildren(backtickToken);
	}

	protected void visitEqualsToken(V8Object equalsToken) {
		visitChildren(equalsToken);
	}

	protected void visitPlusEqualsToken(V8Object plusEqualsToken) {
		visitChildren(plusEqualsToken);
	}

	protected void visitMinusEqualsToken(V8Object minusEqualsToken) {
		visitChildren(minusEqualsToken);
	}

	protected void visitAsteriskEqualsToken(V8Object asteriskEqualsToken) {
		visitChildren(asteriskEqualsToken);
	}

	protected void visitAsteriskAsteriskEqualsToken(V8Object asteriskAsteriskEqualsToken) {
		visitChildren(asteriskAsteriskEqualsToken);
	}

	protected void visitSlashEqualsToken(V8Object slashEqualsToken) {
		visitChildren(slashEqualsToken);
	}

	protected void visitPercentEqualsToken(V8Object percentEqualsToken) {
		visitChildren(percentEqualsToken);
	}

	protected void visitLessThanLessThanEqualsToken(V8Object lessThanLessThanEqualsToken) {
		visitChildren(lessThanLessThanEqualsToken);
	}

	protected void visitGreaterThanGreaterThanEqualsToken(V8Object greaterThanGreaterThanEqualsToken) {
		visitChildren(greaterThanGreaterThanEqualsToken);
	}

	protected void visitGreaterThanGreaterThanGreaterThanEqualsToken(V8Object greaterThanGreaterThanGreaterThanEqualsToken) {
		visitChildren(greaterThanGreaterThanGreaterThanEqualsToken);
	}

	protected void visitAmpersandEqualsToken(V8Object ampersandEqualsToken) {
		visitChildren(ampersandEqualsToken);
	}

	protected void visitBarEqualsToken(V8Object barEqualsToken) {
		visitChildren(barEqualsToken);
	}

	protected void visitBarBarEqualsToken(V8Object barBarEqualsToken) {
		visitChildren(barBarEqualsToken);
	}

	protected void visitAmpersandAmpersandEqualsToken(V8Object ampersandAmpersandEqualsToken) {
		visitChildren(ampersandAmpersandEqualsToken);
	}

	protected void visitQuestionQuestionEqualsToken(V8Object questionQuestionEqualsToken) {
		visitChildren(questionQuestionEqualsToken);
	}

	protected void visitCaretEqualsToken(V8Object caretEqualsToken) {
		visitChildren(caretEqualsToken);
	}

	protected void visitIdentifier(V8Object identifier) {
		visitChildren(identifier);
	}

	protected void visitPrivateIdentifier(V8Object privateIdentifier) {
		visitChildren(privateIdentifier);
	}

	protected void visitBreakKeyword(V8Object breakKeyword) {
		visitChildren(breakKeyword);
	}

	protected void visitCaseKeyword(V8Object caseKeyword) {
		visitChildren(caseKeyword);
	}

	protected void visitCatchKeyword(V8Object catchKeyword) {
		visitChildren(catchKeyword);
	}

	protected void visitClassKeyword(V8Object classKeyword) {
		visitChildren(classKeyword);
	}

	protected void visitConstKeyword(V8Object constKeyword) {
		visitChildren(constKeyword);
	}

	protected void visitContinueKeyword(V8Object continueKeyword) {
		visitChildren(continueKeyword);
	}

	protected void visitDebuggerKeyword(V8Object debuggerKeyword) {
		visitChildren(debuggerKeyword);
	}

	protected void visitDefaultKeyword(V8Object defaultKeyword) {
		visitChildren(defaultKeyword);
	}

	protected void visitDeleteKeyword(V8Object deleteKeyword) {
		visitChildren(deleteKeyword);
	}

	protected void visitDoKeyword(V8Object doKeyword) {
		visitChildren(doKeyword);
	}

	protected void visitElseKeyword(V8Object elseKeyword) {
		visitChildren(elseKeyword);
	}

	protected void visitEnumKeyword(V8Object enumKeyword) {
		visitChildren(enumKeyword);
	}

	protected void visitExportKeyword(V8Object exportKeyword) {
		visitChildren(exportKeyword);
	}

	protected void visitExtendsKeyword(V8Object extendsKeyword) {
		visitChildren(extendsKeyword);
	}

	protected void visitFalseKeyword(V8Object falseKeyword) {
		visitChildren(falseKeyword);
	}

	protected void visitFinallyKeyword(V8Object finallyKeyword) {
		visitChildren(finallyKeyword);
	}

	protected void visitForKeyword(V8Object forKeyword) {
		visitChildren(forKeyword);
	}

	protected void visitFunctionKeyword(V8Object functionKeyword) {
		visitChildren(functionKeyword);
	}

	protected void visitIfKeyword(V8Object ifKeyword) {
		visitChildren(ifKeyword);
	}

	protected void visitImportKeyword(V8Object importKeyword) {
		visitChildren(importKeyword);
	}

	protected void visitInKeyword(V8Object inKeyword) {
		visitChildren(inKeyword);
	}

	protected void visitInstanceOfKeyword(V8Object instanceOfKeyword) {
		visitChildren(instanceOfKeyword);
	}

	protected void visitNewKeyword(V8Object newKeyword) {
		visitChildren(newKeyword);
	}

	protected void visitNullKeyword(V8Object nullKeyword) {
		visitChildren(nullKeyword);
	}

	protected void visitReturnKeyword(V8Object returnKeyword) {
		visitChildren(returnKeyword);
	}

	protected void visitSuperKeyword(V8Object superKeyword) {
		visitChildren(superKeyword);
	}

	protected void visitSwitchKeyword(V8Object switchKeyword) {
		visitChildren(switchKeyword);
	}

	protected void visitThisKeyword(V8Object thisKeyword) {
		visitChildren(thisKeyword);
	}

	protected void visitThrowKeyword(V8Object throwKeyword) {
		visitChildren(throwKeyword);
	}

	protected void visitTrueKeyword(V8Object trueKeyword) {
		visitChildren(trueKeyword);
	}

	protected void visitTryKeyword(V8Object tryKeyword) {
		visitChildren(tryKeyword);
	}

	protected void visitTypeOfKeyword(V8Object typeOfKeyword) {
		visitChildren(typeOfKeyword);
	}

	protected void visitVarKeyword(V8Object varKeyword) {
		visitChildren(varKeyword);
	}

	protected void visitVoidKeyword(V8Object voidKeyword) {
		visitChildren(voidKeyword);
	}

	protected void visitWhileKeyword(V8Object whileKeyword) {
		visitChildren(whileKeyword);
	}

	protected void visitWithKeyword(V8Object withKeyword) {
		visitChildren(withKeyword);
	}

	protected void visitImplementsKeyword(V8Object implementsKeyword) {
		visitChildren(implementsKeyword);
	}

	protected void visitInterfaceKeyword(V8Object interfaceKeyword) {
		visitChildren(interfaceKeyword);
	}

	protected void visitLetKeyword(V8Object letKeyword) {
		visitChildren(letKeyword);
	}

	protected void visitPackageKeyword(V8Object packageKeyword) {
		visitChildren(packageKeyword);
	}

	protected void visitPrivateKeyword(V8Object privateKeyword) {
		visitChildren(privateKeyword);
	}

	protected void visitProtectedKeyword(V8Object protectedKeyword) {
		visitChildren(protectedKeyword);
	}

	protected void visitPublicKeyword(V8Object publicKeyword) {
		visitChildren(publicKeyword);
	}

	protected void visitStaticKeyword(V8Object staticKeyword) {
		visitChildren(staticKeyword);
	}

	protected void visitYieldKeyword(V8Object yieldKeyword) {
		visitChildren(yieldKeyword);
	}

	protected void visitAbstractKeyword(V8Object abstractKeyword) {
		visitChildren(abstractKeyword);
	}

	protected void visitAsKeyword(V8Object asKeyword) {
		visitChildren(asKeyword);
	}

	protected void visitAssertsKeyword(V8Object assertsKeyword) {
		visitChildren(assertsKeyword);
	}

	protected void visitAnyKeyword(V8Object anyKeyword) {
		visitChildren(anyKeyword);
	}

	protected void visitAsyncKeyword(V8Object asyncKeyword) {
		visitChildren(asyncKeyword);
	}

	protected void visitAwaitKeyword(V8Object awaitKeyword) {
		visitChildren(awaitKeyword);
	}

	protected void visitBooleanKeyword(V8Object booleanKeyword) {
		visitChildren(booleanKeyword);
	}

	protected void visitConstructorKeyword(V8Object constructorKeyword) {
		visitChildren(constructorKeyword);
	}

	protected void visitDeclareKeyword(V8Object declareKeyword) {
		visitChildren(declareKeyword);
	}

	protected void visitGetKeyword(V8Object getKeyword) {
		visitChildren(getKeyword);
	}

	protected void visitInferKeyword(V8Object inferKeyword) {
		visitChildren(inferKeyword);
	}

	protected void visitIsKeyword(V8Object isKeyword) {
		visitChildren(isKeyword);
	}

	protected void visitKeyOfKeyword(V8Object keyOfKeyword) {
		visitChildren(keyOfKeyword);
	}

	protected void visitModuleKeyword(V8Object moduleKeyword) {
		visitChildren(moduleKeyword);
	}

	protected void visitNamespaceKeyword(V8Object namespaceKeyword) {
		visitChildren(namespaceKeyword);
	}

	protected void visitNeverKeyword(V8Object neverKeyword) {
		visitChildren(neverKeyword);
	}

	protected void visitReadonlyKeyword(V8Object readonlyKeyword) {
		visitChildren(readonlyKeyword);
	}

	protected void visitRequireKeyword(V8Object requireKeyword) {
		visitChildren(requireKeyword);
	}

	protected void visitNumberKeyword(V8Object numberKeyword) {
		visitChildren(numberKeyword);
	}

	protected void visitObjectKeyword(V8Object objectKeyword) {
		visitChildren(objectKeyword);
	}

	protected void visitSetKeyword(V8Object setKeyword) {
		visitChildren(setKeyword);
	}

	protected void visitStringKeyword(V8Object stringKeyword) {
		visitChildren(stringKeyword);
	}

	protected void visitSymbolKeyword(V8Object symbolKeyword) {
		visitChildren(symbolKeyword);
	}

	protected void visitTypeKeyword(V8Object typeKeyword) {
		visitChildren(typeKeyword);
	}

	protected void visitUndefinedKeyword(V8Object undefinedKeyword) {
		visitChildren(undefinedKeyword);
	}

	protected void visitUniqueKeyword(V8Object uniqueKeyword) {
		visitChildren(uniqueKeyword);
	}

	protected void visitUnknownKeyword(V8Object unknownKeyword) {
		visitChildren(unknownKeyword);
	}

	protected void visitFromKeyword(V8Object fromKeyword) {
		visitChildren(fromKeyword);
	}

	protected void visitGlobalKeyword(V8Object globalKeyword) {
		visitChildren(globalKeyword);
	}

	protected void visitBigIntKeyword(V8Object bigIntKeyword) {
		visitChildren(bigIntKeyword);
	}

	protected void visitOfKeyword(V8Object ofKeyword) {
		visitChildren(ofKeyword);
	}

	protected void visitQualifiedName(V8Object qualifiedName) {
		visitChildren(qualifiedName);
	}

	protected void visitComputedPropertyName(V8Object computedPropertyName) {
		visitChildren(computedPropertyName);
	}

	protected void visitTypeParameter(V8Object typeParameter) {
		visitChildren(typeParameter);
	}

	protected void visitParameter(V8Object parameter) {
		visitChildren(parameter);
	}

	protected void visitDecorator(V8Object decorator) {
		visitChildren(decorator);
	}

	protected void visitPropertySignature(V8Object propertySignature) {
		visitChildren(propertySignature);
	}

	protected void visitPropertyDeclaration(V8Object propertyDeclaration) {
		visitChildren(propertyDeclaration);
	}

	protected void visitMethodSignature(V8Object methodSignature) {
		visitChildren(methodSignature);
	}

	protected void visitMethodDeclaration(V8Object methodDeclaration) {
		visitChildren(methodDeclaration);
	}

	protected void visitConstructor(V8Object constructor) {
		visitChildren(constructor);
	}

	protected void visitGetAccessor(V8Object getAccessor) {
		visitChildren(getAccessor);
	}

	protected void visitSetAccessor(V8Object setAccessor) {
		visitChildren(setAccessor);
	}

	protected void visitCallSignature(V8Object callSignature) {
		visitChildren(callSignature);
	}

	protected void visitConstructSignature(V8Object constructSignature) {
		visitChildren(constructSignature);
	}

	protected void visitIndexSignature(V8Object indexSignature) {
		visitChildren(indexSignature);
	}

	protected void visitTypePredicate(V8Object typePredicate) {
		visitChildren(typePredicate);
	}

	protected void visitTypeReference(V8Object typeReference) {
		visitChildren(typeReference);
	}

	protected void visitFunctionType(V8Object functionType) {
		visitChildren(functionType);
	}

	protected void visitConstructorType(V8Object constructorType) {
		visitChildren(constructorType);
	}

	protected void visitTypeQuery(V8Object typeQuery) {
		visitChildren(typeQuery);
	}

	protected void visitTypeLiteral(V8Object typeLiteral) {
		visitChildren(typeLiteral);
	}

	protected void visitArrayType(V8Object arrayType) {
		visitChildren(arrayType);
	}

	protected void visitTupleType(V8Object tupleType) {
		visitChildren(tupleType);
	}

	protected void visitOptionalType(V8Object optionalType) {
		visitChildren(optionalType);
	}

	protected void visitRestType(V8Object restType) {
		visitChildren(restType);
	}

	protected void visitUnionType(V8Object unionType) {
		visitChildren(unionType);
	}

	protected void visitIntersectionType(V8Object intersectionType) {
		visitChildren(intersectionType);
	}

	protected void visitConditionalType(V8Object conditionalType) {
		visitChildren(conditionalType);
	}

	protected void visitInferType(V8Object inferType) {
		visitChildren(inferType);
	}

	protected void visitParenthesizedType(V8Object parenthesizedType) {
		visitChildren(parenthesizedType);
	}

	protected void visitThisType(V8Object thisType) {
		visitChildren(thisType);
	}

	protected void visitTypeOperator(V8Object typeOperator) {
		visitChildren(typeOperator);
	}

	protected void visitIndexedAccessType(V8Object indexedAccessType) {
		visitChildren(indexedAccessType);
	}

	protected void visitMappedType(V8Object mappedType) {
		visitChildren(mappedType);
	}

	protected void visitLiteralType(V8Object literalType) {
		visitChildren(literalType);
	}

	protected void visitNamedTupleMember(V8Object namedTupleMember) {
		visitChildren(namedTupleMember);
	}

	protected void visitImportType(V8Object importType) {
		visitChildren(importType);
	}

	protected void visitObjectBindingPattern(V8Object objectBindingPattern) {
		visitChildren(objectBindingPattern);
	}

	protected void visitArrayBindingPattern(V8Object arrayBindingPattern) {
		visitChildren(arrayBindingPattern);
	}

	protected void visitBindingElement(V8Object bindingElement) {
		visitChildren(bindingElement);
	}

	protected void visitArrayLiteralExpression(V8Object arrayLiteralExpression) {
		visitChildren(arrayLiteralExpression);
	}

	protected void visitObjectLiteralExpression(V8Object objectLiteralExpression) {
		visitChildren(objectLiteralExpression);
	}

	protected void visitPropertyAccessExpression(V8Object propertyAccessExpression) {
		visitChildren(propertyAccessExpression);
	}

	protected void visitElementAccessExpression(V8Object elementAccessExpression) {
		visitChildren(elementAccessExpression);
	}

	protected void visitCallExpression(V8Object callExpression) {
		visitChildren(callExpression);
	}

	protected void visitNewExpression(V8Object newExpression) {
		visitChildren(newExpression);
	}

	protected void visitTaggedTemplateExpression(V8Object taggedTemplateExpression) {
		visitChildren(taggedTemplateExpression);
	}

	protected void visitTypeAssertionExpression(V8Object typeAssertionExpression) {
		visitChildren(typeAssertionExpression);
	}

	protected void visitParenthesizedExpression(V8Object parenthesizedExpression) {
		visitChildren(parenthesizedExpression);
	}

	protected void visitFunctionExpression(V8Object functionExpression) {
		visitChildren(functionExpression);
	}

	protected void visitArrowFunction(V8Object arrowFunction) {
		visitChildren(arrowFunction);
	}

	protected void visitDeleteExpression(V8Object deleteExpression) {
		visitChildren(deleteExpression);
	}

	protected void visitTypeOfExpression(V8Object typeOfExpression) {
		visitChildren(typeOfExpression);
	}

	protected void visitVoidExpression(V8Object voidExpression) {
		visitChildren(voidExpression);
	}

	protected void visitAwaitExpression(V8Object awaitExpression) {
		visitChildren(awaitExpression);
	}

	protected void visitPrefixUnaryExpression(V8Object prefixUnaryExpression) {
		visitChildren(prefixUnaryExpression);
	}

	protected void visitPostfixUnaryExpression(V8Object postfixUnaryExpression) {
		visitChildren(postfixUnaryExpression);
	}

	protected void visitBinaryExpression(V8Object binaryExpression) {
		visitChildren(binaryExpression);
	}

	protected void visitConditionalExpression(V8Object conditionalExpression) {
		visitChildren(conditionalExpression);
	}

	protected void visitTemplateExpression(V8Object templateExpression) {
		visitChildren(templateExpression);
	}

	protected void visitYieldExpression(V8Object yieldExpression) {
		visitChildren(yieldExpression);
	}

	protected void visitSpreadElement(V8Object spreadElement) {
		visitChildren(spreadElement);
	}

	protected void visitClassExpression(V8Object classExpression) {
		visitChildren(classExpression);
	}

	protected void visitOmittedExpression(V8Object omittedExpression) {
		visitChildren(omittedExpression);
	}

	protected void visitExpressionWithTypeArguments(V8Object expressionWithTypeArguments) {
		visitChildren(expressionWithTypeArguments);
	}

	protected void visitAsExpression(V8Object asExpression) {
		visitChildren(asExpression);
	}

	protected void visitNonNullExpression(V8Object nonNullExpression) {
		visitChildren(nonNullExpression);
	}

	protected void visitMetaProperty(V8Object metaProperty) {
		visitChildren(metaProperty);
	}

	protected void visitSyntheticExpression(V8Object syntheticExpression) {
		visitChildren(syntheticExpression);
	}

	protected void visitTemplateSpan(V8Object templateSpan) {
		visitChildren(templateSpan);
	}

	protected void visitSemicolonClassElement(V8Object semicolonClassElement) {
		visitChildren(semicolonClassElement);
	}

	protected void visitBlock(V8Object block) {
		visitChildren(block);
	}

	protected void visitEmptyStatement(V8Object emptyStatement) {
		visitChildren(emptyStatement);
	}

	protected void visitVariableStatement(V8Object variableStatement) {
		visitChildren(variableStatement);
	}

	protected void visitExpressionStatement(V8Object expressionStatement) {
		visitChildren(expressionStatement);
	}

	protected void visitIfStatement(V8Object ifStatement) {
		visitChildren(ifStatement);
	}

	protected void visitDoStatement(V8Object doStatement) {
		visitChildren(doStatement);
	}

	protected void visitWhileStatement(V8Object whileStatement) {
		visitChildren(whileStatement);
	}

	protected void visitForStatement(V8Object forStatement) {
		visitChildren(forStatement);
	}

	protected void visitForInStatement(V8Object forInStatement) {
		visitChildren(forInStatement);
	}

	protected void visitForOfStatement(V8Object forOfStatement) {
		visitChildren(forOfStatement);
	}

	protected void visitContinueStatement(V8Object continueStatement) {
		visitChildren(continueStatement);
	}

	protected void visitBreakStatement(V8Object breakStatement) {
		visitChildren(breakStatement);
	}

	protected void visitReturnStatement(V8Object returnStatement) {
		visitChildren(returnStatement);
	}

	protected void visitWithStatement(V8Object withStatement) {
		visitChildren(withStatement);
	}

	protected void visitSwitchStatement(V8Object switchStatement) {
		visitChildren(switchStatement);
	}

	protected void visitLabeledStatement(V8Object labeledStatement) {
		visitChildren(labeledStatement);
	}

	protected void visitThrowStatement(V8Object throwStatement) {
		visitChildren(throwStatement);
	}

	protected void visitTryStatement(V8Object tryStatement) {
		visitChildren(tryStatement);
	}

	protected void visitDebuggerStatement(V8Object debuggerStatement) {
		visitChildren(debuggerStatement);
	}

	protected void visitVariableDeclaration(V8Object variableDeclaration) {
		visitChildren(variableDeclaration);
	}

	protected void visitVariableDeclarationList(V8Object variableDeclarationList) {
		visitChildren(variableDeclarationList);
	}

	protected void visitFunctionDeclaration(V8Object functionDeclaration) {
		visitChildren(functionDeclaration);
	}

	protected void visitClassDeclaration(V8Object classDeclaration) {
		visitChildren(classDeclaration);
	}

	protected void visitInterfaceDeclaration(V8Object interfaceDeclaration) {
		visitChildren(interfaceDeclaration);
	}

	protected void visitTypeAliasDeclaration(V8Object typeAliasDeclaration) {
		visitChildren(typeAliasDeclaration);
	}

	protected void visitEnumDeclaration(V8Object enumDeclaration) {
		visitChildren(enumDeclaration);
	}

	protected void visitModuleDeclaration(V8Object moduleDeclaration) {
		visitChildren(moduleDeclaration);
	}

	protected void visitModuleBlock(V8Object moduleBlock) {
		visitChildren(moduleBlock);
	}

	protected void visitCaseBlock(V8Object caseBlock) {
		visitChildren(caseBlock);
	}

	protected void visitNamespaceExportDeclaration(V8Object namespaceExportDeclaration) {
		visitChildren(namespaceExportDeclaration);
	}

	protected void visitImportEqualsDeclaration(V8Object importEqualsDeclaration) {
		visitChildren(importEqualsDeclaration);
	}

	protected void visitImportDeclaration(V8Object importDeclaration) {
		visitChildren(importDeclaration);
	}

	protected void visitImportClause(V8Object importClause) {
		visitChildren(importClause);
	}

	protected void visitNamespaceImport(V8Object namespaceImport) {
		visitChildren(namespaceImport);
	}

	protected void visitNamedImports(V8Object namedImports) {
		visitChildren(namedImports);
	}

	protected void visitImportSpecifier(V8Object importSpecifier) {
		visitChildren(importSpecifier);
	}

	protected void visitExportAssignment(V8Object exportAssignment) {
		visitChildren(exportAssignment);
	}

	protected void visitExportDeclaration(V8Object exportDeclaration) {
		visitChildren(exportDeclaration);
	}

	protected void visitNamedExports(V8Object namedExports) {
		visitChildren(namedExports);
	}

	protected void visitNamespaceExport(V8Object namespaceExport) {
		visitChildren(namespaceExport);
	}

	protected void visitExportSpecifier(V8Object exportSpecifier) {
		visitChildren(exportSpecifier);
	}

	protected void visitMissingDeclaration(V8Object missingDeclaration) {
		visitChildren(missingDeclaration);
	}

	protected void visitExternalModuleReference(V8Object externalModuleReference) {
		visitChildren(externalModuleReference);
	}

	protected void visitJsxElement(V8Object jsxElement) {
		visitChildren(jsxElement);
	}

	protected void visitJsxSelfClosingElement(V8Object jsxSelfClosingElement) {
		visitChildren(jsxSelfClosingElement);
	}

	protected void visitJsxOpeningElement(V8Object jsxOpeningElement) {
		visitChildren(jsxOpeningElement);
	}

	protected void visitJsxClosingElement(V8Object jsxClosingElement) {
		visitChildren(jsxClosingElement);
	}

	protected void visitJsxFragment(V8Object jsxFragment) {
		visitChildren(jsxFragment);
	}

	protected void visitJsxOpeningFragment(V8Object jsxOpeningFragment) {
		visitChildren(jsxOpeningFragment);
	}

	protected void visitJsxClosingFragment(V8Object jsxClosingFragment) {
		visitChildren(jsxClosingFragment);
	}

	protected void visitJsxAttribute(V8Object jsxAttribute) {
		visitChildren(jsxAttribute);
	}

	protected void visitJsxAttributes(V8Object jsxAttributes) {
		visitChildren(jsxAttributes);
	}

	protected void visitJsxSpreadAttribute(V8Object jsxSpreadAttribute) {
		visitChildren(jsxSpreadAttribute);
	}

	protected void visitJsxExpression(V8Object jsxExpression) {
		visitChildren(jsxExpression);
	}

	protected void visitCaseClause(V8Object caseClause) {
		visitChildren(caseClause);
	}

	protected void visitDefaultClause(V8Object defaultClause) {
		visitChildren(defaultClause);
	}

	protected void visitHeritageClause(V8Object heritageClause) {
		visitChildren(heritageClause);
	}

	protected void visitCatchClause(V8Object catchClause) {
		visitChildren(catchClause);
	}

	protected void visitPropertyAssignment(V8Object propertyAssignment) {
		visitChildren(propertyAssignment);
	}

	protected void visitShorthandPropertyAssignment(V8Object shorthandPropertyAssignment) {
		visitChildren(shorthandPropertyAssignment);
	}

	protected void visitSpreadAssignment(V8Object spreadAssignment) {
		visitChildren(spreadAssignment);
	}

	protected void visitEnumMember(V8Object enumMember) {
		visitChildren(enumMember);
	}

	protected void visitUnparsedPrologue(V8Object unparsedPrologue) {
		visitChildren(unparsedPrologue);
	}

	protected void visitUnparsedPrepend(V8Object unparsedPrepend) {
		visitChildren(unparsedPrepend);
	}

	protected void visitUnparsedText(V8Object unparsedText) {
		visitChildren(unparsedText);
	}

	protected void visitUnparsedInternalText(V8Object unparsedInternalText) {
		visitChildren(unparsedInternalText);
	}

	protected void visitUnparsedSyntheticReference(V8Object unparsedSyntheticReference) {
		visitChildren(unparsedSyntheticReference);
	}

	protected void visitSourceFile(V8Object sourceFile) {
		visitChildren(sourceFile);
	}

	protected void visitBundle(V8Object bundle) {
		visitChildren(bundle);
	}

	protected void visitUnparsedSource(V8Object unparsedSource) {
		visitChildren(unparsedSource);
	}

	protected void visitInputFiles(V8Object inputFiles) {
		visitChildren(inputFiles);
	}

	protected void visitJSDocTypeExpression(V8Object jSDocTypeExpression) {
		visitChildren(jSDocTypeExpression);
	}

	protected void visitJSDocAllType(V8Object jSDocAllType) {
		visitChildren(jSDocAllType);
	}

	protected void visitJSDocUnknownType(V8Object jSDocUnknownType) {
		visitChildren(jSDocUnknownType);
	}

	protected void visitJSDocNullableType(V8Object jSDocNullableType) {
		visitChildren(jSDocNullableType);
	}

	protected void visitJSDocNonNullableType(V8Object jSDocNonNullableType) {
		visitChildren(jSDocNonNullableType);
	}

	protected void visitJSDocOptionalType(V8Object jSDocOptionalType) {
		visitChildren(jSDocOptionalType);
	}

	protected void visitJSDocFunctionType(V8Object jSDocFunctionType) {
		visitChildren(jSDocFunctionType);
	}

	protected void visitJSDocVariadicType(V8Object jSDocVariadicType) {
		visitChildren(jSDocVariadicType);
	}

	protected void visitJSDocNamepathType(V8Object jSDocNamepathType) {
		visitChildren(jSDocNamepathType);
	}

	protected void visitJSDocComment(V8Object jSDocComment) {
		visitChildren(jSDocComment);
	}

	protected void visitJSDocTypeLiteral(V8Object jSDocTypeLiteral) {
		visitChildren(jSDocTypeLiteral);
	}

	protected void visitJSDocSignature(V8Object jSDocSignature) {
		visitChildren(jSDocSignature);
	}

	protected void visitJSDocTag(V8Object jSDocTag) {
		visitChildren(jSDocTag);
	}

	protected void visitJSDocAugmentsTag(V8Object jSDocAugmentsTag) {
		visitChildren(jSDocAugmentsTag);
	}

	protected void visitJSDocImplementsTag(V8Object jSDocImplementsTag) {
		visitChildren(jSDocImplementsTag);
	}

	protected void visitJSDocAuthorTag(V8Object jSDocAuthorTag) {
		visitChildren(jSDocAuthorTag);
	}

	protected void visitJSDocDeprecatedTag(V8Object jSDocDeprecatedTag) {
		visitChildren(jSDocDeprecatedTag);
	}

	protected void visitJSDocClassTag(V8Object jSDocClassTag) {
		visitChildren(jSDocClassTag);
	}

	protected void visitJSDocPublicTag(V8Object jSDocPublicTag) {
		visitChildren(jSDocPublicTag);
	}

	protected void visitJSDocPrivateTag(V8Object jSDocPrivateTag) {
		visitChildren(jSDocPrivateTag);
	}

	protected void visitJSDocProtectedTag(V8Object jSDocProtectedTag) {
		visitChildren(jSDocProtectedTag);
	}

	protected void visitJSDocReadonlyTag(V8Object jSDocReadonlyTag) {
		visitChildren(jSDocReadonlyTag);
	}

	protected void visitJSDocCallbackTag(V8Object jSDocCallbackTag) {
		visitChildren(jSDocCallbackTag);
	}

	protected void visitJSDocEnumTag(V8Object jSDocEnumTag) {
		visitChildren(jSDocEnumTag);
	}

	protected void visitJSDocParameterTag(V8Object jSDocParameterTag) {
		visitChildren(jSDocParameterTag);
	}

	protected void visitJSDocReturnTag(V8Object jSDocReturnTag) {
		visitChildren(jSDocReturnTag);
	}

	protected void visitJSDocThisTag(V8Object jSDocThisTag) {
		visitChildren(jSDocThisTag);
	}

	protected void visitJSDocTypeTag(V8Object jSDocTypeTag) {
		visitChildren(jSDocTypeTag);
	}

	protected void visitJSDocTemplateTag(V8Object jSDocTemplateTag) {
		visitChildren(jSDocTemplateTag);
	}

	protected void visitJSDocTypedefTag(V8Object jSDocTypedefTag) {
		visitChildren(jSDocTypedefTag);
	}

	protected void visitJSDocPropertyTag(V8Object jSDocPropertyTag) {
		visitChildren(jSDocPropertyTag);
	}

	protected void visitSyntaxList(V8Object syntaxList) {
		visitChildren(syntaxList);
	}

	protected void visitNotEmittedStatement(V8Object notEmittedStatement) {
		visitChildren(notEmittedStatement);
	}

	protected void visitPartiallyEmittedExpression(V8Object partiallyEmittedExpression) {
		visitChildren(partiallyEmittedExpression);
	}

	protected void visitCommaListExpression(V8Object commaListExpression) {
		visitChildren(commaListExpression);
	}

	protected void visitMergeDeclarationMarker(V8Object mergeDeclarationMarker) {
		visitChildren(mergeDeclarationMarker);
	}

	protected void visitEndOfDeclarationMarker(V8Object endOfDeclarationMarker) {
		visitChildren(endOfDeclarationMarker);
	}

	protected void visitSyntheticReferenceExpression(V8Object syntheticReferenceExpression) {
		visitChildren(syntheticReferenceExpression);
	}

	protected void visitCount(V8Object count) {
		visitChildren(count);
	}

//	protected void visitFirstAssignment(V8Object firstAssignment) {
//		visitChildren(firstAssignment);
//	}
//
//	protected void visitLastAssignment(V8Object lastAssignment) {
//		visitChildren(lastAssignment);
//	}
//
//	protected void visitFirstCompoundAssignment(V8Object firstCompoundAssignment) {
//		visitChildren(firstCompoundAssignment);
//	}
//
//	protected void visitLastCompoundAssignment(V8Object lastCompoundAssignment) {
//		visitChildren(lastCompoundAssignment);
//	}
//
//	protected void visitFirstReservedWord(V8Object firstReservedWord) {
//		visitChildren(firstReservedWord);
//	}
//
//	protected void visitLastReservedWord(V8Object lastReservedWord) {
//		visitChildren(lastReservedWord);
//	}
//
//	protected void visitFirstKeyword(V8Object firstKeyword) {
//		visitChildren(firstKeyword);
//	}
//
//	protected void visitLastKeyword(V8Object lastKeyword) {
//		visitChildren(lastKeyword);
//	}
//
//	protected void visitFirstFutureReservedWord(V8Object firstFutureReservedWord) {
//		visitChildren(firstFutureReservedWord);
//	}
//
//	protected void visitLastFutureReservedWord(V8Object lastFutureReservedWord) {
//		visitChildren(lastFutureReservedWord);
//	}
//
//	protected void visitFirstTypeNode(V8Object firstTypeNode) {
//		visitChildren(firstTypeNode);
//	}
//
//	protected void visitLastTypeNode(V8Object lastTypeNode) {
//		visitChildren(lastTypeNode);
//	}
//
//	protected void visitFirstPunctuation(V8Object firstPunctuation) {
//		visitChildren(firstPunctuation);
//	}
//
//	protected void visitLastPunctuation(V8Object lastPunctuation) {
//		visitChildren(lastPunctuation);
//	}
//
//	protected void visitFirstToken(V8Object firstToken) {
//		visitChildren(firstToken);
//	}
//
//	protected void visitLastToken(V8Object lastToken) {
//		visitChildren(lastToken);
//	}
//
//	protected void visitFirstTriviaToken(V8Object firstTriviaToken) {
//		visitChildren(firstTriviaToken);
//	}
//
//	protected void visitLastTriviaToken(V8Object lastTriviaToken) {
//		visitChildren(lastTriviaToken);
//	}
//
//	protected void visitFirstLiteralToken(V8Object firstLiteralToken) {
//		visitChildren(firstLiteralToken);
//	}
//
//	protected void visitLastLiteralToken(V8Object lastLiteralToken) {
//		visitChildren(lastLiteralToken);
//	}
//
//	protected void visitFirstTemplateToken(V8Object firstTemplateToken) {
//		visitChildren(firstTemplateToken);
//	}
//
//	protected void visitLastTemplateToken(V8Object lastTemplateToken) {
//		visitChildren(lastTemplateToken);
//	}
//
//	protected void visitFirstBinaryOperator(V8Object firstBinaryOperator) {
//		visitChildren(firstBinaryOperator);
//	}
//
//	protected void visitLastBinaryOperator(V8Object lastBinaryOperator) {
//		visitChildren(lastBinaryOperator);
//	}
//
//	protected void visitFirstStatement(V8Object firstStatement) {
//		visitChildren(firstStatement);
//	}
//
//	protected void visitLastStatement(V8Object lastStatement) {
//		visitChildren(lastStatement);
//	}
//
//	protected void visitFirstNode(V8Object firstNode) {
//		visitChildren(firstNode);
//	}
//
//	protected void visitFirstJSDocNode(V8Object firstJSDocNode) {
//		visitChildren(firstJSDocNode);
//	}
//
//	protected void visitLastJSDocNode(V8Object lastJSDocNode) {
//		visitChildren(lastJSDocNode);
//	}
//
//	protected void visitFirstJSDocTagNode(V8Object firstJSDocTagNode) {
//		visitChildren(firstJSDocTagNode);
//	}
//
//	protected void visitLastJSDocTagNode(V8Object lastJSDocTagNode) {
//		visitChildren(lastJSDocTagNode);
//	}
}
