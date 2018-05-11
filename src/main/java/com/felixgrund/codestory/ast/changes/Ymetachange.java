package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yfunction;

public class Ymetachange extends Ychange {

	protected Yfunction matchedFunction;
	protected Yfunction compareFunction;
	protected Ycommit compareCommit;

	public Ymetachange(Ycommit commit, Ycommit compareCommit, Yfunction matchedFunction, Yfunction compareFunction) {
		super(commit);
		this.matchedFunction = matchedFunction;
		this.compareFunction = compareFunction;
		this.compareCommit = compareCommit;
	}

	public Yfunction getCompareFunction() {
		return compareFunction;
	}

	public Yfunction getMatchedFunction() {
		return matchedFunction;
	}

	public Ycommit getCompareCommit() {
		return compareCommit;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + commit.getShortName() + ":" + compareFunction.getName() + ":" + compareFunction.getNameLineNumber() + ")";
	}
}
