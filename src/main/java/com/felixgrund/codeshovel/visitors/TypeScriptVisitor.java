package com.felixgrund.codeshovel.visitors;

import com.eclipsesource.v8.*;

import java.io.File;

public abstract class TypeScriptVisitor {
	private final NodeJS nodeJS = NodeJS.createNodeJS();
	private final V8Object ts = nodeJS.require(new File("node_modules/typescript/lib/typescript.js"));
	private final V8Object syntaxKind = ts.getObject("SyntaxKind");

	private boolean isKind(V8Object node, String kind) {
		return node.contains("kind") && node.getInteger("kind") == syntaxKind.getInteger(kind);
	}

	public void visit(String source) {
		int scriptTarget = ts.getObject("ScriptTarget").getInteger("ES2015");
		int scriptKind = ts.getObject("ScriptKind").getInteger("TS");
		V8Array parameters = new V8Array(nodeJS.getRuntime())
				.push("tempt.ts")
				.push(source)
				.push(scriptTarget)
				.push(true)
				.push(scriptKind);
		V8Object sourceFile = ts.executeObjectFunction("createSourceFile", parameters);
		parameters.release();
		visit(sourceFile);
		sourceFile.release();
	}

	public void visit(V8Object node) {
		if (isKind(node, "SourceFile")) {
			visitSourceFile(node);
		} else if (isKind(node, "FunctionDeclaration")) {
			visitFunctionDeclaration(node);
		} else if (isKind(node, "Constructor")) {
			visitConstructor(node);
		} else if (isKind(node, "MethodDeclaration")) {
			visitMethodDeclaration(node);
		} else if (isKind(node, "ArrowFunction")) {
			visitArrowFunction(node);
		} else if (isKind(node, "FunctionExpression")) {
			visitFunctionExpression(node);
		} else {
			visitChildren(node);
		}
	}

	protected void visitChildren(V8Object node) {
		V8Function callback = new V8Function(nodeJS.getRuntime(), (receiver, parameters) -> {
			final V8Object child = parameters.getObject(0);
			visit(child);
			return null;
		});
		ts.executeJSFunction("forEachChild", node, callback);
	}

	protected void visitArrowFunction(V8Object arrowFunction) {
		visitChildren(arrowFunction);
	}

	protected void visitConstructor(V8Object constructor) {
		visitChildren(constructor);
	}

	protected void visitFunctionDeclaration(V8Object functionDeclaration) {
		visitChildren(functionDeclaration);
	}

	protected void visitFunctionExpression(V8Object functionExpression) {
		visitChildren(functionExpression);
	}

	protected void visitMethodDeclaration(V8Object methodDeclaration) {
		visitChildren(methodDeclaration);
	}

	protected void visitSourceFile(V8Object sourceFile) {
		visitChildren(sourceFile);
	}
}
