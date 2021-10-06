package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.Commit;
import PythonParseTree.PythonParser;
import PythonParseTree.PythonParserBaseVisitor;
import org.antlr.v4.runtime.RuleContext;
import org.apache.commons.lang3.StringUtils;

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
                return visitParameterImpl(parameter, metadata);
            }
            @Override public Void visitNamed_parameter(PythonParser.Named_parameterContext ctx) {
                Yparameter parameter = getParameter(ctx);
                // not possible to have arguments here
                return visitParameterImpl(parameter, null);
            }

            private Void visitParameterImpl(Yparameter parameter, Map<String, String> metadata) {
                if (metadata != null) {
                    parameter.setMetadata(metadata);
                }
                parametersList.add(parameter);
                return null;
            }
        }.visit(function);
        return parametersList;
    }

    @Override
    protected String getInitialBody(PythonParseTree.PythonParser.FuncdefContext function) {
        // This does not include any whitespace
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
        RuleContext parent = function.getParent();
        while (parent != null) {
            if (parent instanceof PythonParseTree.PythonParser.FuncdefContext) {
                return ((PythonParseTree.PythonParser.FuncdefContext) parent).name().getText();
            } else if (parent instanceof PythonParseTree.PythonParser.ClassdefContext) {
                return ((PythonParseTree.PythonParser.ClassdefContext) parent).name().getText();
            } else {
                parent = parent.getParent();
            }
        }
        return "";
    }

    @Override
    protected String getInitialFunctionPath(PythonParseTree.PythonParser.FuncdefContext function) {
        return null;
    }

    @Override
    protected String getInitialAnnotation(PythonParser.FuncdefContext rawMethod) {
        List<String> decoratorList = new ArrayList<>();
        RuleContext parent = rawMethod.getParent();
        if (parent instanceof PythonParser.Class_or_func_def_stmtContext) {
            List<PythonParser.DecoratorContext> decoratorContexts =
                    ((PythonParser.Class_or_func_def_stmtContext) parent).decorator();
            for (PythonParser.DecoratorContext decoratorContext : decoratorContexts) {
                decoratorList.add(decoratorContext.getText());
            }
        }
        return StringUtils.join(decoratorList, ",");
    }

    @Override
    protected String getInitialDoc(PythonParser.FuncdefContext rawMethod) {
        // TODO: implement function for python
        return null;
    }

    @Override
    protected String getInitialUnformattedBody(PythonParser.FuncdefContext rawMethod) {
        // TODO: implement function for python
        return null;
    }

    @Override
    protected String getInitialSourceFragment(PythonParseTree.PythonParser.FuncdefContext function) {
        // Note: This will not work on lambdas
        int startWithDecorators;
        RuleContext parent = function.getParent();
        if (parent instanceof PythonParseTree.PythonParser.Class_or_func_def_stmtContext) {
            startWithDecorators = ((PythonParseTree.PythonParser.Class_or_func_def_stmtContext) parent).getStart().getLine();
        } else {
            startWithDecorators = getNameLineNumber();
        }
        String source = getSourceFileContent() + "\n<EOF>";
        // TODO remove trailing whitespace lines
        return Utl.getTextFragment(source, startWithDecorators, getEndLineNumber());
    }
}
