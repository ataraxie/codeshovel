package com.felixgrund.codestory.ast.entities;

import org.eclipse.jgit.diff.EditList;

import java.util.Map;

public class Ydiff {

	private EditList inFileEdits;
	private Map<String, EditList> otherFileEdits;

	public Ydiff(EditList inFileEdits, Map<String,EditList> otherFileEdits) {
		this.inFileEdits = inFileEdits;
		this.otherFileEdits = otherFileEdits;
	}

	public EditList getInFileEdits() {
		return inFileEdits;
	}

	public Map<String, EditList> getOtherFileEdits() {
		return otherFileEdits;
	}
}
