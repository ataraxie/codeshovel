package com.felixgrund.codestory.ast.entities;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;

import java.util.List;

public class Ydiff {

	private DiffEntry diffEntry;
	private EditList editList;
	private DiffFormatter formatter;

	public Ydiff(DiffEntry diffEntry, EditList editList, DiffFormatter formatter) {
		this.diffEntry = diffEntry;
		this.editList = editList;
		this.formatter = formatter;
	}

	public DiffFormatter getFormatter() {
		return formatter;
	}

	public DiffEntry getDiffEntry() {
		return diffEntry;
	}

	public EditList getEditList() {
		return editList;
	}

}
