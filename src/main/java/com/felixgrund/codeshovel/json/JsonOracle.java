package com.felixgrund.codeshovel.json;

import com.felixgrund.codeshovel.changes.Ychange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class JsonOracle {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private String repositoryName;
	private String filePath;
	private String functionName;
	private int functionStartLine;
	private String startCommitName;
	private List<String> expectedResult;

	public JsonOracle(JsonResult jsonResult) {
		this.repositoryName = jsonResult.getRepositoryName();
		this.filePath = jsonResult.getSourceFilePath();
		this.functionName = jsonResult.getFunctionName();
		this.functionStartLine = jsonResult.getFunctionStartLine();
		this.startCommitName = jsonResult.getStartCommitName();
		this.expectedResult = new ArrayList<>();
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getStartCommitName() {
		return startCommitName;
	}

	public void setStartCommitName(String startCommitName) {
		this.startCommitName = startCommitName;
	}

	public List<String> getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(List<String> expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String toJson() {
		return GSON.toJson(this);
	}
}