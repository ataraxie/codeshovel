package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

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
