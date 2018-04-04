package com.felixgrund.codestory.ast.entities;

import java.util.ArrayList;

public class YCollection extends ArrayList<YCommit> {

	private YCommit firstOccurrence;

	@Override
	public boolean add(YCommit YCommit) {
		if (YCommit.isFunctionFound()) {
			if (this.firstOccurrence != null) {
				this.firstOccurrence.setFirstFunctionOccurrence(false);
			}
			YCommit.setFirstFunctionOccurrence(true);
			this.firstOccurrence = YCommit;
		}
		return super.add(YCommit);
	}

	public YCommit getFirstFunctionOccurrence() {
		return firstOccurrence;
	}
}
