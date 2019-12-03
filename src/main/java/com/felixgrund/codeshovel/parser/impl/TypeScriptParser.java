package com.felixgrund.codeshovel.parser.impl;

import TypeScriptParseTree.TypeScriptLexer;
import TypeScriptParseTree.TypeScriptParserBaseVisitor;
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
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
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
            CharStream input = CharStreams.fromString(this.fileContent);
            TypeScriptLexer lexer = new TypeScriptLexer(input);
            TokenStream tokenStream = new CommonTokenStream(lexer);
            TypeScriptParseTree.TypeScriptParser parser = new TypeScriptParseTree.TypeScriptParser(tokenStream);
            ParseTree tree = parser.program();
            TypeScriptMethodVisitor visitor = new TypeScriptMethodVisitor() {
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

    private Yfunction transformMethod(ParserRuleContext function) {
        return new TypeScriptFunction(function, this.commit, this.filePath, this.fileContent);
    }

    private abstract class TypeScriptMethodVisitor extends TypeScriptParserBaseVisitor<Void> {

        private List<Yfunction> matchedNodes = new ArrayList<>();

        public abstract boolean methodMatches(Yfunction method);

        @Override
        public Void visitFunctionDeclaration(TypeScriptParseTree.TypeScriptParser.FunctionDeclarationContext function) {
            Yfunction yfunction = transformMethod(function);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            return visitChildren(function);
        }
        
        @Override
        public Void visitPropertyMemberDeclaration(TypeScriptParseTree.TypeScriptParser.PropertyMemberDeclarationContext method) {
            Yfunction yfunction = transformMethod(method);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            return visitChildren(method);
        }

        List<Yfunction> getMatchedNodes() {
            return matchedNodes;
        }
    }

}
