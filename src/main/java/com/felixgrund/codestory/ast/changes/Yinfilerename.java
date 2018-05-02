package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yfunction;

public class Yinfilerename extends Ymetachange {

	public Yinfilerename(Ycommit commit, Ycommit compareCommit, Yfunction matchedFunction, Yfunction compareFunction) {
		super(commit, compareCommit, matchedFunction, compareFunction);
	}
}
