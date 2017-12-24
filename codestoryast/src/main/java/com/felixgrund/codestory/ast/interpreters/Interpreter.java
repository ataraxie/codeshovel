package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.entities.CommitInfo;
import org.eclipse.jgit.lib.PersonIdent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Interpreter {

	private List<String> findings;

	private CommitInfo commitInfo;

	public Interpreter(CommitInfo commitInfo) {
		this.findings = new ArrayList<>();
		this.commitInfo = commitInfo;
	}

	public void interpret() {
		if (commitInfo.isFirstFunctionOccurrence()) {
			findings.add("Function was first introduced");
		}
		if (isFunctionNotFoundAnymore()) {
			findings.add("Function was removed");
		}
		if (isFunctionFoundAgain()) {
			findings.add("Function was added");
		}
	}

	private boolean isFunctionNotFoundAnymore() {
		return !commitInfo.isFunctionFound()
				&& commitInfo.getPrev() != null
				&& commitInfo.getPrev().isFunctionFound();
	}

	private boolean isFunctionFoundAgain() {
		return !commitInfo.isFirstFunctionOccurrence()
				&& commitInfo.isFunctionFound()
				&& commitInfo.getPrev() != null
				&& !commitInfo.getPrev().isFunctionFound();
	}

	public List<String> getFindings() {
		return findings;
	}

}
