package com.felixgrund.codestory.ast.entities;

import java.util.List;

public class Yfunction {

	private String body;
	private String name;

	private List<String> parameterNames;

	public Yfunction(String name, String body, List<String> parameterNames) {
		this.name = name;
		this.body = body;
		this.parameterNames = parameterNames;
	}

	public String getBody() {
		return body;
	}

	public String getName() {
		return name;
	}

	public List<String> getParameterNames() {
		return parameterNames;
	}
}
