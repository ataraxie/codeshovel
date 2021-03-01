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
import org.jrubyparser.CompatVersion;
import org.jrubyparser.Parser;
import org.jrubyparser.ast.DefnNode;
import org.jrubyparser.ast.DefsNode;
import org.jrubyparser.ast.MethodDefNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.parser.ParserConfiguration;
import org.jrubyparser.util.NoopVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class RubyParser extends AbstractParser implements Yparser {

    public static final String ACCEPTED_FILE_EXTENSION = ".*\\.rb$";
    private Logger log = LoggerFactory.getLogger(JavaParser.class);

    public RubyParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
        super(startEnv, filePath, fileContent, commit);
    }

    @Override
    protected List<Yfunction> parseMethods() throws ParseException {
        try {
            Parser rubyParser = new Parser();
            StringReader in = new StringReader(this.fileContent);
            CompatVersion version = CompatVersion.RUBY2_0;
            ParserConfiguration config = new ParserConfiguration(0, version);
            Node rootNode = rubyParser.parse(this.filePath, in, config);
            RubyMethodVisitor visitor =  new RubyMethodVisitor() {
                @Override
                public boolean methodMatches(Yfunction method) {
                    return method.getBody() != null;
                }
            };
            rootNode.accept(visitor);
            return visitor.getMatchedNodes();
        } catch (Exception e) {
            System.err.println("JavaParser::parseMethods() - parse error. path: " + this.filePath + "; content:\n" + this.fileContent);
            throw new ParseException("Error during parsing of content", this.filePath, this.fileContent);
        }
    }

    @Override
    public double getScopeSimilarity(Yfunction function, Yfunction compareFunction) {
        // TODO
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
        Ybodychange ybodychange = getBodyChange(commit, compareFunction);

        // TODO this currently does nothing. Parameter metadata is not collected
        Yparametermetachange yparametermetachange = getParametersMetaChange(commit, compareFunction);
        if (yreturntypechange != null) {
            changes.add(yreturntypechange);
        }
        if (ymodifierchange != null) {
            changes.add(ymodifierchange);
        }
        if (ybodychange != null) {
            changes.add(ybodychange);
        }
        if (yparametermetachange != null) {
            changes.add(yparametermetachange);
        }
        return changes;
    }


    private Yfunction transformMethod(MethodDefNode method) {
        return new RubyFunction(method, this.commit, this.filePath, this.fileContent);
    }

    public abstract class RubyMethodVisitor extends NoopVisitor {

        private List<Yfunction> matchedNodes = new ArrayList<>();

        public abstract boolean methodMatches(Yfunction method);

        private void addMethodIfMatches(MethodDefNode method) {
            Yfunction yfunction = transformMethod(method);
            if (methodMatches(yfunction)) {
                matchedNodes.add(yfunction);
            }
        }

        @Override
        public Object visitDefnNode(DefnNode defnNode) {
            addMethodIfMatches(defnNode);
            return this.visit(defnNode);
        }

        @Override
        public Object visitDefsNode(DefsNode defsNode) {
            addMethodIfMatches(defsNode);
            return this.visit(defsNode);
        }

        public List<Yfunction> getMatchedNodes() {
            return matchedNodes;
        }
    }

}
