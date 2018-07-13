package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.wrappers.Environment;

public class Ymovefromfile extends Ycrossfilechange {

	public Ymovefromfile(Environment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

}
