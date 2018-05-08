package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Interpreter {

	private Ycommit ycommit;

	public Interpreter(Ycommit ycommit) {
		this.ycommit = ycommit;
	}

	public Ychange interpret() throws IOException {
		Ychange interpretation;

		Yfunction matchedFunction = this.ycommit.getMatchedFunction();

		List<Ychange> changes = new ArrayList<>();
		if (ycommit.isFirstFunctionOccurrence()) {
			Yfunction compareFunction = null;
			if (matchedFunction != null) {
				compareFunction = this.getCompareFunction(this.ycommit);
				if (compareFunction != null) {
					Yparameterchange yparameterchange = getParametersChange(matchedFunction, compareFunction);
					Yreturntypechange yreturntypechange = getReturnTypeChange(matchedFunction, compareFunction);
					Yinfilerename yinfilerename = getFunctionRename(matchedFunction, compareFunction);
					if (yinfilerename != null) {
						changes.add(yinfilerename);
					}
					if (yparameterchange != null) {
						changes.add(yparameterchange);
					}
					if (yreturntypechange != null) {
						changes.add(yreturntypechange);
					}
				}
			}

			if (changes.isEmpty()) {
				changes.add(new Yintroduced(ycommit));
			}
		}

		if (isFunctionNotFoundAnymore()) {
			changes.add(new Yremoved(ycommit));
		}

		if (isFunctionFoundAgain()) {
			changes.add(new Yadded(ycommit));
		}

		if (isFunctionBodyModified()) {
			changes.add(new Ymodbody(ycommit));
//			findEdits();
		}

		int numChanges = changes.size();
		if (numChanges > 1) {
			interpretation = new Ymultichange(ycommit, changes);
		} else if (numChanges == 1) {
			interpretation = changes.get(0);
		} else {
			interpretation = new Ynochange(ycommit);
		}

		return interpretation;
	}

	private List<Edit> findEdits() throws IOException {
		List<Edit> ret = new ArrayList<>();
		Ydiff Ydiff = ycommit.getYdiff();
		RevCommit prev = ycommit.getCommit().getParent(0);
		RawText aSource = new RawText(ycommit.getParent().getFileContent().getBytes());
		RawText bSource = new RawText(ycommit.getFileContent().getBytes());
//		Ydiff.getFormatter().format(Ydiff.getEditList(), aSource, bSource);
		return ret;
	}

	private boolean isFunctionBodyModified() {
		boolean ret = false;
		if (ycommit.isFunctionFound() && ycommit.getParent() != null && ycommit.getParent().isFunctionFound()) {
			String bodyCurrent = ycommit.getMatchedFunction().getBody();
			String bodyPrev = ycommit.getParent().getMatchedFunction().getBody();
			if (!bodyCurrent.equals(bodyPrev)) {
				ret = true;
			} else {
				ret = false; // debugger
			}
		}
		return ret;
	}

	private boolean isFunctionNotFoundAnymore() {
//		boolean isNotFoundAnymore = false;
//		if (isFunctionNotFoundAnymoreUsingFunctionName()) {
//			isNotFoundAnymore = true;
//			Yfunction parent = ycommit.getParent().getMatchedFunction();
//			FunctionNode function = ycommit.getParser().findFunctionByNameAndBody("data", parent.getBodyString());
//			int a = 1;
//		}
//		return isNotFoundAnymore;
		return !ycommit.isFunctionFound()
				&& ycommit.getParent() != null
				&& ycommit.getParent().isFunctionFound();
	}

	private boolean isFunctionFoundAgain() {
		return !ycommit.isFirstFunctionOccurrence()
				&& ycommit.isFunctionFound()
				&& ycommit.getParent() != null
				&& !ycommit.getParent().isFunctionFound();
	}

	private Yreturntypechange getReturnTypeChange(Yfunction matchedFunction, Yfunction compareFunction) {
		Yreturntypechange ret = null;
		Yreturn returnA = compareFunction.getReturnStmt();
		Yreturn returnB = matchedFunction.getReturnStmt();
		if (!returnA.equals(returnB)) {
			ret = new Yreturntypechange(ycommit, ycommit.getParent(), matchedFunction, compareFunction);
		}
		return ret;
	}

	private Yinfilerename getFunctionRename(Yfunction matchedFunction, Yfunction compareFunction) {
		Yinfilerename ret = null;
		if (compareFunction != null) {
			String nameA = compareFunction.getName();
			String nameB = matchedFunction.getName();
			if (!nameA.equals(nameB)) {
				ret = new Yinfilerename(ycommit, ycommit.getParent(), matchedFunction, compareFunction);
			}
		}
		return ret;
	}

	private Yfunction getCompareFunction(Ycommit ycommit) {
		Yfunction ret = null;
		Ycommit parentCommit = ycommit.getParent();
		if (parentCommit != null) {
			Ydiff ydiff = ycommit.getYdiff();
			Yfunction functionB = ycommit.getMatchedFunction();
			int lineNumberB = functionB.getNameLineNumber();
			EditList editList = ydiff.getEditList();
			for (Edit edit : editList) {
				int beginA = edit.getBeginA();
				int endA = edit.getEndA();
				int beginB = edit.getBeginB();
				int endB = edit.getEndB();
				if (beginB <= lineNumberB && endB >= lineNumberB) {
					Yparser parser = parentCommit.getParser();
					List<Yfunction> functionsInRange = parser.findFunctionsByLineRange(beginA, endA);
					for (Yfunction functionA : functionsInRange) {
						if (Utl.isFunctionBodySimilar(functionA, functionB)) {
							ret = functionA;
							break;
						}
					}
				}
			}
		}
		return ret;
	}

	private Yparameterchange getParametersChange(Yfunction matchedFunction, Yfunction compareFunction) throws IOException {
		Yparameterchange ret = null;
		List<Yparameter> parametersA = compareFunction.getParameters();
		List<Yparameter> parametersB = matchedFunction.getParameters();
		if (!parametersA.equals(parametersB)) {
			ret = new Yparameterchange(ycommit, ycommit.getParent(), matchedFunction, compareFunction);
		}
		return ret;
	}


}
