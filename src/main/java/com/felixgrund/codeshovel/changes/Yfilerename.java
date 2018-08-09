package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

public class Yfilerename extends Ycrossfilechange {

	public Yfilerename(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	public JsonObject getExtendedDetailsJsonObject() {
		JsonObject extendedObj = new JsonObject();
		String oldPath = oldFunction.getSourceFilePath();
		String newPath = newFunction.getSourceFilePath();
		extendedObj.addProperty("oldPath", oldPath);
		extendedObj.addProperty("newPath", newPath);
		return extendedObj;
	}
}
