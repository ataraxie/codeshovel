package com.felixgrund.codestory.ast.util;

import com.felixgrund.codestory.ast.exceptions.NoParserFoundException;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.parser.impl.JavaParser;
import com.felixgrund.codestory.ast.parser.impl.JsParser;

public class ParserFactory {

	public static Yparser getParser(String repoName, String fileName, String fileContent, String commitName) throws NoParserFoundException {
		if (fileName.endsWith(".js")) {
			return new JsParser(repoName, fileName, fileContent, commitName);
		} else if (fileName.endsWith(".java")) {
			return new JavaParser(repoName, fileName, fileContent, commitName);
		} else {
			throw new NoParserFoundException("No parser found for filename " + fileName);
		}
	}

}
