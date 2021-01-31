package com.felixgrund.codeshovel.interpreters;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.entities.Ydiff;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			Ydiff ydiff = new Ydiff(startEnv.getRepositoryService(), ycommit.getCommit(), ycommit.getPrev().getCommit(), false);
			Yfunction matchedFunction = ycommit.getMatchedFunction();
			EditList editList = ydiff.getSingleEditList(ycommit.getFilePath());
			if (editList != null) {
				Yparser parentCommitParser = parentCommit.getParser();
				for (Edit edit : editList) {
					int beginA = edit.getBeginA();
					int endA = edit.getEndA();
					if (isEditInMethod(matchedFunction, edit)) {
						String filePathOldAndNew = ycommit.getFilePath();
						List<Yfunction> candidates = getRemovedFunctions(
								ycommit.getCommit(), parentCommit.getCommit(), filePathOldAndNew, filePathOldAndNew, false);
						List<Yfunction> candidatesLineRange = parentCommitParser.findMethodsByLineRange(beginA, endA);
						candidates.addAll(candidatesLineRange);
						candidates = removeDuplicates(candidates);
						ret = parentCommitParser.getMostSimilarFunction(candidates, matchedFunction, false);
						if (ret != null) {
							break; // found it an we can exit the loop
						}
					}
				}
			}
		}
		return ret;
	}

	private boolean isEditInMethod(Yfunction method, Edit edit) {
		int methodStart = method.getNameLineNumber();
		int methodEnd = method.getEndLineNumber();
		int editBegin = edit.getBeginB();
		int editEnd = edit.getEndB();
		return (editBegin >= methodStart && editBegin <= methodEnd) // edit begin is within method line range
				|| (editEnd >= methodStart && editEnd <= methodEnd) // edit end is within method line range
				|| (editBegin <= methodStart && editEnd >= methodEnd); // full method is within edit line range
	}

	private List<Yfunction> removeDuplicates(List<Yfunction> functions) {
		Map<String, Yfunction> functionsMap = new HashMap<>();
		for (Yfunction function : functions) {
			functionsMap.put(function.getId(), function);
		}
		return new ArrayList<Yfunction>(functionsMap.values());
	}

	private boolean isFirstFunctionOccurrence() {
		return this.ycommit.getPrev() == null || this.ycommit.getPrev().getMatchedFunction() == null;
	}
}
