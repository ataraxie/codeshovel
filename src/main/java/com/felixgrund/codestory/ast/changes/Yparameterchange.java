package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yfunction;

public class Yparameterchange extends Ymetachange {

	public Yparameterchange(Ycommit commit, Ycommit compareCommit, Yfunction matchedFunction, Yfunction compareFunction) {
		super(commit, compareCommit, matchedFunction, compareFunction);
	}

	@Override
	public String toString() {
		return "Yparameterchange(" + commit.getShortName() + ":" + compareFunction.getName() + ":" + compareFunction.getNameLineNumber() + ")";
	}
}
