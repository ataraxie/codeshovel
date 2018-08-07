package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

public abstract class Ychange {

	protected Commit commit;
	protected StartEnvironment startEnv;
	protected RepositoryService repositoryService;

	public Ychange(StartEnvironment startEnv, Commit commit) {
		this.startEnv = startEnv;
		this.commit = commit;
		this.repositoryService = startEnv.getRepositoryService();
	}

	public Commit getCommit() {
		return commit;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public String getTypeAsString() {
		return getClass().getSimpleName();
	}

}
