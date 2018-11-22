package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.parser.AbstractParser;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.jrubyparser.CompatVersion;
import org.jrubyparser.Parser;
import org.jrubyparser.ast.MethodDefNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.parser.ParserConfiguration;
import org.jrubyparser.util.NoopVisitor;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RubyParser extends AbstractParser implements Yparser {

	public static final String ACCEPTED_FILE_EXTENSION = ".rb";

	private List<Yfunction> allMethods;

	public RubyParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
		super(startEnv, filePath, fileContent, commit);
	}

	@Override
	protected List<Yfunction> parseMethods() throws ParseException {
		Parser rubyParser = new Parser();
		StringReader in = new StringReader(this.fileContent);
		CompatVersion version = CompatVersion.RUBY2_0;
		ParserConfiguration config = new ParserConfiguration(0, version);
		Node rootNode = rubyParser.parse(this.filePath, in, config);
		if (rootNode == null) {
			throw new ParseException("Could not parseMethods root node", this.filePath, this.fileContent);
		}

		MethodNodeVisitor visitor = new MethodNodeVisitor() {
			@Override
			public boolean methodMatches(Yfunction method) {
				return true;
			}
		};
		rootNode.accept(visitor);
		return visitor.getMatchedNodes();
	}

	@Override
	public Yfunction findFunctionByNameAndLine(String name, int line) {
		return findMethod(new MethodNodeVisitor() {
			@Override
			public boolean methodMatches(Yfunction method) {
				String methodName = method.getName();
				int methodLineNumber = method.getNameLineNumber();
				return name.equals(methodName) && line == methodLineNumber;
			}
		});
	}

	private Yfunction findMethod(MethodNodeVisitor visitor) {
		Yfunction ret = null;
		List<Yfunction> matchedNodes = findAllMethods(visitor);
		if (matchedNodes.size() > 0) {
			ret = matchedNodes.get(0);
		}
		return ret;
	}

	private List<Yfunction> findAllMethods(MethodNodeVisitor visitor) {
		List<Yfunction> matchedMethods = new ArrayList<>();
		for (Yfunction method : this.allMethods) {
			if (visitor.methodMatches(method)) {
				matchedMethods.add(method);
			}
		}
		return matchedMethods;
	}


	@Override
	public List<Yfunction> findMethodsByLineRange(int beginLine, int endLine) {
		return findAllMethods(new MethodNodeVisitor() {
			@Override
			public boolean methodMatches(Yfunction method) {
				int lineNumber = method.getNameLineNumber();
				return lineNumber >= beginLine && lineNumber <= endLine;
			}
		});
	}

	@Override
	public Map<String, Yfunction> getAllMethodsCount() {
		return null;
	}

	@Override
	public Yfunction findFunctionByOtherFunction(Yfunction otherFunction) {
		return null;
	}

	@Override
	public double getScopeSimilarity(Yfunction function, Yfunction compareFunction) {
		return 0;
	}

	@Override
	public List<Ychange> getMinorChanges(Ycommit commit, Yfunction compareFunction) throws Exception {
		return null;
	}

	@Override
	public String getAcceptedFileExtension() {
		return null;
	}

	private Yfunction transformMethod(MethodDefNode node) {
		return new RubyFunction(node, this.commit, this.filePath, this.fileContent);
	}

	private abstract class MethodNodeVisitor extends NoopVisitor {

		private List<Yfunction> matchedNodes = new ArrayList<>();

		public abstract boolean methodMatches(Yfunction method);

		@Override
		protected Object visit(Node node) {
			if (node instanceof MethodDefNode) {
				Yfunction yfunction = transformMethod((MethodDefNode) node);
				matchedNodes.add(yfunction);
			}
			return super.visit(node);
		}

		public List<Yfunction> getMatchedNodes() {
			return matchedNodes;
		}

	}

}
