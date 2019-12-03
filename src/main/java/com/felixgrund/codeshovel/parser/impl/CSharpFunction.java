package com.felixgrund.codeshovel.parser.impl;

import CSharpParseTree.CSharpParser;
import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class CSharpFunction extends AbstractFunction<CSharpParser.Method_declarationContext> implements Yfunction {

    CSharpFunction(CSharpParser.Method_declarationContext function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }

    @Override
    protected String getInitialName(CSharpParser.Method_declarationContext function) {
        return function.method_member_name().getText();
    }

    @Override
    protected String getInitialType(CSharpParser.Method_declarationContext function) {
        ParserRuleContext parent = function.getParent();
        if (parent instanceof CSharpParser.Common_member_declarationContext) {
            return "void";
        } else {
            CSharpParser.Typed_member_declarationContext tmd = (CSharpParser.Typed_member_declarationContext) parent;
            return tmd.type_().getText();
        }
    }

    @Override
    protected Ymodifiers getInitialModifiers(CSharpParser.Method_declarationContext function) {
        return new Ymodifiers(new ArrayList<>());
    }

    @Override
    protected Yexceptions getInitialExceptions(CSharpParser.Method_declarationContext function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(CSharpParser.Method_declarationContext function) {
        return new ArrayList<>();
    }

    @Override
    protected String getInitialBody(CSharpParser.Method_declarationContext function) {
        return function.method_body().getText();
    }

    @Override
    protected int getInitialBeginLine(CSharpParser.Method_declarationContext function) {
        return function.getStart().getLine();
    }

    @Override
    protected int getInitialEndLine(CSharpParser.Method_declarationContext function) {
        return function.getStop().getLine() - 1;
    }

    @Override
    protected String getInitialParentName(CSharpParser.Method_declarationContext function) {
        RuleContext parent = function.getParent();
        while (parent != null) {
            if (parent instanceof CSharpParser.Method_declarationContext) {
                return ""; // ((CSharpParser.Method_declarationContext) parent).name().getText();
            } else {
                parent = parent.getParent();
            }
        }
        return "";
    }

    @Override
    protected String getInitialFunctionPath(CSharpParser.Method_declarationContext function) {
        return null;
    }
}
