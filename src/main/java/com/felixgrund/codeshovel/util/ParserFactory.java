package com.felixgrund.codeshovel.util;

import com.felixgrund.codeshovel.exceptions.NoParserFoundException;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.parser.impl.JavaParser;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class ParserFactory {

	private static Map<String, Yparser> parserCache = new HashMap<>();

	public static Yparser getParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws NoParserFoundException, ParseException {
		String cacheKey = commit.getName() + "-" + DigestUtils.md5Hex(filePath);
		Yparser parser = parserCache.get(cacheKey);
		if (parser == null) {
			if (filePath.endsWith(JavaParser.ACCEPTED_FILE_EXTENSION)) {
				parser = new JavaParser(startEnv, filePath, fileContent, commit);
			} else {
				throw new NoParserFoundException("No parser found for filename " + filePath);
			}
		}

		parserCache.put(cacheKey, parser);

		return parser;
	}

}
