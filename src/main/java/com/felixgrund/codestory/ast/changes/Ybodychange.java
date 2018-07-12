package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.util.Environment;

public class Ybodychange extends Ycomparefunctionchange {

	public Ybodychange(Environment startEnv, Yfunction matchedFunction, Yfunction compareFunction) {
		super(startEnv, matchedFunction, compareFunction);
	}

}
