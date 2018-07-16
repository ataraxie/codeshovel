package com.felixgrund.codestory.ast.util;

import com.felixgrund.codestory.ast.exceptions.NoParserFoundException;
import com.felixgrund.codestory.ast.exceptions.ParseException;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.parser.impl.JavaParser;
import com.felixgrund.codestory.ast.parser.impl.JsParser;
import com.felixgrund.codestory.ast.wrappers.StartEnvironment;
import org.eclipse.jgit.revwalk.RevCommit;

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
