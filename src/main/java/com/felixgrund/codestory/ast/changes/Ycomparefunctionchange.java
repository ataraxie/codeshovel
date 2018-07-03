package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.parser.Yfunction;

public class Ycomparefunctionchange extends Ychange {

	protected Yfunction matchedFunction;
	protected Yfunction compareFunction;

	public Ycomparefunctionchange(Yfunction matchedFunction, Yfunction compareFunction) {
		super(matchedFunction.getCommitName());
		this.matchedFunction = matchedFunction;
		this.compareFunction = compareFunction;
	}

	public Yfunction getCompareFunction() {
		return compareFunction;
	}

	public Yfunction getMatchedFunction() {
		return matchedFunction;
	}

	public String getCompareCommitName() {
		return compareFunction.getCommitName();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + compareFunction.getCommitName() + ":" + compareFunction.getSourceFilePath() + ":" + compareFunction.getName() + ":" + compareFunction.getNameLineNumber() + ")";
	}

}
