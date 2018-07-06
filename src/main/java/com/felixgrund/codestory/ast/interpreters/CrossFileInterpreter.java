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

	private Yfunction startFunction;
	private Yparser startParser;

	public CrossFileInterpreter(Repository repository, String repositoryName, Ycommit ycommit) {
		super(repository, repositoryName, ycommit);
		this.startFunction = ycommit.getMatchedFunction();
		this.startParser = ycommit.getParser();
	}

	public Ychange interpret() throws Exception {
		Ychange ret = null;
		RevCommit commit = Utl.findCommitByName(repository, startFunction.getCommitName());
		RevCommit prevCommit = Utl.getPrevCommitNeglectingFile(repository, commit);
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
					List<Ychange> majorChanges = startParser.getMinorChanges(ycommit, compareFunction);
					List<Ysignaturechange> minorChanges = startParser.getMajorChanges(ycommit, compareFunction);
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
