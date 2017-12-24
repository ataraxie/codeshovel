package com.felixgrund.codestory.ast.entities;

import java.util.ArrayList;

public class CommitInfoCollection extends ArrayList<CommitInfo> {

	private CommitInfo firstOccurrence;

	@Override
	public boolean add(CommitInfo commitInfo) {
		if (commitInfo.isFunctionFound()) {
			if (this.firstOccurrence != null) {
				this.firstOccurrence.setFirstFunctionOccurrence(false);
			}
			commitInfo.setFirstFunctionOccurrence(true);
			this.firstOccurrence = commitInfo;
		}
		return super.add(commitInfo);
	}

	public CommitInfo getFirstFunctionOccurrence() {
		return firstOccurrence;
	}
}
