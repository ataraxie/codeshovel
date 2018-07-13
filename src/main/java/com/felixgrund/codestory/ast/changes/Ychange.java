package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.services.RepositoryService;
import com.felixgrund.codestory.ast.wrappers.Environment;

public abstract class Ychange {

	protected String commitName;
	protected Environment startEnv;
	protected RepositoryService repositoryService;

	public Ychange(Environment startEnv, String commitName) {
		this.startEnv = startEnv;
		this.commitName = commitName;
		this.repositoryService = startEnv.getRepositoryService();
	}

	public String getCommitName() {
		return commitName;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
