package com.felixgrund.codestory.ast.entities;

import jdk.nashorn.internal.ir.FunctionNode;

public class FunctionInfo {

	private FunctionNode functionNode;
	private String fileContent;
	private String bodyString;

	public FunctionInfo(FunctionNode functionNode, String fileContent) {
		this.functionNode = functionNode;
		this.fileContent = fileContent;
		if (functionNode != null) {
			setBodyString();
		}
	}

	private void setBodyString() {
		// TODO: use functionNode.getSource?
		bodyString = fileContent.substring(functionNode.getStart(), functionNode.getFinish());
	}

	public FunctionNode getFunctionNode() {
		return functionNode;
	}

	public String getBodyString() {
		return bodyString;
	}
}
