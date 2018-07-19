package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.wrappers.CommitWrap;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;

public abstract class Ychange {

	protected RevCommit commit;
	protected StartEnvironment startEnv;
	protected RepositoryService repositoryService;
	protected CommitWrap commitWrap;

	public Ychange(StartEnvironment startEnv, RevCommit commit) {
		this.startEnv = startEnv;
		this.commit = commit;
		this.commitWrap = new CommitWrap(commit);
		this.repositoryService = startEnv.getRepositoryService();
	}

	public CommitWrap getCommitWrap() {
		return commitWrap;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public String getTypeAsString() {
		return getClass().getSimpleName();
	}

}
