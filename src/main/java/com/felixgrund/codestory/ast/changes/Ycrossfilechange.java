package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.parser.Yfunction;

public class Ycrossfilechange extends Ycomparefunctionchange {

	public Ycrossfilechange(Yfunction matchedFunction, Yfunction compareFunction) {
		super(matchedFunction, compareFunction);
	}
}
