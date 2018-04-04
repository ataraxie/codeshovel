package com.felixgrund.codestory.ast.entities;

import com.felixgrund.codestory.ast.util.Utl;
import jdk.nashorn.internal.ir.FunctionNode;

public class YFunction {

	private FunctionNode functionNode;

	public YFunction(FunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	public String getBodyString() {
		return Utl.getFunctionBody(functionNode);
	}

	public FunctionNode getFunctionNode() {
		return functionNode;
	}

}
