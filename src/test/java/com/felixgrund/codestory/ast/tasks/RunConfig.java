package com.felixgrund.codestory.ast.tasks;

import java.util.LinkedHashMap;

public class RunConfig {

	String configName;
	String repoName;
	String filePath;
	String fileName;
	String branchName;
	String startCommitName;
	String functionName;
	int functionStartLine;

	LinkedHashMap<String, String> expectedResult;
}
