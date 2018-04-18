package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Ydiff;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class InterpreterLevel2 implements Interpreter {

	private LinkedHashMap<Ycommit, Ychange> findings;

	private Ycommit ycommit;
	private Ychange level1Change;
	private Ychange level2Change;

	public InterpreterLevel2(Ycommit ycommit, Ychange level1Change) {
		this.findings = new LinkedHashMap<>();
		this.ycommit = ycommit;
		this.level1Change = level1Change;
	}

	@Override
	public void interpret() throws IOException {
		List<Ychange> changes = new ArrayList<>();
		if (isFunctionRename()) {
			changes.add(new Yinfilerename());
		}
		if (isReturnTypeChange()) {
			changes.add(new Yreturntypechange());
		}
		if (isParametersChange()) {
			changes.add(new Yparameterchange());
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

	private boolean isReturnTypeChange() {
		return false;
	}

	private boolean isFunctionRename() {
		return false;
	}

	private boolean isParametersChange() throws IOException {
		if (this.level1Change instanceof Yintroduced) {
			Ycommit parentCommit = ycommit.getParent();
			if (parentCommit != null) {
				Ydiff ydiff = ycommit.getYdiff();
				EditList editList = ydiff.getEditList();
				DiffFormatter formatter = ydiff.getFormatter();
				RawText aSource = new RawText(ycommit.getParent().getFileContent().getBytes());
				RawText bSource = new RawText(ycommit.getFileContent().getBytes());
				formatter.format(ydiff.getEditList(), aSource, bSource);
			}
		}


		return false;
	}

}
