package com.felixgrund.codestory.ast.util;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MethodVisitor extends VoidVisitorAdapter<Void> {

	private List<MethodDeclaration> matchedNodes = new ArrayList<>();

	public abstract boolean methodMatches(MethodDeclaration method);

	@Override
	public void visit(MethodDeclaration method, Void arg) {
		super.visit(method, arg);
		if (methodMatches(method)) {
			matchedNodes.add(method);
		}
	}

	public List<MethodDeclaration> getMatchedNodes() {
		return matchedNodes;
	}
}
