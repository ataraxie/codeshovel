package com.felixgrund.codeshovel.parser.impl;

import CSharpParseTree.CSharpLexer;
import CSharpParseTree.CSharpParserBaseVisitor;
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
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CSharpParser extends AbstractParser implements Yparser {

    public static final String ACCEPTED_FILE_EXTENSION = ".*\\.cs$";
    private Logger log = LoggerFactory.getLogger(CSharpParser.class);

    public CSharpParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
        super(startEnv, filePath, fileContent, commit);
    }

    @Override
    protected List<Yfunction> parseMethods() throws ParseException {
        try {
            // TODO https://gist.github.com/KvanTTT/d95579de257531a3cc15
            CharStream input = CharStreams.fromString(this.fileContent);
            CSharpLexer lexer = new CSharpLexer(input);
            TokenStream tokenStream = new CommonTokenStream(lexer);
            CSharpParseTree.CSharpParser parser = new CSharpParseTree.CSharpParser(tokenStream);
            ParseTree tree = parser.compilation_unit();
            CSharpMethodVisitor visitor = new CSharpMethodVisitor() {
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

    private Yfunction transformMethod(CSharpParseTree.CSharpParser.Method_declarationContext function) {
        return new CSharpFunction(function, this.commit, this.filePath, this.fileContent);
    }

    private abstract class CSharpMethodVisitor extends CSharpParserBaseVisitor<Void> {

        private List<Yfunction> matchedNodes = new ArrayList<>();

        public abstract boolean methodMatches(Yfunction method);

        @Override
        public Void visitMethod_declaration(CSharpParseTree.CSharpParser.Method_declarationContext function) {
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
