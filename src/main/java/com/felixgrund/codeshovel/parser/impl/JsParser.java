package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.parser.AbstractParser;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.visitors.MethodVisitor;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JsParser extends AbstractParser implements Yparser {

	public static final String ACCEPTED_FILE_EXTENSION = ".*\\.js$";
	private Logger log = LoggerFactory.getLogger(JsParser.class);
	private Options parserOptions;

	public JsParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
		super(startEnv, filePath, fileContent, commit);
	}

	@Override
	protected List<Yfunction> parseMethods() throws ParseException {
		this.parserOptions = new Options("nashorn");
		this.parserOptions.set("anon.functions", true);
		this.parserOptions.set("parseMethods.only", true);
		this.parserOptions.set("scripting", true);
		this.parserOptions.set("language", "es6");

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ErrorManager errorManager = new Context.ThrowErrorManager();
		Context context = new Context(this.parserOptions, errorManager, classLoader);
		Source source = Source.sourceFor(this.filePath, this.fileContent);
		Parser originalParser = new Parser(context.getEnv(), source, errorManager);

		FunctionNode rootFunctionNode;
		try {
			rootFunctionNode = originalParser.parse();
		} catch (ParserException e) {
			throw new ParseException("Could not parseMethods root function node. Root cause: " + e.getMessage(), this.filePath, this.fileContent);
		}

		if (rootFunctionNode == null) {
			throw new ParseException("Could not parseMethods root function node.", this.filePath, this.fileContent);
		}

		JsMethodVisitor visitor = new JsMethodVisitor() {
			@Override
			public boolean methodMatches(Yfunction functionNode) {
				return true;
			}
		};

		rootFunctionNode.accept(visitor);

		return visitor.getMatchedNodes();
	}

	@Override
	public boolean functionNamesConsideredEqual(String aName, String bName) {
		return aName != null && bName != null &&
				(aName.equals(bName) || (aName.startsWith("L:") && bName.startsWith("L:")));
	}

	private List<String> getScopeParts(Yfunction compareFunction) {
		List<String> scopeParts = new ArrayList<>();
		String functionPath = compareFunction.getFunctionPath();
		if (functionPath.contains("#")) {
			String[] scopeSplit = functionPath.split("#");
			for (int i = 0; i < scopeSplit.length - 1; i++) { // ignore last element because it's the function name
				String scopeName = scopeSplit[i];
				if (!scopeName.startsWith("L:")) {
					scopeParts.add(scopeName);
					scopeParts.add(scopeName);
				}
			}
		}
		return scopeParts;
	}

	@Override
	public double getScopeSimilarity(Yfunction function, Yfunction compareFunction) {
		List<String> functionScopeParts = getScopeParts(function);
		List<String> compareFunctionScopeParts = getScopeParts(compareFunction);
		double scopeSimilarity;
		boolean hasCompareFunctionScope = !compareFunctionScopeParts.isEmpty();
		boolean hasThisFunctionScope = !functionScopeParts.isEmpty();
		if (!hasCompareFunctionScope && !hasThisFunctionScope) {
			scopeSimilarity = 1;
		} else if (hasCompareFunctionScope && hasThisFunctionScope) {
			int matchedScopeParts = 0;
			for (String part : compareFunctionScopeParts) {
				if (functionScopeParts.contains(part)) {
					matchedScopeParts += 1;
				}
			}
			scopeSimilarity = matchedScopeParts / (double) compareFunctionScopeParts.size();
		} else {
			scopeSimilarity = 0;
		}
		return scopeSimilarity;
	}

	@Override
	public String getAcceptedFileExtension() {
		return ACCEPTED_FILE_EXTENSION;
	}

	private Yfunction transformMethod(FunctionNode method) {
		return new JsFunction(method, this.commit, this.filePath, this.fileContent);
	}

	public abstract class JsMethodVisitor extends SimpleNodeVisitor {

		private List<Yfunction> matchedNodes = new ArrayList<>();

		public abstract boolean methodMatches(Yfunction method);

		@Override
		public boolean enterFunctionNode(FunctionNode functionNode) {
			Yfunction yfunction = transformMethod(functionNode);
			if (methodMatches(yfunction) && !functionNode.isProgram()) {
				matchedNodes.add(yfunction);
			}
			return true;
		}

		public List<Yfunction> getMatchedNodes() {
			return matchedNodes;
		}
	}

}
