package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.antlr.python3.Python3Parser;
import com.felixgrund.codeshovel.util.Utl;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ReferenceType;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PythonFunction extends AbstractFunction<Python3Parser.FuncdefContext> implements Yfunction {

    PythonFunction(Python3Parser.FuncdefContext function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }

    @Override
    protected String getInitialName(Python3Parser.FuncdefContext function) {
        return function.NAME().getText();
    }

    @Override
    protected String getInitialType(Python3Parser.FuncdefContext function) {
        return null;
        // TODO consider type hints
    }

    @Override
    protected Ymodifiers getInitialModifiers(Python3Parser.FuncdefContext function) {
        return null;
    }

    @Override
    protected Yexceptions getInitialExceptions(Python3Parser.FuncdefContext function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(Python3Parser.FuncdefContext function) {
        List<Yparameter> parametersList = new ArrayList<>();
        if (function.parameters().typedargslist() != null) {
            List<Python3Parser.TfpdefContext> l = function.parameters().typedargslist().tfpdef();
            for (Python3Parser.TfpdefContext t : l) {
                // TODO consider default parameters
                Yparameter parameter = new Yparameter(t.getText(), "");
                parametersList.add(parameter);
            }
            // TODO consider type hints
        }
        return parametersList;
    }

    @Override
    protected String getInitialBody(Python3Parser.FuncdefContext function) {
        return function.suite().getText();
    }

    @Override
    protected int getInitialBeginLine(Python3Parser.FuncdefContext function) {
        return function.getStart().getLine();
    }

    @Override
    protected int getInitialEndLine(Python3Parser.FuncdefContext function) {
        return function.getStop().getLine();
    }

    @Override
    protected String getInitialParentName(Python3Parser.FuncdefContext function) {
        return function.getParent().getClass().getSimpleName();
    }

    @Override
    protected String getInitialFunctionPath(Python3Parser.FuncdefContext function) {
        return null;
    }
}
