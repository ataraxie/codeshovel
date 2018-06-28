package com.felixgrund.codestory.ast.util;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

public final class StartCommitRevFilter extends RevFilter {

	private RevCommit startCommit;

	public StartCommitRevFilter(RevCommit startCommit) {
		this.startCommit = startCommit;
	}

	@Override
	public boolean include(final RevWalk walker, final RevCommit commit) {
		return commit.getCommitTime() <= this.startCommit.getCommitTime();
	}

	@Override
	public RevFilter clone() {
		return this;
	}

	@Override
	public boolean requiresCommitBody() {
		return false;
	}

	@Override
	public String toString() {
		return "START_COMMIT";
	}
}
