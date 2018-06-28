package com.felixgrund.codestory.ast.changes;

public abstract class Ychange {

	protected String commitName;

	public Ychange(String commitName) {
		this.commitName = commitName;
	}

	public String getCommitName() {
		return commitName;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
