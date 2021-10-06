package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

public class Ydocchange extends  Ysignaturechange{

    public Ydocchange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
        super(startEnv, newFunction, oldFunction);
    }

    @Override
    protected Object getOldValue() {
        return oldFunction.getFunctionDoc();
    }

    @Override
    protected Object getNewValue() {
        return newFunction.getFunctionDoc();
    }
}
