package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

public class Ybodychange extends Ycomparefunctionchange {

	public Ybodychange(StartEnvironment startEnv, Yfunction matchedFunction, Yfunction compareFunction) {
		super(startEnv, matchedFunction, compareFunction);
	}

}
