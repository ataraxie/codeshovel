package com.felixgrund.codeshovel.parser;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.changes.Ysignaturechange;
import com.felixgrund.codeshovel.entities.Ycommit;

import java.util.List;
import java.util.Map;

public interface Yparser {

	/**
	 * Find a method by the given name and line number
	 * @param name Method name
	 * @param line Line number
	 * @return Method if found, null otherwise
	 */
	Yfunction findFunctionByNameAndLine(String name, int line);

	/**
	 * Find a method using the given line range
	 * @param beginLine Start line of the line range
	 * @param endLine End line of the line range
	 * @return Method if found, null otherwise
	 */
	List<Yfunction> findMethodsByLineRange(int beginLine, int endLine);

	/**
	 * @return All methods in this file
	 */
	List<Yfunction> getAllMethods();

	/**
	 * @return A mapping MethodName => Method for all methods in this file
	 */
	Map<String, Yfunction> getAllMethodsCount();

	/**
	 * Find a method that is similar to the given other method
	 * @param otherFunction The similar method
	 * @return Method if found, null otherwise
	 */
	Yfunction findFunctionByOtherFunction(Yfunction otherFunction);

	/**
	 * Check if the given method names are equal (i.e. == in most languages or .equals in others)
	 * @param aName First method name
	 * @param bName Second method name
	 * @return True if method names are equal, false otherwise
	 */
	boolean functionNamesConsideredEqual(String aName, String bName);

	/**
	 * Find the most similar method as the given compareFunction in the given set of candidate methods
	 * @param candidates Set of candidate methods
	 * @param compareFunction Similar method
	 * @param crossFile True if other files in commit should be searched, false if only in-file
	 * @return Similar method if found, null otherwise
	 */
	Yfunction getMostSimilarFunction(List<Yfunction> candidates, Yfunction compareFunction, boolean crossFile);

	/**
	 * @param function Method 1
	 * @param compareFunction Method 2
	 * @return Number between 0 and 1 for the similarity of the scope of the two given methods
	 */
	double getScopeSimilarity(Yfunction function, Yfunction compareFunction);

	/**
	 * Retrieves the set of "major changes" (i.e. all derivates of Ysignaturechange as of now) for the given commit
	 * and method.
	 * @param commit Commit
	 * @param compareFunction Method
	 * @return Set of major changes
	 * @throws Exception
	 */
	List<Ysignaturechange> getMajorChanges(Ycommit commit, Yfunction compareFunction) throws Exception;

	/**
	 * Retrieves the set of "minor changes" (i.e. all changes that are NOT derivates of Ysignaturechange as of now)
	 * for the given commit and method.
	 * @param commit Commit
	 * @param compareFunction Method
	 * @return Set of minor changes
	 * @throws Exception
	 */
	List<Ychange> getMinorChanges(Ycommit commit, Yfunction compareFunction) throws Exception;

	/**
	 * @return The accepted file extension for this language as string (e.g. ".js", ".java")
	 */
	String getAcceptedFileExtension();

}