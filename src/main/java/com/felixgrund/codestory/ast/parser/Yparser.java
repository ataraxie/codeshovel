package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.entities.Yfunction;
import com.felixgrund.codestory.ast.entities.Yrange;
import com.felixgrund.codestory.ast.exceptions.ParseException;

import java.util.List;

public interface Yparser {

	Yfunction findFunctionByNameAndLine(String name, int line);
	List<Yfunction> findFunctionsByLineRange(int beginLine, int endLine);
	List<Yfunction> findFunctionsByOtherFunction(Yfunction otherFunction);
	void parse() throws ParseException;

}
