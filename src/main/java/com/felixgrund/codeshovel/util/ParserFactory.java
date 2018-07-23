package com.felixgrund.codeshovel.util;

import com.felixgrund.codeshovel.exceptions.NoParserFoundException;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.parser.impl.JavaParser;
import com.felixgrund.codeshovel.parser.impl.JsParser;
import com.felixgrund.codeshovel.wrappers.RevCommit;

public class ParserFactory {

	public static Yparser getParser(StartEnvironment startEnv, String filePath, String fileContent, RevCommit commit) throws NoParserFoundException, ParseException {
		if (filePath.endsWith(JsParser.ACCEPTED_FILE_EXTENSION)) {
			return new JsParser(startEnv, filePath, fileContent, commit);
		} else if (filePath.endsWith(JavaParser.ACCEPTED_FILE_EXTENSION)) {
			return new JavaParser(startEnv, filePath, fileContent, commit);
		} else {
			throw new NoParserFoundException("No parser found for filename " + filePath);
		}
	}

}
