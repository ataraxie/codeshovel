package com.felixgrund.codestory.ast.util;

import com.felixgrund.codestory.ast.exceptions.NoParserFoundException;
import com.felixgrund.codestory.ast.exceptions.ParseException;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.parser.impl.JavaParser;
import com.felixgrund.codestory.ast.parser.impl.JsParser;

public class ParserFactory {

	public static Yparser getParser(String repoName, String filePath, String fileContent, String commitName) throws NoParserFoundException, ParseException {
		if (filePath.endsWith(JsParser.ACCEPTED_FILE_EXTENSION)) {
			return new JsParser(repoName, filePath, fileContent, commitName);
		} else if (filePath.endsWith(JavaParser.ACCEPTED_FILE_EXTENSION)) {
			return new JavaParser(repoName, filePath, fileContent, commitName);
		} else {
			throw new NoParserFoundException("No parser found for filename " + filePath);
		}
	}

}
