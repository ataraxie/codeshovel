package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.util.Environment;

public abstract class Ychange {

	protected String commitName;
	protected Environment startEnv;

	public Ychange(Environment startEnv, String commitName) {
		this.startEnv = startEnv;
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
