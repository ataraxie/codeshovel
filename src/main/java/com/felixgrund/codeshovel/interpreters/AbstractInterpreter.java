package com.felixgrund.codeshovel.interpreters;

import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.exceptions.NoParserFoundException;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.util.ParserFactory;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.io.IOException;
import java.util.*;

public abstract class AbstractInterpreter {

	protected RepositoryService repositoryService;

	protected StartEnvironment startEnv;
	protected Ycommit ycommit;
	protected Repository repository;
	protected String repositoryName;

	protected abstract Ychange interpret() throws Exception;

	public AbstractInterpreter(StartEnvironment startEnv, Ycommit ycommit) {
		this.startEnv = startEnv;
		this.repository = startEnv.getRepository();
		this.repositoryName = startEnv.getRepositoryName();
		this.repositoryService = startEnv.getRepositoryService();
		this.ycommit = ycommit;
	}

	protected Yparser createParserForCommitAndFile(Commit commit, String filePath)
			throws IOException, ParseException, NoParserFoundException {
		Yparser ret = null;
		String fileContent = repositoryService.findFileContent(commit, filePath);
		if (fileContent != null) {
			ret = ParserFactory.getParser(this.startEnv, filePath, fileContent, commit);
		}
		return ret;
	}

	protected List<Yfunction> getRemovedFunctions(Commit commitNew, Commit commitOld, String oldFilePath, String newFilePath, boolean strictMode) {

		List<Yfunction> ret = new ArrayList<>();
		// TODO: this shouldn't be done here because we already have these parsers!
		try {
			Yparser parserOld = createParserForCommitAndFile(commitOld, oldFilePath);
			Yparser parserNew = createParserForCommitAndFile(commitNew, newFilePath);
			if (parserNew == null) {
				ret = parserOld.getAllFunctions();
			} else {
				Map<String, Yfunction> functionsNew = parserNew.getAllFunctionsCount();
				Map<String, Yfunction> functionsOld = parserOld.getAllFunctionsCount();
				Map<String, Integer> nameCountNew = getFunctionNameCount(functionsNew);
				Map<String, Integer> nameCountOld = getFunctionNameCount(functionsOld);

				Set<String> newFunctionIds = functionsNew.keySet();

				for (String functionId : functionsOld.keySet()) {
					boolean functionIdLost = !newFunctionIds.contains(functionId);
					if (functionIdLost) {
						if (strictMode) {
							Yfunction functionOld = functionsOld.get(functionId);
							String functionName = functionOld.getName();
							Integer countOld = nameCountOld.get(functionName);
							Integer countNew = nameCountNew.get(functionName);
							if (countNew == null || countNew < countOld) {
								ret.add(functionsOld.get(functionId));
							}
						} else {
							ret.add(functionsOld.get(functionId));
						}
					}
				}
			}
		} catch (ParseException | NoParserFoundException | IOException e) {
			// return empty array
		}

		return ret;
	}

	private Map<String, Integer> getFunctionNameCount(Map<String, Yfunction> functions) {
		Map<String, Integer> ret = new HashMap<>();
		for (Yfunction function : functions.values()) {
			String name = function.getName();
			Integer oldValue = ret.get(name);
			if (oldValue == null) {
				ret.put(name, 1);
			} else {
				ret.put(name, oldValue + 1);
			}
		}
		return ret;
	}


}
