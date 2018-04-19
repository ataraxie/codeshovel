package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.parser.Yparser;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InterpreterLevel2 implements Interpreter {

	private Ycommit ycommit;
	private Ychange level1Change;
	private Ychange level2Change;

	private Yfunction matchedFunction;
	private Yfunction compareFunction;

	public InterpreterLevel2(Ycommit ycommit, Ychange level1Change) {
		this.ycommit = ycommit;
		this.level1Change = level1Change;
	}

	@Override
	public void interpret() throws IOException {
		List<Ychange> changes = new ArrayList<>();

		this.matchedFunction = this.ycommit.getMatchedFunction();
		this.compareFunction = getCompareFunction();

		Yparameterchange yparameterchange = getParametersChange();
		Yreturntypechange yreturntypechange = getReturnTypeChange();
		Yinfilerename yinfilerename = getFunctionRename();

		if (yinfilerename != null) {
			changes.add(yinfilerename);
		}
		if (yparameterchange != null) {
			changes.add(yparameterchange);
		}
		if (yreturntypechange != null) {
			changes.add(yreturntypechange);
		}

		int numChanges = changes.size();
		if (numChanges > 1) {
			this.level2Change = new Ymultichange(changes);
		} else if (numChanges == 1) {
			this.level2Change = changes.get(0);
		} else {
			this.level2Change = this.level1Change;
		}
	}

	@Override
	public Ychange getInterpretation() {
		return level2Change;
	}

	private Yreturntypechange getReturnTypeChange() {
		Yreturntypechange ret = null;
		if (this.compareFunction != null) {
			Yreturn returnA = this.compareFunction.getReturnStmt();
			Yreturn returnB = this.matchedFunction.getReturnStmt();
			if (!returnA.equals(returnB)) {
				ret = new Yreturntypechange(returnA.getType(), returnB.getType());
			}
		}
		return ret;
	}

	private Yinfilerename getFunctionRename() {
		Yinfilerename ret = null;
		if (this.compareFunction != null) {
			String nameA = this.compareFunction.getName();
			String nameB = this.matchedFunction.getName();
			if (!nameA.equals(nameB)) {
				ret = new Yinfilerename(nameA, nameB);
			}
		}
		return ret;
	}

	private Yfunction getCompareFunction() {
		Yfunction ret = null;
		Ycommit parentCommit = ycommit.getParent();
		if (parentCommit != null) {
			Ydiff ydiff = ycommit.getYdiff();
			Yfunction yfunctionB = ycommit.getMatchedFunction();
			int lineNumberB = yfunctionB.getNameLineNumber();
			EditList editList = ydiff.getEditList();
			for (Edit edit : editList) {
				int beginA = edit.getBeginA();
				int endA = edit.getEndA();
				int beginB = edit.getBeginB();
				int endB = edit.getEndB();
				if (beginB <= lineNumberB && endB >= lineNumberB) {
					Yparser parser = parentCommit.getParser();
					List<Yfunction> functionsInRange = parser.findFunctionsByLineRange(beginA, endA);
					if (functionsInRange.size() > 0) {
						ret = functionsInRange.get(0);
					}
				}
			}
		}
		return ret;
	}

	private Yparameterchange getParametersChange() throws IOException {
		Yparameterchange ret = null;
		if (this.compareFunction != null) {
			List<Yparameter> parametersA = this.compareFunction.getParameters();
			List<Yparameter> parametersB = this.matchedFunction.getParameters();
			if (!parametersA.equals(parametersB)) {
				ret = new Yparameterchange(parametersA, parametersB);
			}
		}
		return ret;
	}


}
