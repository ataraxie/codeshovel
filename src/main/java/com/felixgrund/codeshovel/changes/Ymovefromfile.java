package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

public class Ymovefromfile extends Ycrossfilechange {

	public Ymovefromfile(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	public JsonObject getExtendedDetailsJsonObject() {
		JsonObject extendedObj = super.getExtendedDetailsJsonObject();
		String oldPath = oldFunction.getSourceFilePath();
		String newPath = newFunction.getSourceFilePath();
		String oldMethodName = oldFunction.getName();
		String newMethodName = newFunction.getName();
		extendedObj.addProperty("oldPath", oldPath);
		extendedObj.addProperty("newPath", newPath);
		extendedObj.addProperty("oldMethodName", oldMethodName);
		extendedObj.addProperty("newMethodName", newMethodName);
		return extendedObj;
	}

}
