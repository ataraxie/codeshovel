package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.entities.YCommit;
import com.felixgrund.codestory.ast.entities.YDiff;
import com.felixgrund.codestory.ast.entities.YFunction;
import jdk.nashorn.internal.ir.FunctionNode;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Interpreter {

	private List<String> findings;

	private YCommit yCommit;

	public Interpreter(YCommit yCommit) {
		this.findings = new ArrayList<>();
		this.yCommit = yCommit;
	}

	public void interpret() throws IOException {
		if (yCommit.isFirstFunctionOccurrence()) {
			findings.add("Function was first introduced");
		}
		if (isFunctionNotFoundAnymore()) {
			findings.add("Function was removed");
		}
		if (isFunctionFoundAgain()) {
			findings.add("Function was added");
		}
		if (isFunctionModified()) {
			findings.add("Function was modified");
			findEdits();
		}
	}

	private List<Edit> findEdits() throws IOException {
		List<Edit> ret = new ArrayList<>();
		YDiff YDiff = yCommit.getYDiff();
		RawText aSource = new RawText(yCommit.getPrev().getFileContent().getBytes());
		RawText bSource = new RawText(yCommit.getFileContent().getBytes());
//		YDiff.getFormatter().format(YDiff.getEditList(), aSource, bSource);
		return ret;
	}

	private boolean isFunctionModified() {
		boolean ret = false;
		if (yCommit.isFunctionFound() && yCommit.getPrev() != null && yCommit.getPrev().isFunctionFound()) {
			String bodyCurrent = yCommit.getMatchedFunctionInfo().getBodyString();
			String bodyPrev = yCommit.getPrev().getMatchedFunctionInfo().getBodyString();
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
			YFunction prev = yCommit.getPrev().getMatchedFunctionInfo();
			FunctionNode function = yCommit.getParser().findFunctionByNameAndBody("data", prev.getBodyString());
			int a = 1;
		}
		return isNotFoundAnymore;
	}

	private boolean isFunctionNotFoundAnymoreUsingFunctionName() {
		return !yCommit.isFunctionFound()
				&& yCommit.getPrev() != null
				&& yCommit.getPrev().isFunctionFound();
	}

	private boolean isFunctionFoundAgain() {
		return !yCommit.isFirstFunctionOccurrence()
				&& yCommit.isFunctionFound()
				&& yCommit.getPrev() != null
				&& !yCommit.getPrev().isFunctionFound();
	}

	public List<String> getFindings() {
		return findings;
	}

}
