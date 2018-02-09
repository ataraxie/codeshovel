package com.felixgrund.codestory.ast.entities;

import com.felixgrund.codestory.ast.util.Utl;
import jdk.nashorn.internal.ir.FunctionNode;

public class FunctionInfo {

	private FunctionNode functionNode;

	public FunctionInfo(FunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	public String getBodyString() {
		return Utl.getFunctionBody(functionNode);
	}

	public FunctionNode getFunctionNode() {
		return functionNode;
	}

}
