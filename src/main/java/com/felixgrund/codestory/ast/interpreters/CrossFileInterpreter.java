package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Ydiff;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrossFileInterpreter extends AbstractInterpreter {

	private Repository repository;
	private String repositoryName;
	private Yfunction startFunction;
	private Yparser startParser;
	private Ycommit startCommit;

	public CrossFileInterpreter(Repository repository, String repositoryName, Ycommit ycommit) {
		this.repository = repository;
		this.repositoryName = repositoryName;
		this.startFunction = ycommit.getMatchedFunction();
		this.startParser = ycommit.getParser();
		this.startCommit = ycommit;
	}

	public Ychange interpret() throws Exception {
		Ychange ret = null;
		RevCommit commit = Utl.findCommitByName(repository, startFunction.getCommitName());
		if (commit.getParentCount() > 0) {
			String prevCommitName = commit.getParent(0).getName();
			RevCommit prevCommit = Utl.findCommitByName(repository, prevCommitName);
			Ydiff ydiff = new Ydiff(this.repository, commit, prevCommit, true);
			Map<String, DiffEntry> diffEntries = ydiff.getDiff();
			DiffEntry diffEntry = diffEntries.get(startFunction.getSourceFilePath());
			if (diffEntry != null) {
				String oldFilePath = diffEntry.getOldPath();
				Yfunction compareFunction = null;
				Ycrossfilechange crossFileChange = null;
				if (diffEntry.getChangeType() == DiffEntry.ChangeType.RENAME) {
					compareFunction = getCompareFunctionFromFile(oldFilePath, prevCommit);
					if (compareFunction != null) {
						crossFileChange = new Yfilerename(startFunction, compareFunction);
					}
				} else {
					compareFunction = getCompareFunctionFromMultipleFiles(ydiff, prevCommit);
					if (compareFunction != null) {
						crossFileChange = new Ymovefromfile(startFunction, compareFunction);
					}
				}

				if (crossFileChange != null) {
					List<Ychange> allChanges = new ArrayList<>();
					allChanges.add(crossFileChange);
					List<Ychange> majorChanges = startParser.getMinorChanges(startCommit, compareFunction);
					List<Ysignaturechange> minorChanges = startParser.getMajorChanges(startCommit, compareFunction);
					allChanges.addAll(majorChanges);
					allChanges.addAll(minorChanges);
					if (allChanges.size() == 1) {
						ret = allChanges.get(0);
					} else {
						ret = new Ymultichange(startFunction.getCommitName(), allChanges);
					}
				}
			}
		}
		return ret;
	}

	private Yfunction getCompareFunctionFromMultipleFiles(Ydiff ydiff, RevCommit prevCommit) throws Exception {
		Yfunction ret = null;
		List<Yfunction> allFunctions = new ArrayList<>();
		for (String path : ydiff.getOldPaths()) {
			if (path.endsWith(this.startParser.getAcceptedFileExtension())) {
				Yparser parserOld = createParser(prevCommit, path);
				Yparser parserNew = createParser(this.startCommit.getCommit(), path);
				List<Yfunction> removedFunctions;
				if (parserNew == null) {
					removedFunctions = parserOld.getAllFunctions();
				} else {
					Map<String, Yfunction> functionsNew = parserNew.getAllFunctionsAsMap();
					Map<String, Yfunction> functionsOld = parserOld.getAllFunctionsAsMap();
					removedFunctions = getRemovedFunctions(functionsNew, functionsOld);
				}
				allFunctions.addAll(removedFunctions);
			}
		}

		if (!allFunctions.isEmpty()) {
			ret = this.startParser.getMostSimilarFunction(allFunctions, this.startFunction, true, false);
		}
		return ret;
	}

	private List<Yfunction> getRemovedFunctions(Map<String, Yfunction> functionsNew, Map<String, Yfunction> functionsOld) {
		List<Yfunction> ret = new ArrayList<>();
		Set<String> newFunctionIds = functionsNew.keySet();
		for (String functionId : functionsOld.keySet()) {
			if (!newFunctionIds.contains(functionId)) {
				ret.add(functionsOld.get(functionId));
			}
		}
		return ret;
	}

	private Yfunction getCompareFunctionFromFile(String filePath, RevCommit commit) throws Exception {
		Yparser parser = createParser(commit, filePath);
		List<Yfunction> allFunctions = parser.getAllFunctions();
		Yfunction ret = this.startParser.getMostSimilarFunction(allFunctions, this.startFunction, false, false);
		return ret;
	}

	private Yparser createParser(RevCommit commit, String filePath) throws Exception {
		Yparser ret = null;
		String fileContent = Utl.findFileContent(this.repository, commit, filePath);
		if (fileContent != null) {
			ret = ParserFactory.getParser(this.repositoryName, filePath, fileContent, commit.getName());
		}
		return ret;
	}

}
