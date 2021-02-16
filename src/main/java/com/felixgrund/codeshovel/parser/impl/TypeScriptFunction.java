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
            stype = v8type.executeStringFunction("getText", new V8Array(v8type.getRuntime()));
        }
        return new Yparameter(param.getObject("name").getString("escapedText").trim(), stype);
    }

    private String getModifier(V8Object modifier) {
        return modifier.executeStringFunction("getText", new V8Array(modifier.getRuntime()));
    }

    @Override
    protected String getInitialName(V8Object function) {
        /**
         * TODO this magic number of 162 is _extremely_ brittle
         * this line should read `if (new TypeScriptVisitor().isKind(function, "Constructor")) {`
         * however it causes extreme performance degradation
         */
        if (function.getInteger("kind") == 162) {
            return "constructor";
        } else {
            return function.getObject("name").getString("escapedText");
        }
    }

    @Override
    protected String getInitialType(V8Object function) {
        String stype;
        V8Object v8type = function.getObject("type");
        if (v8type.isUndefined()) {
            stype = null;
        } else {
            stype = v8type.executeStringFunction("getText", new V8Array(v8type.getRuntime()));
        }
        return stype;
    }

    @Override
    protected Ymodifiers getInitialModifiers(V8Object function) {
        List<String> ymodifiers = new ArrayList<>();
        V8Object maybeModifiers = function.getObject("modifiers");
        if (!maybeModifiers.isUndefined()) {
            V8Array modifiers = (V8Array) maybeModifiers;
            int length = modifiers.length();
            for (int i = 0; i < length; ++i) {
                V8Object mod = modifiers.getObject(i);
                ymodifiers.add(getModifier(mod));
            }
        }
        return new Ymodifiers(ymodifiers);
    }

    @Override
    protected Yexceptions getInitialExceptions(V8Object function) {
        return Yexceptions.NONE;
    }

    @Override
    protected List<Yparameter> getInitialParameters(V8Object function) {
        List<Yparameter> yparameters = new ArrayList<>();
        V8Array params = function.getArray("parameters");
        int length = params.length();
        for (int i = 0; i < length; ++i) {
            V8Object param = params.getObject(i);
            yparameters.add(getParameter(param));
        }
        return yparameters;
    }

    @Override
    protected String getInitialBody(V8Object function) {
        String sbody;
        V8Object v8body = function.getObject("body");
        if (v8body.isUndefined()) {
            sbody = null;
        } else {
            sbody = v8body.executeStringFunction("getText", new V8Array(v8body.getRuntime()));
        }
        return sbody;
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
        String sparent = "";
        V8Object v8current = function.getObject("parent");
        while (!v8current.isUndefined()) {
            if (v8current.contains("name")) {
                sparent = v8current.getObject("name").getString("escapedText");
                break;
            } else {
                V8Object v8last = v8current;
                v8current = v8current.getObject("parent");
            }
        }
        return sparent;
    }

    @Override
    protected String getInitialFunctionPath(V8Object function) {
        StringBuilder path = new StringBuilder();
        V8Object v8current = function.getObject("parent");
        while (!v8current.isUndefined()) {
            if (v8current.contains("name")) {
                path.insert(0, "/");
                path.insert(0, v8current.getObject("name").getString("escapedText"));
            }
            V8Object v8last = v8current;
            v8current = v8current.getObject("parent");
        }
        return path.toString();
    }
}
