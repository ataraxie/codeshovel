package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.wrappers.StartEnvironment;

public class Yintroduced extends Ychange {

	private Yfunction newFunction;

	public Yintroduced(StartEnvironment startEnv, Yfunction newFunction) {
		super(startEnv, newFunction.getCommitName());
		this.newFunction = newFunction;
	}

	@Override
	public String toString() {
		String template = "%s(%s:%s:%s)";
		return String.format(template,
				getClass().getSimpleName(),
				newFunction.getCommitNameShort(),
				newFunction.getName(),
				newFunction.getNameLineNumber()
		);
	}
}
