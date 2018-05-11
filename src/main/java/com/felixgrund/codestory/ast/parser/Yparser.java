package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.exceptions.ParseException;

import java.util.List;

public interface Yparser {

	Yfunction findFunctionByNameAndLine(String name, int line);
	List<Yfunction> findFunctionsByLineRange(int beginLine, int endLine);
	List<Yfunction> getAllFunctions();
	List<Yfunction> findFunctionsByOtherFunction(Yfunction otherFunction);
	Object parse() throws ParseException;
	boolean functionNamesConsideredEqual(String aName, String bName);

}
