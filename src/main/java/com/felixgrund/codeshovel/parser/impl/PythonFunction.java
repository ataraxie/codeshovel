package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
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

public class PythonFunction extends AbstractFunction<MethodDeclaration> implements Yfunction {

    PythonFunction(MethodDeclaration method, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(method, commit, sourceFilePath, sourceFileContent);
    }

    private Map<String,String> createParameterMetadataMap(Parameter parameterElement) {
        return new HashMap<>();
    }

    private String createParameterModifiersString(Parameter parameterElement) {
        return null;
    }

    private String createParameterAnnotationsString(Parameter parameterElement) {
        return null;
    }

    @Override
    protected String getInitialName(MethodDeclaration method) {
        return method.getNameAsString();
    }

    @Override
    protected String getInitialType(MethodDeclaration method) {
        return method.getTypeAsString();
    }

    @Override
    protected Ymodifiers getInitialModifiers(MethodDeclaration method) {
        return null;
    }

    @Override
    protected Yexceptions getInitialExceptions(MethodDeclaration method) {
        return null;
    }

    @Override
    protected List<Yparameter> getInitialParameters(MethodDeclaration method) {
        return new ArrayList<>();
    }

    @Override
    protected String getInitialBody(MethodDeclaration method) {
        return null;
    }

    @Override
    protected int getInitialBeginLine(MethodDeclaration method) {
        return 0;
    }

    @Override
    protected int getInitialEndLine(MethodDeclaration method) {
        return 0;
    }

    @Override
    protected String getInitialParentName(MethodDeclaration method) {
        return null;
    }

    @Override
    protected String getInitialFunctionPath(MethodDeclaration method) {
        return null;
    }
}
