package com.felixgrund.codeshovel.parser.impl;

import com.eclipsesource.v8.V8Object;
import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.List;

public class TypeScriptFunction extends AbstractFunction<V8Object> implements Yfunction {

    TypeScriptFunction(V8Object function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }

    @Override
    protected String getInitialName(V8Object function) {
        return "";
    }

    @Override
    protected String getInitialType(V8Object function) {
        return "";
    }

    @Override
    protected Ymodifiers getInitialModifiers(V8Object function) {
        List<String> modifiers = new ArrayList<>();
        return new Ymodifiers(modifiers);
    }

    @Override
    protected Yexceptions getInitialExceptions(V8Object function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(V8Object function) {
        return new ArrayList<>();
    }

    @Override
    protected String getInitialBody(V8Object function) {
        return "";
    }

    @Override
    protected int getInitialBeginLine(V8Object function) {
        return 0;
    }

    @Override
    protected int getInitialEndLine(V8Object function) {
        return 0;
    }

    @Override
    protected String getInitialParentName(V8Object function) {
        return "";
    }

    @Override
    protected String getInitialFunctionPath(V8Object function) {
        return null;
    }
}
