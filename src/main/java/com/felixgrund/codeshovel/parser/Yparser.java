package com.felixgrund.codeshovel.parser;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.changes.Ysignaturechange;
import com.felixgrund.codeshovel.entities.Ycommit;

import java.util.List;
import java.util.Map;

public interface Yparser {

	Yfunction findFunctionByNameAndLine(String name, int line);
	List<Yfunction> findFunctionsByLineRange(int beginLine, int endLine);
	List<Yfunction> getAllFunctions();
	Map<String, Yfunction> getAllFunctionsAsMap();
	Yfunction findFunctionByOtherFunction(Yfunction otherFunction);
	boolean functionNamesConsideredEqual(String aName, String bName);

	Yfunction getMostSimilarFunction(List<Yfunction> candidates, Yfunction compareFunction, boolean crossFile, boolean writeOutputFile);
	double getScopeSimilarity(Yfunction function, Yfunction compareFunction);

	List<Ysignaturechange> getMajorChanges(Ycommit commit, Yfunction compareFunction) throws Exception;
	List<Ychange> getMinorChanges(Ycommit commit, Yfunction compareFunction) throws Exception;

	String getAcceptedFileExtension();

}
