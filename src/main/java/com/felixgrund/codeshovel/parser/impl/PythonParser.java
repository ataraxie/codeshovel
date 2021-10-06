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
import PythonParseTree.PythonLexer;
import PythonParseTree.PythonParserBaseVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PythonParser extends AbstractParser implements Yparser {

    public static final String ACCEPTED_FILE_EXTENSION = ".*\\.py$";
    private Logger log = LoggerFactory.getLogger(PythonParser.class);

    public PythonParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
        super(startEnv, filePath, fileContent, commit);
    }

    @Override
    protected List<Yfunction> parseMethods() throws ParseException {
        try {
            CharStream input = CharStreams.fromString(this.fileContent);
            PythonLexer lexer = new PythonLexer(input);
            TokenStream tokenStream = new CommonTokenStream(lexer);
            PythonParseTree.PythonParser parser = new PythonParseTree.PythonParser(tokenStream);
            ParseTree tree = parser.file_input();
            PythonMethodVisitor visitor = new PythonMethodVisitor() {
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

        Yparametermetachange yparametermetachange = getParametersMetaChange(commit, compareFunction);
        Yreturntypechange yreturntypechange = getReturnTypeChange(commit, compareFunction);
        Ybodychange ybodychange = getBodyChange(commit, compareFunction);
        Ymodifierchange ymodifierchange = getModifiersChange(commit, compareFunction);
        Yannotationchange yannotationchange = getAnnotationChange(commit, compareFunction);
        // TODO handle Ydocchange

        if (yparametermetachange != null) {
            changes.add(yparametermetachange);
        }
        if (yreturntypechange != null) {
            changes.add(yreturntypechange);
        }
        if (ybodychange != null) {
            changes.add(ybodychange);
        }
        if (ymodifierchange != null) {
            changes.add(ymodifierchange);
        }
        if (yannotationchange != null) {
            changes.add(yannotationchange);
        }
        return changes;
    }

    private Yfunction transformMethod(PythonParseTree.PythonParser.FuncdefContext function) {
        return new PythonFunction(function, this.commit, this.filePath, this.fileContent);
    }

    private abstract class PythonMethodVisitor extends PythonParserBaseVisitor<Void> {

        private List<Yfunction> matchedNodes = new ArrayList<>();

        public abstract boolean methodMatches(Yfunction method);

        @Override
        public Void visitFuncdef(PythonParseTree.PythonParser.FuncdefContext function) {
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
