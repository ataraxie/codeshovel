package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.parser.AbstractParser;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.parser.antlr.python3.Python3BaseVisitor;
import com.felixgrund.codeshovel.parser.antlr.python3.Python3Lexer;
import com.felixgrund.codeshovel.parser.antlr.python3.Python3Parser;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.github.javaparser.ast.body.MethodDeclaration;
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

    public static final String ACCEPTED_FILE_EXTENSION = ".py";
    private Logger log = LoggerFactory.getLogger(PythonParser.class);

    public PythonParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
        super(startEnv, filePath, fileContent, commit);
    }

    @Override
    protected List<Yfunction> parseMethods() throws ParseException {
        CharStream input = CharStreams.fromString(this.fileContent);
        Python3Lexer lexer = new Python3Lexer(input);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokenStream);
        ParseTree tree = parser.file_input();
        PythonMethodVisitor visitor = new PythonMethodVisitor() {
            @Override
            public boolean methodMatches(Yfunction method) {
                return method.getBody() != null;
            }
        };
        visitor.visit(tree);
        return visitor.getMatchedNodes();
    }

    @Override
    public double getScopeSimilarity(Yfunction function, Yfunction compareFunction) {
        return 0;
    }

    @Override
    public String getAcceptedFileExtension() {
        return ACCEPTED_FILE_EXTENSION;
    }

    @Override
    public List<Ychange> getMinorChanges(Ycommit commit, Yfunction compareFunction) {
        return new ArrayList<>();
    }

    private Yfunction transformMethod(Python3Parser.FuncdefContext function) {
        return new PythonFunction(function, this.commit, this.filePath, this.fileContent);
    }

    private abstract class PythonMethodVisitor extends Python3BaseVisitor<Void> {

        private List<Yfunction> matchedNodes = new ArrayList<>();

        public abstract boolean methodMatches(Yfunction method);
        
        // @Override
        // public Void visitAsync_funcdef(Python3Parser.Async_funcdefContext ctx) {
        //     return null; // TODO
        // }

        @Override
        public Void visitFuncdef(Python3Parser.FuncdefContext function) {
            Yfunction yfunction = transformMethod(function);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
            return null;
        }

        List<Yfunction> getMatchedNodes() {
            return matchedNodes;
        }
    }

}
