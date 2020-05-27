package com.felixgrund.codeshovel.visitors;

import com.eclipsesource.v8.*;
import com.eclipsesource.v8.utils.MemoryManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class TypeScriptVisitor {
	private static final String TYPESCRIPT_PATH = "node_modules/typescript/lib/typescript.js";

	private final V8Object ts;
	private final V8Object syntaxKind;
	private final MemoryManager scope;
	private final NodeJS nodeJS;

	private final Map<String, Integer> syntaxKindCache = new HashMap<String, Integer>();

	protected final V8Object sourceFile;

	public TypeScriptVisitor(String name, String source) throws IOException {
		InputStream inputStream = TypeScriptVisitor.class.getClassLoader().getResourceAsStream(TYPESCRIPT_PATH);
		File file = File.createTempFile("typescript", null);
		file.deleteOnExit();
		FileUtils.copyInputStreamToFile(inputStream, file);
		nodeJS = NodeJS.createNodeJS();
		scope = new MemoryManager(nodeJS.getRuntime());
		ts = nodeJS.require(file);
		syntaxKind = ts.getObject("SyntaxKind");
		// TODO are these the right parameters?
		ts.getRuntime().getLocker().acquire();
		int scriptTarget = ts.getObject("ScriptTarget").getInteger("ES2015");
		int scriptKind = ts.getObject("ScriptKind").getInteger("TS");
		V8Array parameters = new V8Array(ts.getRuntime())
				.push(name)
				.push(source)
				.push(scriptTarget)
				.push(true)
				.push(scriptKind);
		sourceFile = ts.executeObjectFunction("createSourceFile", parameters);
		parameters.release();
	}

	protected boolean isKind(V8Object node, String kind) {
		if (!syntaxKindCache.containsKey(kind)) {
			syntaxKindCache.put(kind, syntaxKind.getInteger(kind));
		}
		return node.contains("kind") && node.getInteger("kind") == syntaxKindCache.get(kind);
	}

	protected V8Object addStartAndEndLines(V8Object node) {
		V8Array parameters = new V8Array(ts.getRuntime())
				.push(node.executeIntegerFunction("getStart", new V8Array(ts.getRuntime())));
		V8Object start = sourceFile.executeObjectFunction("getLineAndCharacterOfPosition", parameters);
		parameters.release();

		parameters = new V8Array(ts.getRuntime())
				.push(node.executeIntegerFunction("getEnd", new V8Array(ts.getRuntime())));
		V8Object end = sourceFile.executeObjectFunction("getLineAndCharacterOfPosition", parameters);
		parameters.release();

		int startLine = start.getInteger("line");
		int endLine = end.getInteger("line");

		start.release();
		end.release();
		// Add one because lines are zero indexed in ts
		node.add("startLine", startLine + 1);
		node.add("endLine", endLine + 1);
		return node;
	}

	public void visit() {
		visit(sourceFile);
		sourceFile.release();
		scope.release();
		nodeJS.release();
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
		V8Function callback = new V8Function(ts.getRuntime(), (receiver, parameters) -> {
			final V8Object child = parameters.getObject(0);
			visit(child);
			child.release();
			return null;
		});
		ts.executeJSFunction("forEachChild", node, callback);
		callback.release();
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
