package com.felixgrund.codeshovel.parser.impl;

import TypeScriptParseTree.TypeScriptParser;
import TypeScriptParseTree.TypeScriptParserBaseVisitor;
import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.util.ArrayList;
import java.util.List;

public class TypeScriptFunction extends AbstractFunction<ParserRuleContext> implements Yfunction {
    private static InitialNameVisitor initialNameVisitor = new InitialNameVisitor();
    private static InitialTypeVisitor initialTypeVisitor = new InitialTypeVisitor();
    private static InitialBodyVisitor initialBodyVisitor = new InitialBodyVisitor();

    TypeScriptFunction(ParserRuleContext function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }

    @Override
    protected String getInitialName(ParserRuleContext function) {
        return function.accept(initialNameVisitor);
    }

    @Override
    protected String getInitialType(ParserRuleContext function) {
        return function.accept(initialTypeVisitor);
    }

    @Override
    protected Ymodifiers getInitialModifiers(ParserRuleContext function) {
        List<String> modifiers = new ArrayList<>();
        return new Ymodifiers(modifiers);
    }

    @Override
    protected Yexceptions getInitialExceptions(ParserRuleContext function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(ParserRuleContext function) {
        return new ArrayList<>();
    }

    @Override
    protected String getInitialBody(ParserRuleContext function) {
        return function.accept(initialBodyVisitor);
    }

    @Override
    protected int getInitialBeginLine(ParserRuleContext function) {
        return function.getStart().getLine();
    }

    @Override
    protected int getInitialEndLine(ParserRuleContext function) {
        return function.getStop().getLine() - 1;
    }

    @Override
    protected String getInitialParentName(ParserRuleContext function) {
        RuleContext parent = function.getParent();
        while (parent != null) {
            if (parent instanceof TypeScriptParser.FunctionDeclarationContext) {
                return ((TypeScriptParser.FunctionDeclarationContext) parent).Identifier().getText();
            } else if (parent instanceof  TypeScriptParser.ClassDeclarationContext) {
                return ((TypeScriptParser.ClassDeclarationContext) parent).Identifier().getText();
            } else {
                parent = parent.getParent();
            }
            // TODO this doesn't account for the many ways a method can be nested
        }
        return "";
    }

    @Override
    protected String getInitialFunctionPath(ParserRuleContext function) {
        return null;
    }

    private static class InitialNameVisitor extends TypeScriptParserBaseVisitor<String> {
        @Override
        public String visitPropertyMemberDeclaration(TypeScriptParser.PropertyMemberDeclarationContext method) {
            return "";
        }

        @Override
        public String visitFunctionDeclaration(TypeScriptParseTree.TypeScriptParser.FunctionDeclarationContext function) {
            return function.Identifier().getText();
        }
    }
    
    private static class InitialTypeVisitor extends TypeScriptParserBaseVisitor<String> {
        @Override
        public String visitPropertyMemberDeclaration(TypeScriptParser.PropertyMemberDeclarationContext method) {
            return "";
        }

        @Override
        public String visitFunctionDeclaration(TypeScriptParseTree.TypeScriptParser.FunctionDeclarationContext function) {
            TypeScriptParser.TypeAnnotationContext ta = function.callSignature().typeAnnotation();
            if (ta != null) {
                return ta.type_().getText();
            } else {
                return "";
            }
        }
    }

    private static class InitialBodyVisitor extends TypeScriptParserBaseVisitor<String> {
        @Override
        public String visitPropertyMemberDeclaration(TypeScriptParser.PropertyMemberDeclarationContext method) {
            return "";
        }

        @Override
        public String visitFunctionDeclaration(TypeScriptParseTree.TypeScriptParser.FunctionDeclarationContext function) {
            return function.functionBody().getText();
        }
    }
}
