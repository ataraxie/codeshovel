package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.changes.Ysignaturechange;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.exceptions.ParseException;

import java.util.List;

public interface Yparser {

	Yfunction findFunctionByNameAndLine(String name, int line);
	List<Yfunction> findFunctionsByLineRange(int beginLine, int endLine);
	List<Yfunction> getAllFunctions();
	Yfunction findFunctionByOtherFunction(Yfunction otherFunction);
	Object parse() throws ParseException;
	boolean functionNamesConsideredEqual(String aName, String bName);

	Yfunction getMostSimilarFunction(List<Yfunction> candidates, Yfunction compareFunction, boolean writeOutputFile);
	double getScopeSimilarity(Yfunction function, Yfunction compareFunction);

	List<Ysignaturechange> getMajorChanges(Ycommit commit, Yfunction compareFunction) throws Exception;
	List<Ychange> getMinorChanges(Ycommit commit, Yfunction compareFunction) throws Exception;

	List<Ychange> getCrossFileChanges(Ycommit ycommit, Yfunction compareFunction);

}
