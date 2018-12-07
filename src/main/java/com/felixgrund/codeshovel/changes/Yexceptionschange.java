package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;

public class Yexceptionschange extends Ysignaturechange {

	public Yexceptionschange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	protected Object getOldValue() {
		return oldFunction.getExceptions();
	}

	@Override
	protected Object getNewValue() {
		return newFunction.getExceptions();
	}


}
