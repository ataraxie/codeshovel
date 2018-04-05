package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Ydiff;
import com.felixgrund.codestory.ast.entities.Yfunction;
import jdk.nashorn.internal.ir.FunctionNode;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Interpreter {

	private LinkedHashMap<Ycommit, Ychange> findings;

	private Ycommit ycommit;

	public Interpreter(Ycommit ycommit) {
		this.findings = new LinkedHashMap<>();
		this.ycommit = ycommit;
	}

	public void interpret() throws IOException {
		if (ycommit.isFirstFunctionOccurrence()) {
			findings.put(ycommit, new Yintroduced());
		}
		if (isFunctionNotFoundAnymore()) {
			findings.put(ycommit, new Yremoved());
		}
		if (isFunctionFoundAgain()) {
			findings.put(ycommit, new Yadded());
		}
		if (isFunctionBodyModified()) {
			findings.put(ycommit, new Ymodbody());
//			findEdits();
		}
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
			String bodyCurrent = ycommit.getMatchedFunctionInfo().getBodyString();
			String bodyPrev = ycommit.getParent().getMatchedFunctionInfo().getBodyString();
			if (!bodyCurrent.equals(bodyPrev)) {
				ret = true;
			} else {
				ret = false; // debugger
			}
		}
		return ret;
	}

	private boolean isFunctionNotFoundAnymore() {
		boolean isNotFoundAnymore = false;
		if (isFunctionNotFoundAnymoreUsingFunctionName()) {
			isNotFoundAnymore = true;
			Yfunction parent = ycommit.getParent().getMatchedFunctionInfo();
			FunctionNode function = ycommit.getParser().findFunctionByNameAndBody("data", parent.getBodyString());
			int a = 1;
		}
		return isNotFoundAnymore;
	}

	private boolean isFunctionNotFoundAnymoreUsingFunctionName() {
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

	public LinkedHashMap<Ycommit, Ychange> getFindings() {
		return findings;
	}
}
