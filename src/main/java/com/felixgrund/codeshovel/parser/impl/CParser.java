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
import ext.antlr.c.CBaseVisitor;
import ext.antlr.c.CLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CParser extends AbstractParser implements Yparser {

    public static final String ACCEPTED_FILE_EXTENSION = ".*\\.(c|h)$";
    private Logger log = LoggerFactory.getLogger(CParser.class);

    public CParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
        super(startEnv, filePath, fileContent, commit);
    }

    @Override
    protected List<Yfunction> parseMethods() throws ParseException {
        try {
            CharStream input = CharStreams.fromString(this.fileContent);
            CLexer lexer = new CLexer(input);
            TokenStream tokenStream = new CommonTokenStream(lexer);
            ext.antlr.c.CParser parser = new ext.antlr.c.CParser(tokenStream);
            ParseTree tree = parser.translationUnit();
            CMethodVisitor visitor = new CMethodVisitor() {
                @Override
                public boolean methodMatches(Yfunction method) {
                    return method.getBody() != null;
                }
            };
            visitor.visit(tree);
            return visitor.getMatchedNodes();
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), this.filePath, this.fileContent);
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

    private Yfunction transformMethod(ext.antlr.c.CParser.FunctionDefinitionContext function) {
        return new CFunction(function, this.commit, this.filePath, this.fileContent);
    }

    private abstract class CMethodVisitor extends CBaseVisitor<Void> {

        private List<Yfunction> matchedNodes = new ArrayList<>();

        public abstract boolean methodMatches(Yfunction method);

        @Override
        public Void visitFunctionDefinition(ext.antlr.c.CParser.FunctionDefinitionContext function) {
            Yfunction yfunction = transformMethod(function);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            return visitChildren(function);
        }

        List<Yfunction> getMatchedNodes() {
            return matchedNodes;
        }
    }

}
