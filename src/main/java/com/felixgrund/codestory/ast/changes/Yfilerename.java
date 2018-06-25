package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yfunction;

public class Yfilerename extends Ycomparefunctionchange {

	public Yfilerename(Ycommit commit, Ycommit compareCommit, Yfunction matchedFunction, Yfunction compareFunction) {
		super(commit, compareCommit, matchedFunction, compareFunction);
	}
	
}
