package com.felixgrund.codestory.ast.changes;

public class Yinfilerename extends Ychange {

	private String oldName;
	private String newName;

	public Yinfilerename(String oldName, String newName) {
		this.oldName = oldName;
		this.newName = newName;
	}

	public String getOldName() {
		return oldName;
	}

	public String getNewName() {
		return newName;
	}
}
