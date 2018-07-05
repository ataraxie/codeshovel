package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yfunction;

public class Yrename extends Ysignaturechange {

	public Yrename(Yfunction matchedFunction, Yfunction compareFunction) {
		super(matchedFunction, compareFunction);
	}
}
