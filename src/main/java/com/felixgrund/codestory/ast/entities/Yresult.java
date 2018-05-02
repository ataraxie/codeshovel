package com.felixgrund.codestory.ast.entities;

import com.felixgrund.codestory.ast.changes.Ychange;

import java.util.LinkedHashMap;

public class Yresult extends LinkedHashMap<Ycommit, Ychange> {

	private String startCommitName;
	private String functionName;
	private int functionStartLine;

	public Yresult(String startCommitName, String functionName, int functionStartLine) {
		super();
		this.startCommitName = startCommitName;
		this.functionName = functionName;
		this.functionStartLine = functionStartLine;
	}

	public String getStartCommitName() {
		return startCommitName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public int getFunctionStartLine() {
		return functionStartLine;
	}
}
