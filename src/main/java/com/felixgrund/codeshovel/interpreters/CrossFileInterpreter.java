package com.felixgrund.codeshovel.interpreters;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.entities.Ydiff;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import org.eclipse.jgit.diff.DiffEntry;
import com.felixgrund.codeshovel.wrappers.Commit;

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
		Commit commit = this.startFunction.getCommit();
		Commit prevCommit = startEnv.getRepositoryService().getPrevCommitNeglectingFile(commit);
		if (prevCommit != null) {
			Ydiff ydiff = new Ydiff(startEnv.getRepositoryService(), commit, prevCommit, true);
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
						ret = new Ymultichange(this.startEnv, this.startFunction.getCommit(), allChanges);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Handles comparing methods that are spread across files.
	 *
	 * e.g., to check if a method was in a previous version of a different file.
	 *
	 * By looking at removed functions this specifically searches for methods
	 * that were moved between files in a revision.
	 *
	 * @param ydiff
	 * @param prevCommit
	 * @return
	 * @throws Exception
	 */
	private Yfunction getCompareFunctionFromMultipleFiles(Ydiff ydiff, Commit prevCommit) throws Exception {
		Yfunction ret = null;
		List<Yfunction> allFunctions = new ArrayList<>();
		String acceptedFileExtension = this.startParser.getAcceptedFileExtension();
		Map<String, String> pathMapping = ydiff.getPathMapping();
		for (String oldPath : pathMapping.keySet()) {
			if (oldPath.matches(acceptedFileExtension)) {
				String newPath = pathMapping.get(oldPath);
				List<Yfunction> removedFunctions = getRemovedFunctions(this.ycommit.getCommit(), prevCommit, oldPath, newPath, true);
				allFunctions.addAll(removedFunctions);
			}
		}

		if (!allFunctions.isEmpty()) {
			ret = this.startParser.getMostSimilarFunction(allFunctions, this.startFunction, true);
		}
		return ret;
	}

	/**
	 * Handles comparing methods within a file.
	 *
	 * e.g., to check if a method was in the previous version of the same file.
	 *
	 * @param filePath
	 * @param commit
	 * @return
	 * @throws Exception
	 */
	private Yfunction getCompareFunctionFromFile(String filePath, Commit commit) throws Exception {
		Yparser parser = createParserForCommitAndFile(commit, filePath);
		List<Yfunction> allFunctions = parser.getAllMethods();
		return this.startParser.getMostSimilarFunction(allFunctions, this.startFunction, false);
	}

}
