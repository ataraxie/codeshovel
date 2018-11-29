package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.parser.AbstractParser;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.util.Utl;
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

public class RubyParser extends AbstractParser implements Yparser {

	public static final String ACCEPTED_FILE_EXTENSION = ".rb";

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
	public double getScopeSimilarity(Yfunction function, Yfunction compareFunction) {
		return Utl.parentNamesMatch(function, compareFunction) ? 1.0 : 0.0;
	}

	@Override
	public String getAcceptedFileExtension() {
		return ACCEPTED_FILE_EXTENSION;
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
