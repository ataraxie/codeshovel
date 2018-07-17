package com.felixgrund.codeshovel.interpreters;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.entities.Ydiff;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrossFileInterpreter extends AbstractInterpreter {

	private Yfunction startFunction;
	private Yparser startParser;

	public CrossFileInterpreter(StartEnvironment startEnv, Ycommit ycommit) {
		super(startEnv, ycommit);
		this.startFunction = ycommit.getMatchedFunction();
		this.startParser = ycommit.getParser();
	}

	public Ychange interpret() throws Exception {
		Ychange ret = null;
		RevCommit commit = repositoryService.findCommitByName(this.startFunction.getCommitName());
		RevCommit prevCommit = repositoryService.getPrevCommitNeglectingFile(commit);
		if (prevCommit != null) {
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
						crossFileChange = new Yfilerename(this.startEnv, this.startFunction, compareFunction);
					}
				} else {
					compareFunction = getCompareFunctionFromMultipleFiles(ydiff, prevCommit);
					if (compareFunction != null) {
						crossFileChange = new Ymovefromfile(this.startEnv, this.startFunction, compareFunction);
					}
				}

				if (crossFileChange != null) {
					List<Ychange> allChanges = new ArrayList<>();
					allChanges.add(crossFileChange);
					List<Ychange> majorChanges = startParser.getMinorChanges(ycommit, compareFunction);
					List<Ysignaturechange> minorChanges = startParser.getMajorChanges(ycommit, compareFunction);
					allChanges.addAll(majorChanges);
					allChanges.addAll(minorChanges);
					if (allChanges.size() == 1) {
						ret = allChanges.get(0);
					} else {
						ret = new Ymultichange(this.startEnv, this.startFunction.getCommitName(), allChanges);
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
				List<Yfunction> removedFunctions = getRemovedFunctions(this.ycommit.getCommit(), prevCommit, path);
				allFunctions.addAll(removedFunctions);
			}
		}

		if (!allFunctions.isEmpty()) {
			ret = this.startParser.getMostSimilarFunction(allFunctions, this.startFunction, true, false);
		}
		return ret;
	}

	private Yfunction getCompareFunctionFromFile(String filePath, RevCommit commit) throws Exception {
		Yparser parser = createParserForCommitAndFile(commit, filePath);
		List<Yfunction> allFunctions = parser.getAllFunctions();
		Yfunction ret = this.startParser.getMostSimilarFunction(allFunctions, this.startFunction, false, false);
		return ret;
	}

}
