package com.felixgrund.codestory.ast.util;

import com.felixgrund.codestory.ast.json.JsonSimilarity;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.wrappers.FunctionSimilarity;
import jdk.nashorn.internal.ir.FunctionNode;

public class SimilarityUtil {

	public static int getLineNumberDistance(Yfunction aFunction, Yfunction bFunction) {
		return Math.abs(aFunction.getNameLineNumber() - bFunction.getNameLineNumber());
	}

	public static double getLineNumberSimilarity(Yfunction aFunction, Yfunction bFunction) {
		String aFileSource = aFunction.getSourceFileContent();
		int aNumLines = Utl.countLineNumbers(aFileSource);
		String bFileSource = bFunction.getSourceFileContent();
		int bNumLines = Utl.countLineNumbers(bFileSource);
		double maxLines = Math.max(aNumLines, bNumLines);
		double lineNumberDistance = SimilarityUtil.getLineNumberDistance(aFunction, bFunction);
		double similarity = (maxLines - lineNumberDistance) / maxLines;
		return similarity;
	}


}
