package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ydiff;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import com.github.javaparser.printer.lexicalpreservation.Difference;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrossFileInterpreter extends AbstractInterpreter {

	private Repository repository;
	private String repositoryName;
	private Yfunction startFunction;
	private Yparser startParser;

	public CrossFileInterpreter(Repository repository, String repositoryName, Yfunction startFunction, Yparser startParser) {
		this.repository = repository;
		this.repositoryName = repositoryName;
		this.startFunction = startFunction;
		this.startParser = startParser;
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
				if (diffEntry.getChangeType() == DiffEntry.ChangeType.RENAME) {
					compareFunction = getCompareFunctionFromFile(oldFilePath, prevCommit);
					if (compareFunction != null) {
						ret = new Yfilerename(startFunction, compareFunction);
					}
				} else {
					compareFunction = getCompareFunctionFromMultipleFiles(ydiff, prevCommit);
					if (compareFunction != null) {
						ret = new Ymovefromfile(startFunction, compareFunction);
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
				Yparser parser = createCompareParser(prevCommit, path);
				allFunctions.addAll(parser.getAllFunctions());
			}
		}
		if (!allFunctions.isEmpty()) {
			ret = this.startParser.getMostSimilarFunction(allFunctions, this.startFunction, true, false);
		}
		return ret;
	}

	private Yfunction getCompareFunctionFromFile(String filePath, RevCommit commit) throws Exception {
		Yparser parser = createCompareParser(commit, filePath);
		List<Yfunction> allFunctions = parser.getAllFunctions();
		Yfunction ret = this.startParser.getMostSimilarFunction(allFunctions, this.startFunction, false, false);
		return ret;
	}

	private Yparser createCompareParser(RevCommit commit, String filePath) throws Exception {
		String fileContent = Utl.findFileContent(this.repository, commit, filePath);
		return ParserFactory.getParser(this.repositoryName, filePath, fileContent, commit.getName());
	}

}
