package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Ydiff;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.Map;

public class CrossFileInterpreter extends AbstractInterpreter {

	private Ycommit ycommit;
	private Repository repository;

	public CrossFileInterpreter(Repository repository, Ycommit ycommit) {
		this.ycommit = ycommit;
		this.repository = repository;
	}

	public Ychange interpret() throws Exception {
		RevCommit commit = Utl.findCommitByName(repository, ycommit.getName());
		if (commit.getParentCount() > 0) {
			String prevCommitName = commit.getParent(0).getName();
			RevCommit prevCommit = Utl.findCommitByName(repository, prevCommitName);
			Ydiff ydiff = new Ydiff(this.repository, commit, prevCommit, true);
			DiffEntry diffEntry = ydiff.getDiff().get(ycommit.getFilePath());
			if (diffEntry != null) {
				int a = 1;
			}
		}
		return null;
	}

//	private Yfunction getCompareFunction() {
//		Yfunction ret = null;
//		Ycommit parentCommit = ycommit.getPrev();
//		if (parentCommit != null) {
//			Yfunction functionB = ycommit.getMatchedFunction();
//			int lineNumberB = functionB.getNameLineNumber();
//			EditList editList = ycommit.getYdiff().getSingleEditList(ycommit.getFilePath());
//			for (Edit edit : editList) {
//				int beginA = edit.getBeginA();
//				int endA = edit.getEndA();
//				int beginB = edit.getBeginB();
//				int endB = edit.getEndB();
//				if (beginB <= lineNumberB && endB >= lineNumberB) {
//					Yparser parser = parentCommit.getParser();
//					List<Yfunction> functionsInRange = parser.findFunctionsByLineRange(beginA, endA);
//					if (functionsInRange.size() == 1) {
//						ret = functionsInRange.get(0);
//					} else if (functionsInRange.size() > 1) {
//						ret = parser.getMostSimilarFunction(functionsInRange, functionB, true);
//					}
//				}
//			}
//		}
//		return ret;
//	}

}
