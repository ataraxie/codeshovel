package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.Environment;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractInterpreter {

	protected Environment startEnv;
	protected Ycommit ycommit;
	protected Repository repository;
	protected String repositoryName;

	protected abstract Ychange interpret() throws Exception;

	public AbstractInterpreter(Environment startEnv, Ycommit ycommit) {
		this.startEnv = startEnv;
		this.repository = startEnv.getRepository();
		this.repositoryName = startEnv.getRepositoryName();
		this.ycommit = ycommit;
	}

	protected Yparser createParserForCommitAndFile(RevCommit commit, String filePath) throws Exception {
		Yparser ret = null;
		String fileContent = Utl.findFileContent(this.repository, commit, filePath);
		if (fileContent != null) {
			ret = ParserFactory.getParser(this.startEnv, filePath, fileContent, commit.getName());
		}
		return ret;
	}

	protected List<Yfunction> getRemovedFunctions(RevCommit commitNew, RevCommit commitOld, String filePath) throws Exception {
		List<Yfunction> ret = new ArrayList<>();
		Yparser parserOld = createParserForCommitAndFile(commitOld, filePath);
		Yparser parserNew = createParserForCommitAndFile(commitNew, filePath);
		if (parserNew == null) {
			ret = parserOld.getAllFunctions();
		} else {
			Map<String, Yfunction> functionsNew = parserNew.getAllFunctionsAsMap();
			Map<String, Yfunction> functionsOld = parserOld.getAllFunctionsAsMap();
			Set<String> newFunctionIds = functionsNew.keySet();
			for (String functionId : functionsOld.keySet()) {
				if (!newFunctionIds.contains(functionId)) {
					ret.add(functionsOld.get(functionId));
				}
			}
		}

		return ret;
	}


}
