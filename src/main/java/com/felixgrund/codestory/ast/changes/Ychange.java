package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;

public abstract class Ychange {

	protected Ycommit commit;

	public Ychange(Ycommit commit) {
		this.commit = commit;
	}

	public Ycommit getCommit() {
		return commit;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
