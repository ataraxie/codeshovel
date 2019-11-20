package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.antlr.python.AntlrPythonParser;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.List;

public class PythonFunction extends AbstractFunction<AntlrPythonParser.FuncdefContext> implements Yfunction {
    //            TODO replace with AbstractFunction<Python3Parser.ParserRuleContext> to support async_funcdef

    PythonFunction(AntlrPythonParser.FuncdefContext function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }

    @Override
    protected String getInitialName(AntlrPythonParser.FuncdefContext function) {
        return function.name().getText();
    }

    @Override
    protected String getInitialType(AntlrPythonParser.FuncdefContext function) {
        return null;
        // TODO consider type hints
    }

    @Override
    protected Ymodifiers getInitialModifiers(AntlrPythonParser.FuncdefContext function) {
        return null;
        // TODO consider async
    }

    @Override
    protected Yexceptions getInitialExceptions(AntlrPythonParser.FuncdefContext function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(AntlrPythonParser.FuncdefContext function) {
        List<Yparameter> parametersList = new ArrayList<>();
        if (function.typedargslist() != null) {
            List<AntlrPythonParser.Def_parametersContext> l = function.typedargslist().def_parameters();
            for (AntlrPythonParser.Def_parametersContext t : l) {
                // TODO consider default parameters
                Yparameter parameter = new Yparameter(t.getText(), "");
                parametersList.add(parameter);
            }
            // TODO consider type hints
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
        return function.getStop().getLine();
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
