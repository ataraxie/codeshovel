package com.felixgrund.codestory.ast.util;

import com.felixgrund.codestory.ast.tasks.AnalysisTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class JsonResult {

	private String origin;
	private String repositoryName;
	private String startCommitName;
	private String sourceFileName;
	private String functionName;
	private String sourceFilePath;
	private int functionStartLine;
	private int functionEndLine;
	private List<String> changeHistory;
	private List<String> changeHistoryDetails;

	public JsonResult(String origin, AnalysisTask startTask, List<String> changeHistory, List<String> changeHistoryDetails) {
		this.origin = origin;
		this.repositoryName = startTask.getRepositoryName();
		this.startCommitName = startTask.getStartCommitName();
		this.sourceFileName = startTask.getFileName();
		this.functionName = startTask.getFunctionName();
		this.functionStartLine = startTask.getFunctionStartLine();
		this.sourceFilePath = startTask.getFilePath();
		this.functionEndLine = startTask.getFunctionEndLine();
		this.changeHistory = changeHistory;
		this.changeHistoryDetails = changeHistoryDetails;
	}

	public String getStartCommitName() {
		return startCommitName;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public int getFunctionStartLine() {
		return functionStartLine;
	}

	public int getFunctionEndLine() {
		return functionEndLine;
	}

	public List<String> getChangeHistory() {
		return changeHistory;
	}

	public String toJsonString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getOrigin() {
		return origin;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}
}