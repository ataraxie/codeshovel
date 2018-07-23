package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.wrappers.RevCommit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

public abstract class Ychange {

	protected RevCommit commit;
	protected StartEnvironment startEnv;
	protected RepositoryService repositoryService;

	public Ychange(StartEnvironment startEnv, RevCommit commit) {
		this.startEnv = startEnv;
		this.commit = commit;
		this.repositoryService = startEnv.getRepositoryService();
	}

	public RevCommit getCommit() {
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
