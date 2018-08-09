package com.felixgrund.codeshovel.interpreters;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.List;

public class InFileInterpreter extends AbstractInterpreter {


	public InFileInterpreter(StartEnvironment startEnv, Ycommit ycommit) {
		super(startEnv, ycommit);
	}

	public Ychange interpret() throws Exception {
		Ychange interpretation;

		Yfunction matchedFunction = this.ycommit.getMatchedFunction();
		Yparser parser = this.ycommit.getParser();
		List<Ychange> changes = new ArrayList<>();

		if (isFirstFunctionOccurrence()) {
			Yfunction compareFunction = null;
			if (matchedFunction != null) {
				compareFunction = this.getCompareFunction(this.ycommit);
			}
			if (compareFunction != null) {
				List<Ysignaturechange> majorChanges = parser.getMajorChanges(this.ycommit, compareFunction);
				changes.addAll(majorChanges);
			}

			if (changes.isEmpty()) {
				changes.add(new Yintroduced(this.startEnv, this.ycommit.getMatchedFunction()));
			} else {
				Ysignaturechange firstMajorChange = (Ysignaturechange) changes.get(0);
				List<Ychange> minorChanges = parser.getMinorChanges(ycommit, firstMajorChange.getOldFunction());
				changes.addAll(minorChanges);
			}
		} else {
			Yfunction parentMatchedFunction = ycommit.getPrev().getMatchedFunction();
			if (ycommit.getPrev() != null && parentMatchedFunction != null) {
				List<Ychange> minorChanges = parser.getMinorChanges(ycommit, parentMatchedFunction);
				changes.addAll(minorChanges);
			}
		}

		int numChanges = changes.size();
		if (numChanges > 1) {
			interpretation = new Ymultichange(this.startEnv, this.ycommit.getCommit(), changes);
		} else if (numChanges == 1) {
			interpretation = changes.get(0);
		} else {
			interpretation = new Ynochange(this.startEnv, this.ycommit.getCommit());
		}

		return interpretation;
	}

	private Yfunction getCompareFunction(Ycommit ycommit) throws Exception {
		Yfunction ret = null;
		Ycommit parentCommit = ycommit.getPrev();
		if (parentCommit != null) {
			Yfunction functionB = ycommit.getMatchedFunction();
			int lineNumberB = functionB.getNameLineNumber();
			EditList editList = ycommit.getYdiff().getSingleEditList(ycommit.getFilePath());
			if (editList != null) {
				Yparser parentCommitParser = parentCommit.getParser();
				for (Edit edit : editList) {
					int beginA = edit.getBeginA();
					int endA = edit.getEndA();
					int beginB = edit.getBeginB();
					int endB = edit.getEndB();
					if (beginB <= lineNumberB && endB >= lineNumberB) {
						List<Yfunction> candidates = parentCommitParser.findFunctionsByLineRange(beginA, endA);
						String filePathOldAndNew = ycommit.getFilePath(); // FIXME: I'm not too sure if this is ok
						List<Yfunction> removedFunctions = getRemovedFunctions(
								ycommit.getCommit(), parentCommit.getCommit(), filePathOldAndNew, filePathOldAndNew);
						candidates.addAll(removedFunctions);
						ret = parentCommitParser.getMostSimilarFunction(candidates, functionB, false, false);
					}
				}
			}
		}
		return ret;
	}

	private boolean isFirstFunctionOccurrence() {
		return this.ycommit.getPrev() == null || this.ycommit.getPrev().getMatchedFunction() == null;
	}


}
