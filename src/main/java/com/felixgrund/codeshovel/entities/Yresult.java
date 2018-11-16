package com.felixgrund.codeshovel.entities;

import com.felixgrund.codeshovel.changes.Ychange;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;

public class Yresult extends LinkedHashMap<String, Ychange> {

	private StringBuilder builder = new StringBuilder();

	@Override
	public Ychange put(String commitName, Ychange change) {
		builder.append("\n").append(commitName).append(":").append(change.toString());
		return super.put(commitName, change);
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
