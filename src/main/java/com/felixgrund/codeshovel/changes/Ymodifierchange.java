package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

public class Ymodifierchange extends Ysignaturechange {

	public Ymodifierchange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	protected Object getOldValue() {
		return oldFunction.getModifiers();
	}

	@Override
	protected Object getNewValue() {
		return newFunction.getModifiers();
	}


}
