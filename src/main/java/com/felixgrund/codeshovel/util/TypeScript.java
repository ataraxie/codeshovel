package com.felixgrund.codeshovel.util;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TypeScript {
    private static final String TYPESCRIPT_PATH = "node_modules/typescript/lib/typescript.js";
    private static TypeScript instance;
    private final NodeJS nodeJS;
    private static V8Object ts;

    private static Logger log = LoggerFactory.getLogger(TypeScript.class);

    public static TypeScript getInstance() {
        if (instance == null) {
            try {
                instance = new TypeScript();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("MAJOR ERROR - Could not init TypeScript");
            }

        }
        return instance;
    }

    public TypeScript() throws IOException {
        // TODO there must be a better way to do this
        InputStream inputStream = TypeScript.class.getClassLoader().getResourceAsStream(TYPESCRIPT_PATH);
        File file = File.createTempFile("typescript", null);
        file.deleteOnExit();
        FileUtils.copyInputStreamToFile(inputStream, file);
        nodeJS = NodeJS.createNodeJS();
        ts = nodeJS.require(file);
    }

    public V8Object getTS() {
        return ts;
    }

    public V8 getRuntime() {
        return nodeJS.getRuntime();
    }

    public int getLine(V8Object node, V8Object sourceFile) {
        V8Object ts = TypeScript.getInstance().getTS();
        int start = node.executeIntegerFunction("getStart", new V8Array(ts.getRuntime()));
        V8Object lineAndCharacter = sourceFile
                .executeObjectFunction("getLineAndCharacterOfPosition", new V8Array(ts.getRuntime()).push(start));
        return lineAndCharacter.getInteger("line");
    }

    public String getNodeText(V8Object node) {
        V8Object ts = TypeScript.getInstance().getTS();
        V8Array arguments = new V8Array(ts.getRuntime());
        String text = node.executeStringFunction("getText", arguments);
        arguments.release();
        return text;
    }
}
