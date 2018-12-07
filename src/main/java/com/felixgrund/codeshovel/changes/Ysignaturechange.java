package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;

public abstract class Ysignaturechange extends Ycomparefunctionchange {

	public Ysignaturechange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	protected abstract Object getOldValue();
	protected abstract Object getNewValue();

	@Override
	public JsonObject getExtendedDetailsJsonObject() {
		JsonObject extendedObj = new JsonObject();
		String oldValue = "";
		String newValue = "";
		if (getOldValue() != null) {
			oldValue = getOldValue().toString();
		}
		if (getNewValue() != null) {
			newValue = getNewValue().toString();
		}
		extendedObj.addProperty("oldValue", oldValue);
		extendedObj.addProperty("newValue", newValue);
		return extendedObj;
	}
}
