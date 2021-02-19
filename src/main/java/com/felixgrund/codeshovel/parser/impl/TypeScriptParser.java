package com.felixgrund.codeshovel.parser.impl;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.felixgrund.codeshovel.changes.*;
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

    public static final String ACCEPTED_FILE_EXTENSION = ".*\\.(t|j)sx?$";
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
            visitor.visit(this.fileContent);
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
        Yreturntypechange yreturntypechange = getReturnTypeChange(commit, compareFunction);
        Ymodifierchange ymodifierchange = getModifiersChange(commit, compareFunction);
        Ybodychange ybodychange = getBodyChange(commit, compareFunction);
        if (yreturntypechange != null) {
            changes.add(yreturntypechange);
        }
        if (ymodifierchange != null) {
            changes.add(ymodifierchange);
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

        private V8Object sourceFile = null;

        @Override
        public void visit(String source) {
            sourceFile = getSource(source);
            visit(sourceFile);
            sourceFile = null;
            clear();
        }

        @Override
        public void visitArrowFunction(V8Object arrowFunction) {
            // TODO handle this function with no name
            visitChildren(arrowFunction);
        }

        @Override
        public void visitConstructor(V8Object constructor) {
            addStartAndEndLines(constructor);
            Yfunction yfunction = transformMethod(constructor);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            visitChildren(constructor);
        }

        @Override
        public void visitFunctionDeclaration(V8Object function) {
            addStartAndEndLines(function);
            Yfunction yfunction = transformMethod(function);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            visitChildren(function);
        }

        @Override
        public void visitFunctionExpression(V8Object functionExpression) {
            // TODO handle this function with potentially no name
            visitChildren(functionExpression);
        }

        @Override
        public void visitMethodDeclaration(V8Object methodDeclaration) {
            addStartAndEndLines(methodDeclaration);
            Yfunction yfunction = transformMethod(methodDeclaration);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            visitChildren(methodDeclaration);
        }

        List<Yfunction> getMatchedNodes() {
            return matchedNodes;
        }

        private void addStartAndEndLines(V8Object node) {
            V8 runtime = ts.getRuntime();

            int start = node.executeIntegerFunction("getStart", new V8Array(runtime));
            V8Array parameters = new V8Array(runtime).push(start);
            V8Object startLineAndCharacterOfPosition = sourceFile.executeObjectFunction("getLineAndCharacterOfPosition", parameters);
            parameters.release();

            int end = node.executeIntegerFunction("getEnd", new V8Array(runtime));
            parameters = new V8Array(runtime).push(end);
            V8Object endLineAndCharacterOfPosition = sourceFile.executeObjectFunction("getLineAndCharacterOfPosition", parameters);
            parameters.release();

            int startLine = startLineAndCharacterOfPosition.getInteger("line");
            int endLine = endLineAndCharacterOfPosition.getInteger("line");

            startLineAndCharacterOfPosition.release();
            endLineAndCharacterOfPosition.release();
            // Add one because lines are zero indexed in ts
            node.add("startLine", startLine + 1);
            node.add("endLine", endLine + 1);
        }
    }

}
