package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.services.RepositoryService;
import com.felixgrund.codestory.ast.wrappers.StartEnvironment;

public abstract class Ychange {

	protected String commitName;
	protected StartEnvironment startEnv;
	protected RepositoryService repositoryService;

	public Ychange(StartEnvironment startEnv, String commitName) {
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

	public String getTypeAsString() {
		return getClass().getSimpleName();
	}

}
