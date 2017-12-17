package com.felixgrund.codestory.ast.parser;

import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class FunctionNodeVisitor extends SimpleNodeVisitor {

	private List<FunctionNode> visitedNodes = new ArrayList<>();

	@Override
	public boolean enterFunctionNode(FunctionNode functionNode) {
		visitedNodes.add(functionNode);
		return true;
	}

	public List<FunctionNode> getVisitedNodes() {
		return visitedNodes;
	}
}
