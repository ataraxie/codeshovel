package com.felixgrund.codeshovel.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class JsonChangeHistoryDiff {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private List<String> codeshovelHistory;
	private List<String> baselineHistory;
	private List<String> onlyInCodestory;
	private List<String> onlyInBaseline;
	private String baselineType;


	public JsonChangeHistoryDiff(
			List<String> codeshovelHistory,
			List<String> baselineHistory,
			List<String> onlyInCodeshovel,
			List<String> onlyInBaseline,
			String baselineType) {

		this.codeshovelHistory = codeshovelHistory;
		this.baselineHistory = baselineHistory;
		this.onlyInCodestory = onlyInCodeshovel;
		this.onlyInBaseline = onlyInBaseline;
		this.baselineType = baselineType;
	}

	public List<String> getOnlyInCodestory() {
		return onlyInCodestory;
	}

	public List<String> getOnlyInBaseline() {
		return onlyInBaseline;
	}

	public String getBaselineType() {
		return baselineType;
	}

	public String toJson() {
		return GSON.toJson(this);
	}
}
