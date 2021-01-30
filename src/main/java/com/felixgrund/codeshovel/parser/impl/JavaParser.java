package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.parser.AbstractParser;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JavaParser extends AbstractParser implements Yparser {

    public static final String ACCEPTED_FILE_EXTENSION = ".java";
    private Logger log = LoggerFactory.getLogger(JavaParser.class);

    public JavaParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
        super(startEnv, filePath, fileContent, commit);
    }

    @Override
    protected List<Yfunction> parseMethods() throws ParseException {
        try {
//			CompilationUnit rootCompilationUnit = com.github.javaparser.JavaParser.parse(this.fileContent);
//			if (rootCompilationUnit == null) {
//				throw new ParseException("Could not parseMethods root compilation unit.", this.filePath, this.fileContent);
//			}
            CompilationUnit rootCompilationUnit = null;
            try {
                rootCompilationUnit = com.github.javaparser.JavaParser.parse(this.fileContent);
            } catch (Exception e) {
                return new ArrayList<>();
            }
            JavaMethodVisitor visitor = new JavaMethodVisitor() {
                @Override
                public boolean methodMatches(Yfunction method) {
                    return method.getBody() != null;
                }
            };
            rootCompilationUnit.accept(visitor, null);
            return visitor.getMatchedNodes();
        } catch (Exception e) {
            System.err.println("JavaParser::parseMethods() - parse error. path: " + this.filePath + "; content:\n" + this.fileContent);
            throw new ParseException("Error during parsing of content", this.filePath, this.fileContent);
        }
    }

    @Override
    public double getScopeSimilarity(Yfunction function, Yfunction compareFunction) {
        return Utl.parentNamesMatch(function, compareFunction) ? 1.0 : 0.0;
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
        Yexceptionschange yexceptionschange = getExceptionsChange(commit, compareFunction);
        Ybodychange ybodychange = getBodyChange(commit, compareFunction);
        Yparametermetachange yparametermetachange = getParametersMetaChange(commit, compareFunction);
        if (yreturntypechange != null) {
            changes.add(yreturntypechange);
        }
        if (ymodifierchange != null) {
            changes.add(ymodifierchange);
        }
        if (yexceptionschange != null) {
            changes.add(yexceptionschange);
        }
        if (ybodychange != null) {
            changes.add(ybodychange);
        }
        if (yparametermetachange != null) {
            changes.add(yparametermetachange);
        }
        return changes;
    }


    private Yfunction transformMethod(MethodDeclaration method) {
        return new JavaFunction(method, this.commit, this.filePath, this.fileContent);
    }

    public abstract class JavaMethodVisitor extends VoidVisitorAdapter<Void> {

        private List<Yfunction> matchedNodes = new ArrayList<>();

        public abstract boolean methodMatches(Yfunction method);

        @Override
        public void visit(MethodDeclaration method, Void arg) {
            super.visit(method, arg);
            boolean hasBody = method.getBody().isPresent();
            Yfunction yfunction = transformMethod(method);
            if (hasBody && methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
        }

        public List<Yfunction> getMatchedNodes() {
            return matchedNodes;
        }
    }

}
