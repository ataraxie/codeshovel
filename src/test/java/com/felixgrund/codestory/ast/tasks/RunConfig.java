package com.felixgrund.codestory.ast.tasks;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

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

	LinkedHashSet<String> codestoryLog;
	LinkedHashSet<String> intellijLog;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getStartCommitName() {
		return startCommitName;
	}

	public void setStartCommitName(String startCommitName) {
		this.startCommitName = startCommitName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public int getFunctionStartLine() {
		return functionStartLine;
	}

	public void setFunctionStartLine(int functionStartLine) {
		this.functionStartLine = functionStartLine;
	}

	public LinkedHashMap<String, String> getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(LinkedHashMap<String, String> expectedResult) {
		this.expectedResult = expectedResult;
	}

	public LinkedHashSet<String> getCodestoryLog() {
		return codestoryLog;
	}

	public void setCodestoryLog(LinkedHashSet<String> codestoryLog) {
		this.codestoryLog = codestoryLog;
	}

	public LinkedHashSet<String> getIntellijLog() {
		return intellijLog;
	}

	public void setIntellijLog(LinkedHashSet<String> intellijLog) {
		this.intellijLog = intellijLog;
	}
}
