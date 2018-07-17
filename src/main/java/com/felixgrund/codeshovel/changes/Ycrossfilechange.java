package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

public abstract class Ycrossfilechange extends Ycomparefunctionchange {

	public Ycrossfilechange(StartEnvironment startEnv, Yfunction matchedFunction, Yfunction compareFunction) {
		super(startEnv, matchedFunction, compareFunction);
	}

	@Override
	public String toString() {
		return super.toString() + "[" + oldFunction.getSourceFilePath() + "]";
	}

}
