package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.Commit;
import PythonParseTree.PythonParser;
import PythonParseTree.PythonParserBaseVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PythonFunction extends AbstractFunction<PythonParser.FuncdefContext> implements Yfunction {

    PythonFunction(PythonParseTree.PythonParser.FuncdefContext function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }
    
    private Yparameter getParameter(PythonParser.Def_parameterContext param) {
        if (param.named_parameter() != null) {
            String argumentName = param.named_parameter().name().getText();
            String argumentType = param.named_parameter().test() == null ? "" :
                    param.named_parameter().test().getText();
            return new Yparameter(argumentName, argumentType);
        } else {
            // TODO is `_` included in this case?
            return new Yparameter(param.getText(), "");
        }
    }

    private Yparameter getParameter(PythonParser.Named_parameterContext param) {
        String argumentName = "";
        if (param.getParent().getChild(0).getText().contains("*")) {
            // OK this looks a little shaky but is meant to include *args and **kwargs
            argumentName += param.getParent().getChild(0).getText();
        }
        argumentName += param.name().getText();
        String argumentType = param.test() == null ? "" : param.test().getText();
        return new Yparameter(argumentName, argumentType);
    }
    
    private Map<String, String> getDefaultArguments(PythonParser.Def_parameterContext param) {
        return param.test() == null ? null : getDefaultArguments(param.test().getText());
    }

    private Map<String, String> getDefaultArguments(PythonParser.Named_parameterContext param) {
        // return param.test() == null ? null : getDefaultArguments(param.test().getText());
        // TODO is this possible?
        return null;
    }
    
    private Map<String, String> getDefaultArguments(String defaultArgument) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("default", defaultArgument);
        return metadata;
    }

    @Override
    protected String getInitialName(PythonParseTree.PythonParser.FuncdefContext function) {
        return function.name().getText();
    }

    @Override
    protected String getInitialType(PythonParseTree.PythonParser.FuncdefContext function) {
        return function.test() == null ? null : function.test().getText();
    }

    @Override
    protected Ymodifiers getInitialModifiers(PythonParseTree.PythonParser.FuncdefContext function) {
        List<String> modifiers = new ArrayList<>();
        if (function.ASYNC() != null) {
            modifiers.add("async");
        }
        return new Ymodifiers(modifiers);
    }

    @Override
    protected Yexceptions getInitialExceptions(PythonParseTree.PythonParser.FuncdefContext function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(PythonParseTree.PythonParser.FuncdefContext function) {
        List<Yparameter> parametersList = new ArrayList<>();
        new PythonParserBaseVisitor<Void>() {
            @Override public Void visitDef_parameter(PythonParser.Def_parameterContext ctx) {
                Yparameter parameter = getParameter(ctx);
                Map<String, String> metadata = getDefaultArguments(ctx);
                if (metadata != null) {
                    parameter.setMetadata(metadata);
                }
                parametersList.add(parameter);
                return null;
            }
            @Override public Void visitNamed_parameter(PythonParser.Named_parameterContext ctx) {
                Yparameter parameter = getParameter(ctx);
                Map<String, String> metadata = getDefaultArguments(ctx);
                if (metadata != null) {
                    parameter.setMetadata(metadata);
                }
                parametersList.add(parameter);
                return null;
            }
        }.visit(function);
//        if (function.typedargslist() != null && function.typedargslist().def_parameters(0) != null) {
//            List<ext.antlr.python.PythonParser.Def_parameterContext> l = function.typedargslist().def_parameters(0).def_parameter();
//            for (ext.antlr.python.PythonParser.Def_parameterContext p : l) {
//                Yparameter parameter = getParameter(p);
//                Map<String, String> metadata = getDefaultArguments(p);
//                if (metadata != null) {
//                    parameter.setMetadata(metadata);
//                }
//                parametersList.add(parameter);
//            }
//        }
        return parametersList;
    }

    @Override
    protected String getInitialBody(PythonParseTree.PythonParser.FuncdefContext function) {
        return function.suite().getText();
    }

    @Override
    protected int getInitialBeginLine(PythonParseTree.PythonParser.FuncdefContext function) {
        return function.getStart().getLine();
    }

    @Override
    protected int getInitialEndLine(PythonParseTree.PythonParser.FuncdefContext function) {
        return function.getStop().getLine() - 1;
    }

    @Override
    protected String getInitialParentName(PythonParseTree.PythonParser.FuncdefContext function) {
        return function.getParent().getClass().getSimpleName();
    }

    @Override
    protected String getInitialFunctionPath(PythonParseTree.PythonParser.FuncdefContext function) {
        return null;
    }
}
