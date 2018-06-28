package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yfunction;

public class Yexceptionschange extends Ysignaturechange {

	public Yexceptionschange(Yfunction matchedFunction, Yfunction compareFunction) {
		super(matchedFunction, compareFunction);
	}

}
