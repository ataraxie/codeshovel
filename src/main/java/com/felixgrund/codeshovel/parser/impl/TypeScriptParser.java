package com.felixgrund.codeshovel.parser.impl;

import com.eclipsesource.v8.V8Object;
import com.felixgrund.codeshovel.changes.Ybodychange;
import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.changes.Yparametermetachange;
import com.felixgrund.codeshovel.changes.Yreturntypechange;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.parser.AbstractParser;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.visitors.TypeScriptVisitor;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TypeScriptParser extends AbstractParser implements Yparser {

    public static final String ACCEPTED_FILE_EXTENSION = ".*\\.tsx?$";
    private Logger log = LoggerFactory.getLogger(TypeScriptParser.class);

    public TypeScriptParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
        super(startEnv, filePath, fileContent, commit);
    }

    @Override
    protected List<Yfunction> parseMethods() throws ParseException {
        try {
            TypeScriptMethodVisitor visitor = new TypeScriptMethodVisitor() {
                @Override
                public boolean methodMatches(Yfunction method) {
                    return method.getBody() != null;
                }
            };
            visitor.visit(this.filePath, this.fileContent);
            return visitor.getMatchedNodes();
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), this.filePath, this.fileContent);
        }
    }

    @Override
    public double getScopeSimilarity(Yfunction function, Yfunction compareFunction) {
        return Utl.parentNamesMatch(function, compareFunction) ? 1.0 : 0.0; // TODO
    }

    @Override
    public String getAcceptedFileExtension() {
        return ACCEPTED_FILE_EXTENSION;
    }

    @Override
    public List<Ychange> getMinorChanges(Ycommit commit, Yfunction compareFunction) {
        List<Ychange> changes = new ArrayList<>();

        Yparametermetachange yparametermetachange = getParametersMetaChange(commit, compareFunction);
        Yreturntypechange yreturntypechange = getReturnTypeChange(commit, compareFunction);
        Ybodychange ybodychange = getBodyChange(commit, compareFunction);

        if (yparametermetachange != null) {
            changes.add(yparametermetachange);
        }
        if (yreturntypechange != null) {
            changes.add(yreturntypechange);
        }
        if (ybodychange != null) {
            changes.add(ybodychange);
        }
        return changes;
    }

    private Yfunction transformMethod(V8Object function) {
        return new TypeScriptFunction(function, this.commit, this.filePath, this.fileContent);
    }

    private abstract class TypeScriptMethodVisitor extends TypeScriptVisitor {

        private List<Yfunction> matchedNodes = new ArrayList<>();

        public abstract boolean methodMatches(Yfunction method);

        @Override
        public void visitArrowFunction(V8Object arrowFunction) {
            Yfunction yfunction = transformMethod(arrowFunction);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            visitChildren(arrowFunction);
        }

        @Override
        public void visitConstructor(V8Object constructor) {
            Yfunction yfunction = transformMethod(constructor);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            visitChildren(constructor);
        }

        @Override
        public void visitFunctionDeclaration(V8Object function) {
            Yfunction yfunction = transformMethod(function);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            visitChildren(function);
        }

        @Override
        public void visitFunctionExpression(V8Object functionExpression) {
            Yfunction yfunction = transformMethod(functionExpression);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            visitChildren(functionExpression);
        }

        @Override
        public void visitMethodDeclaration(V8Object methodDeclaration) {
            Yfunction yfunction = transformMethod(methodDeclaration);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            visitChildren(methodDeclaration);
        }

        List<Yfunction> getMatchedNodes() {
            return matchedNodes;
        }
    }

}
