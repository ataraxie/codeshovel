package com.felixgrund.codestory.ast.entities;

import com.felixgrund.codestory.ast.util.Utl;
import jdk.nashorn.internal.ir.FunctionNode;

public class Yfunction {

	private FunctionNode functionNode;

	public Yfunction(FunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	public String getBodyString() {
		return Utl.getFunctionBody(functionNode);
	}

	public FunctionNode getFunctionNode() {
		return functionNode;
	}

}
