package com.felixgrund.codestory.ast.parser;

import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionNodeVisitor extends SimpleNodeVisitor {

	private List<FunctionNode> matchedNodes = new ArrayList<>();

	public abstract boolean nodeMatches(FunctionNode functionNode);

	@Override
	public boolean enterFunctionNode(FunctionNode functionNode) {
		if (nodeMatches(functionNode)) {
			matchedNodes.add(functionNode);
		}
		return true;
	}

	public List<FunctionNode> getMatchedNodes() {
		return matchedNodes;
	}
}
