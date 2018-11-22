package com.felixgrund.codeshovel.visitors;

import com.felixgrund.codeshovel.parser.Yfunction;

import java.util.ArrayList;
import java.util.List;

public abstract class MethodVisitor {

	private List<Yfunction> allMethods;

	public MethodVisitor(List<Yfunction> allMethods) {
		this.allMethods = allMethods;
	}

	public abstract boolean methodMatches(Yfunction method);

	public List<Yfunction> getMatchedNodes() {
		List<Yfunction> matchedNodes = new ArrayList<>();
		for (Yfunction method : allMethods) {
			if (methodMatches(method)) {
				matchedNodes.add(method);
			}
		}
		return matchedNodes;
	}
}