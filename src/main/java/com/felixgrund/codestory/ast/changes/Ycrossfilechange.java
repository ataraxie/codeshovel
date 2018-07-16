package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.wrappers.StartEnvironment;

public abstract class Ycrossfilechange extends Ycomparefunctionchange {

	public Ycrossfilechange(StartEnvironment startEnv, Yfunction matchedFunction, Yfunction compareFunction) {
		super(startEnv, matchedFunction, compareFunction);
	}

	@Override
	public String toString() {
		return super.toString() + "[" + oldFunction.getSourceFilePath() + "]";
	}

}
