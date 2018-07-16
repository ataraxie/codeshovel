package com.felixgrund.codestory.ast.tasks;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class RunConfig {

	private String configName;
	private String repoName;
	private String filePath;
	private String branchName;
	private String startCommitName;
	private String methodName;
	private int methodStartLine;

	private LinkedHashMap<String, String> expectedResult;

	private LinkedHashSet<String> codestoryLog;
	private LinkedHashSet<String> gitLog;

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

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getMethodStartLine() {
		return methodStartLine;
	}

	public void setMethodStartLine(int methodStartLine) {
		this.methodStartLine = methodStartLine;
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

	public LinkedHashSet<String> getGitLog() {
		return gitLog;
	}

	public void setGitLog(LinkedHashSet<String> gitLog) {
		this.gitLog = gitLog;
	}
}
