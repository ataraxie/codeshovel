package com.felixgrund.codeshovel.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class JsonChangeHistoryDiff {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private List<String> codeshovelHistory;
	private List<String> baselineHistory;
	private List<String> onlyInCodeshovel;
	private List<String> onlyInBaseline;

	public JsonChangeHistoryDiff(
			List<String> codeshovelHistory,
			List<String> baselineHistory,
			List<String> onlyInCodeshovel,
			List<String> onlyInBaseline) {

		this.codeshovelHistory = codeshovelHistory;
		this.baselineHistory = baselineHistory;
		this.onlyInCodeshovel = onlyInCodeshovel;
		this.onlyInBaseline = onlyInBaseline;
	}

	public String toJson() {
		return GSON.toJson(this);
	}
}
