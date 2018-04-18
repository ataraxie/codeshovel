package com.felixgrund.codestory.ast.entities;

import java.util.List;

public class Yfunction {

	private String body;
	private String name;
	private int nameLineNumber;

	private List<Yparameter> parameters;
	private Yreturn returnStmt;

	public Yfunction(String name, String body, List<Yparameter> parameters, Yreturn returnStmt, int nameLineNumber) {
		this.name = name;
		this.body = body;
		this.parameters = parameters;
		this.returnStmt = returnStmt;
		this.nameLineNumber = nameLineNumber;
	}

	public String getBody() {
		return body;
	}

	public String getName() {
		return name;
	}

	public List<Yparameter> getParameters() {
		return parameters;
	}

	public Yreturn getReturnStmt() {
		return returnStmt;
	}

	public int getNameLineNumber() {
		return nameLineNumber;
	}
}
