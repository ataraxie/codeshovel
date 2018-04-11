package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.entities.Yfunction;
import com.felixgrund.codestory.ast.exceptions.ParseException;

import java.util.List;

public interface Yparser {

	Yfunction findFunctionByNameAndLine(String name, int line);
	List<Yfunction> findFunctionByOtherFunction(Yfunction otherFunction);
	void parse() throws ParseException;

}
