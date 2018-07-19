package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

public class Yintroduced extends Ychange {

	private Yfunction newFunction;

	public Yintroduced(StartEnvironment startEnv, Yfunction newFunction) {
		super(startEnv, newFunction.getCommit());
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
