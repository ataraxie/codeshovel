package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yfunction;

public class Yreturntypechange extends Ysignaturechange {

	public Yreturntypechange(Yfunction matchedFunction, Yfunction compareFunction) {
		super(matchedFunction, compareFunction);
	}

}
