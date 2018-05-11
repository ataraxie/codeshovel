package com.felixgrund.codestory.ast.util;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.tasks.AnalysisTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class JsonResult {

	private String repositoryName;
	private String startCommitName;
	private String sourceFileName;
	private String functionName;
	private int functionStartLine;
	private int functionEndLine;
	private List<String> changeHistory;

	public JsonResult(AnalysisTask startTask, Yresult recursiveResult) {
		this.repositoryName = startTask.getRepositoryName();
		this.startCommitName = startTask.getStartCommitName();
		this.sourceFileName = startTask.getFileName();
		this.functionName = startTask.getFunctionName();
		this.functionStartLine = startTask.getFunctionStartLine();
		this.functionEndLine = startTask.getFunctionEndLine();

		this.changeHistory = new ArrayList<>();
		for (Ycommit ycommit : recursiveResult.keySet()) {
			this.changeHistory.add(ycommit.getName());
		}
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
}