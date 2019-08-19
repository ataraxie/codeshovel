package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;

import java.io.IOException;

public class Yintroduced extends Ychange {

	private Yfunction newFunction;

	protected String diffString;

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

	@Override
	public JsonObject toJsonObject() {
		JsonObject obj = super.toJsonObject();
		obj.addProperty("diff", getDiffAsString());
		return obj;
	}

	private String getDiffAsString() {
		if (this.diffString == null) {
			try {
				this.diffString = this.getDiffAsString("", newFunction.getSourceFragment());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return diffString;
	}
}
