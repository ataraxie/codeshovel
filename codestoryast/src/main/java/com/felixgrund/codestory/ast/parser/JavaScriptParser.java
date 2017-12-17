package com.felixgrund.codestory.ast.parser;

import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class JavaScriptParser {

	private Parser parser;
	private ErrorManager errorManager;
	private Context context;
	private FunctionNode rootFunctionNode;

	public JavaScriptParser(String name, URL url) throws IOException {
		Options options = new Options("nashorn");
		options.set("anon.functions", true);
		options.set("parse.only", true);
		options.set("scripting", true);

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		this.context = new Context(options, this.errorManager, classLoader);

		this.errorManager = new ErrorManager();

		Source source = Source.sourceFor(name, url);

		this.parser = new Parser(context.getEnv(), source, this.errorManager);
		this.rootFunctionNode = this.parser.parse();
	}

	public List<FunctionNode> getAllFunctions() {
		return this.findFunctions(this.rootFunctionNode);
	}

	private List<FunctionNode> findFunctions(FunctionNode rootFunctionNode) {
//		Block block = rootFunctionNode.getBody();
//		List<Statement> statements = block.getStatements();
//		LexicalContext lexicalContext = new LexicalContext();
//		FunctionNodeVisitor visitor = new FunctionNodeVisitor();
//		visitor.enterFunctionNode(rootFunctionNode);

		FunctionNodeVisitor visitor = new FunctionNodeVisitor();
		rootFunctionNode.accept(visitor);

		return null;
	}

}
