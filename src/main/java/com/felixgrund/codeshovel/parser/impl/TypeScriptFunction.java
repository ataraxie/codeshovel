package com.felixgrund.codeshovel.parser.impl;

import com.eclipsesource.v8.V8Array;
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

    private Yparameter getParameter(V8Object param) {
        String stype;
        V8Object v8type = param.getObject("type");
        if (v8type.isUndefined()) {
            stype = "";
        } else {
            stype = this.getSourceFileContent()
                    .substring(v8type.getInteger("pos"), v8type.getInteger("end"))
                    .trim();
        }
        return new Yparameter(param.getObject("name").getString("escapedText"), stype);
    }

    private String getModifier(V8Object modifier) {
        return this.getSourceFileContent()
                .substring(modifier.getInteger("pos"), modifier.getInteger("end"))
                .trim();
    }

    @Override
    protected String getInitialName(V8Object function) {
        // TODO remove magic number and use typescript
        if (function.getInteger("kind") == 162) {
            return "constructor";
        } else {
            return function.getObject("name").getString("escapedText");
        }
    }

    @Override
    protected String getInitialType(V8Object function) {
        V8Object type = function.getObject("type");
        if (type.isUndefined()) {
            return null;
        } else {
            return this.getSourceFileContent()
                    .substring(type.getInteger("pos"), type.getInteger("end"))
                    .trim();
        }
    }

    @Override
    protected Ymodifiers getInitialModifiers(V8Object function) {
        // export, async, static, private etc...
        List<String> ymodifiers = new ArrayList<>();
        V8Object maybeModifiers = function.getObject("modifiers");
        if (!maybeModifiers.isUndefined()) {
            V8Array modifiers = (V8Array) maybeModifiers;
            int length = modifiers.length();
            for (int i = 0; i < length; ++i) {
                ymodifiers.add(getModifier(modifiers.getObject(i)));
            }
        }
        return new Ymodifiers(ymodifiers);
    }

    @Override
    protected Yexceptions getInitialExceptions(V8Object function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(V8Object function) {
        List<Yparameter> yparameters = new ArrayList<>();
        V8Array params = function.getArray("parameters");
        int length = params.length();
        for (int i = 0; i < length; ++i) {
            yparameters.add(getParameter(params.getObject(i)));
        }
        return yparameters;
    }

    @Override
    protected String getInitialBody(V8Object function) {
        V8Object body = function.getObject("body");
        if (body.isUndefined()) {
            return null; // TODO is this correct?
        } else {
            return this.getSourceFileContent()
                    .substring(body.getInteger("pos"), body.getInteger("end"))
                    .trim();
        }
    }

    @Override
    protected int getInitialBeginLine(V8Object function) {
        return function.getInteger("startLine");
    }

    @Override
    protected int getInitialEndLine(V8Object function) {
        return function.getInteger("endLine");
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
