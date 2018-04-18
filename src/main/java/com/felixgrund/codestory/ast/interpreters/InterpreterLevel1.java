package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Ydiff;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class InterpreterLevel1 implements Interpreter {

	private Ychange interpretation;
	private Ycommit ycommit;

	public InterpreterLevel1(Ycommit ycommit) {
		this.interpretation = null;
		this.ycommit = ycommit;
	}

	@Override
	public void interpret() throws IOException {
		List<Ychange> changes = new ArrayList<>();
		if (ycommit.isFirstFunctionOccurrence()) {
			changes.add(new Yintroduced());
		}
		if (isFunctionNotFoundAnymore()) {
			changes.add(new Yremoved());
		}
		if (isFunctionFoundAgain()) {
			changes.add(new Yadded());
		}
		if (isFunctionBodyModified()) {
			changes.add(new Ymodbody());
//			findEdits();
		}

		int numChanges = changes.size();
		if (numChanges > 1) {
			this.interpretation = new Ymultichange(changes);
		} else if (numChanges == 1) {
			this.interpretation = changes.get(0);
		} else {
			this.interpretation = new Ynochange();
		}
	}

	@Override
	public Ychange getInterpretation() {
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


}
