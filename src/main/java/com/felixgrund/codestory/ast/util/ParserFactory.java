package com.felixgrund.codestory.ast.util;

import com.felixgrund.codestory.ast.exceptions.NoParserFoundException;
import com.felixgrund.codestory.ast.exceptions.ParseException;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.parser.impl.JavaParser;
import com.felixgrund.codestory.ast.parser.impl.JsParser;
import org.eclipse.jgit.lib.Repository;

public class ParserFactory {

	public static Yparser getParser(Environment startEnv, String filePath, String fileContent, String commitName) throws NoParserFoundException, ParseException {
		if (filePath.endsWith(JsParser.ACCEPTED_FILE_EXTENSION)) {
			return new JsParser(startEnv, filePath, fileContent, commitName);
		} else if (filePath.endsWith(JavaParser.ACCEPTED_FILE_EXTENSION)) {
			return new JavaParser(startEnv, filePath, fileContent, commitName);
		} else {
			throw new NoParserFoundException("No parser found for filename " + filePath);
		}
	}

}
