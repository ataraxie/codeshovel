package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.Commit;
import ext.antlr.python.PythonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PythonFunction extends AbstractFunction<PythonParser.FuncdefContext> implements Yfunction {

    PythonFunction(ext.antlr.python.PythonParser.FuncdefContext function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }
    
    private Yparameter getParameter(ext.antlr.python.PythonParser.Def_parameterContext param) {
        if (param.named_parameter() != null) {
            String argumentName = param.named_parameter().name().getText();
            String argumentType = param.named_parameter().test() == null ? "" :
                    param.named_parameter().test().getText();
            return new Yparameter(argumentName, argumentType);
        } else {
            // TODO is this possible? def func(*:str): or def func(_:str):
            return new Yparameter(param.getText(), "");
        }
    }
    
    private Map<String, String> getDefaultArguments(ext.antlr.python.PythonParser.Def_parameterContext param) {
        if (param.test() != null) {
            Map<String, String> metadata = new HashMap<>();
            String defaultArgument = param.test().getText();
            metadata.put("default", defaultArgument);
            return metadata;
        } else {
            return null;
        }
    }

    @Override
    protected String getInitialName(ext.antlr.python.PythonParser.FuncdefContext function) {
        return function.name().getText();
    }

    @Override
    protected String getInitialType(ext.antlr.python.PythonParser.FuncdefContext function) {
        return function.test() == null ? null : function.test().getText();
    }

    @Override
    protected Ymodifiers getInitialModifiers(ext.antlr.python.PythonParser.FuncdefContext function) {
        List<String> modifiers = new ArrayList<>();
        if (function.ASYNC() != null) {
            modifiers.add("async");
        }
        return new Ymodifiers(modifiers);
    }

    @Override
    protected Yexceptions getInitialExceptions(ext.antlr.python.PythonParser.FuncdefContext function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(ext.antlr.python.PythonParser.FuncdefContext function) {
        List<Yparameter> parametersList = new ArrayList<>();
        if (function.typedargslist() != null && function.typedargslist().def_parameters(0) != null) {
            List<ext.antlr.python.PythonParser.Def_parameterContext> l = function.typedargslist().def_parameters(0).def_parameter();
            for (ext.antlr.python.PythonParser.Def_parameterContext p : l) {
                Yparameter parameter = getParameter(p);
                Map<String, String> metadata = getDefaultArguments(p);
                if (metadata != null) {
                    parameter.setMetadata(metadata);
                }
                parametersList.add(parameter);
            }
        }
        return parametersList;
    }

    @Override
    protected String getInitialBody(ext.antlr.python.PythonParser.FuncdefContext function) {
        return function.suite().getText();
    }

    @Override
    protected int getInitialBeginLine(ext.antlr.python.PythonParser.FuncdefContext function) {
        return function.getStart().getLine();
    }

    @Override
    protected int getInitialEndLine(ext.antlr.python.PythonParser.FuncdefContext function) {
        return function.getStop().getLine() - 1;
    }

    @Override
    protected String getInitialParentName(ext.antlr.python.PythonParser.FuncdefContext function) {
        return function.getParent().getClass().getSimpleName();
    }

    @Override
    protected String getInitialFunctionPath(ext.antlr.python.PythonParser.FuncdefContext function) {
        return null;
    }
}
