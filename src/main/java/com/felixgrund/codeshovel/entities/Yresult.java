package com.felixgrund.codeshovel.entities;

import com.felixgrund.codeshovel.changes.Ychange;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;

public class Yresult extends LinkedHashMap<Ycommit, Ychange> {

	private StringBuilder builder = new StringBuilder();

	@Override
	public Ychange put(Ycommit commit, Ychange change) {
		builder.append("\n").append(commit.getName()).append(":").append(change.toString());
		return super.put(commit, change);
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
