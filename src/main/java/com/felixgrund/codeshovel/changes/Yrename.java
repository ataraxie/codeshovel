package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

public class Yrename extends Ysignaturechange {

	public Yrename(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	public JsonObject getExtendedDetailsJsonObject() {
		JsonObject extendedObj = new JsonObject();
		String oldMethodName = oldFunction.getName();
		String newMethodName = newFunction.getName();
		extendedObj.addProperty("oldMethodName", oldMethodName);
		extendedObj.addProperty("newMethodName", newMethodName);
		return extendedObj;
	}


}
