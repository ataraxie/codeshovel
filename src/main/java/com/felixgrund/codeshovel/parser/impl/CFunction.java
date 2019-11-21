package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.antlr.c.AntlrCParser;
import com.felixgrund.codeshovel.parser.antlr.c.CBaseVisitor;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFunction extends AbstractFunction<AntlrCParser.FunctionDefinitionContext> implements Yfunction {

    CFunction(AntlrCParser.FunctionDefinitionContext function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }
    
    private static class typeVisitor extends CBaseVisitor<String> {
        @Override public String visitTypeSpecifier(AntlrCParser.TypeSpecifierContext type) {
            return type.getText();
        }
        @Override protected String aggregateResult(String aggregate, String nextResult) {
            // Returns the first type it finds (which should be the function type)
            return aggregate == null ? nextResult : aggregate;
        }
    }
    
    private Map<String, String> buildParameterMetaData(AntlrCParser.ParameterDeclarationContext dec) {
        Map<String, String> metadata = new HashMap<>();
        List<String> modifiers = new ArrayList<>();
        new CBaseVisitor<Void>(){
            @Override public Void visitDeclarationSpecifiers(AntlrCParser.DeclarationSpecifiersContext spec) {
                modifiers.add(spec.getText());
                return null;
            }
        }.visit(dec);
        if (modifiers.size() > 0) {
            metadata.put("modifiers", StringUtils.join(modifiers, "-"));
        }
        return metadata;
    }

    @Override
    protected String getInitialName(AntlrCParser.FunctionDefinitionContext function) {
        return function.declarator().directDeclarator().directDeclarator().getText();
    }

    @Override
    protected String getInitialType(AntlrCParser.FunctionDefinitionContext function) {
        String type = new typeVisitor().visit(function);
        if (function.declarator().pointer() != null) {
            type = type + " *";
        }
        return type;
    }

    @Override
    protected Ymodifiers getInitialModifiers(AntlrCParser.FunctionDefinitionContext function) {
        List<String> modifiers = new ArrayList<>();
        new CBaseVisitor<Void>(){
            @Override public Void visitStorageClassSpecifier(AntlrCParser.StorageClassSpecifierContext spec) {
                modifiers.add(spec.getText());
                return null;
            }
        }.visit(function);
        return new Ymodifiers(modifiers);
    }

    @Override
    protected Yexceptions getInitialExceptions(AntlrCParser.FunctionDefinitionContext function) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(AntlrCParser.FunctionDefinitionContext function) {
        List<Yparameter> parametersList = new ArrayList<>();
        new CBaseVisitor<Void>(){
            @Override public Void visitParameterDeclaration(AntlrCParser.ParameterDeclarationContext dec) {
                Yparameter yparameter = new Yparameter(dec.declarator().getText(), new typeVisitor().visit(dec));
                Map<String, String> metadata = buildParameterMetaData(dec);
                yparameter.setMetadata(metadata);
                parametersList.add(yparameter);
                return null;
            }
        }.visit(function);
        return parametersList;
    }

    @Override
    protected String getInitialBody(AntlrCParser.FunctionDefinitionContext function) {
        return function.compoundStatement() == null ? null : function.compoundStatement().getText();
    }

    @Override
    protected int getInitialBeginLine(AntlrCParser.FunctionDefinitionContext function) {
        return function.declarator().getStart().getLine();
    }

    @Override
    protected int getInitialEndLine(AntlrCParser.FunctionDefinitionContext function) {
        return function.getStop().getLine() - 1;
    }

    @Override
    protected String getInitialParentName(AntlrCParser.FunctionDefinitionContext function) {
        return function.getParent().getClass().getSimpleName();
        // TODO this is the node type name, not the actual parent name. Is this alright?
    }

    @Override
    protected String getInitialFunctionPath(AntlrCParser.FunctionDefinitionContext function) {
        return null;
    }
}
