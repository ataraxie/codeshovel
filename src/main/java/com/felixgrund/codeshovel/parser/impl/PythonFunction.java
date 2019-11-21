package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.antlr.python.AntlrPythonParser;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PythonFunction extends AbstractFunction<AntlrPythonParser.FuncdefContext> implements Yfunction {

    PythonFunction(AntlrPythonParser.FuncdefContext function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }
    
    private Yparameter getParameter(AntlrPythonParser.Def_parameterContext param) {
        String argumentName = param.named_parameter().name().getText();
        String argumentType = param.named_parameter().test() == null ? "" :
                param.named_parameter().test().getText();
        return new Yparameter(argumentName, argumentType);
    }
    
    private Map<String, String> getDefaultArguments(AntlrPythonParser.Def_parameterContext param) {
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
    protected String getInitialName(AntlrPythonParser.FuncdefContext function) {
        return function.name().getText();
    }

    @Override
    protected String getInitialType(AntlrPythonParser.FuncdefContext function) {
        return function.test() == null ? null : function.test().getText();
    }

    @Override
    protected Ymodifiers getInitialModifiers(AntlrPythonParser.FuncdefContext function) {
        List<String> modifiers = new ArrayList<>();
        if (function.ASYNC() != null) {
            modifiers.add("async");
        }
        return new Ymodifiers(modifiers);
    }

    @Override
    protected Yexceptions getInitialExceptions(AntlrPythonParser.FuncdefContext function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(AntlrPythonParser.FuncdefContext function) {
        List<Yparameter> parametersList = new ArrayList<>();
        if (function.typedargslist() != null && function.typedargslist().def_parameters(0) != null) {
            List<AntlrPythonParser.Def_parameterContext> l = function.typedargslist().def_parameters(0).def_parameter();
            for (AntlrPythonParser.Def_parameterContext p : l) {
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
    protected String getInitialBody(AntlrPythonParser.FuncdefContext function) {
        return function.suite().getText();
    }

    @Override
    protected int getInitialBeginLine(AntlrPythonParser.FuncdefContext function) {
        return function.getStart().getLine();
    }

    @Override
    protected int getInitialEndLine(AntlrPythonParser.FuncdefContext function) {
        return function.getStop().getLine() - 1;
    }

    @Override
    protected String getInitialParentName(AntlrPythonParser.FuncdefContext function) {
        return function.getParent().getClass().getSimpleName();
    }

    @Override
    protected String getInitialFunctionPath(AntlrPythonParser.FuncdefContext function) {
        return null;
    }
}
