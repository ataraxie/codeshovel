package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

public class Yformatchange extends Ycomparefunctionchange {
    public Yformatchange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
        super(startEnv, newFunction, oldFunction);
    }
}
