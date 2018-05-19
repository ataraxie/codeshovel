package com.felixgrund.codestory.ast.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class JsonChangeHistoryDiff {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private JsonResult jsonResult;
	private List<String> gitRangeLogHistory;
	private List<String> onlyInCodestory;
	private List<String> onlyInGitRangeLog;


	public JsonChangeHistoryDiff(JsonResult jsonResult, List<String> gitRangeLogHistory,
								 List<String> onlyInCodestory, List<String> onlyInGitRangeLog) {
		this.jsonResult = jsonResult;
		this.onlyInCodestory = onlyInCodestory;
		this.onlyInGitRangeLog = onlyInGitRangeLog;
		this.gitRangeLogHistory = gitRangeLogHistory;
	}

	public List<String> getOnlyInCodestory() {
		return onlyInCodestory;
	}

	public List<String> getOnlyInGitRangeLog() {
		return onlyInGitRangeLog;
	}

	public JsonResult getJsonResult() {
		return jsonResult;
	}

	public List<String> getGitRangeLogHistory() {
		return gitRangeLogHistory;
	}

	public String toJson() {
		return GSON.toJson(this);
	}
}
