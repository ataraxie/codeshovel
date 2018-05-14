package com.felixgrund.codestory.ast.parser.impl;

import com.felixgrund.codestory.ast.entities.Yparameter;
import com.felixgrund.codestory.ast.entities.Yreturn;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import java.util.ArrayList;
import java.util.List;

public class JavaFunction implements Yfunction {

	private MethodDeclaration node;

	public JavaFunction(MethodDeclaration node) {
		this.node = node;
	}

	@Override
	public String getName() {
		return this.node.getNameAsString();
	}

	@Override
	public Yreturn getReturnStmt() {
		return new Yreturn(this.node.getTypeAsString());
	}

	@Override
	public String getBody() {
		return this.node.getBody().get().toString();
	}


	@Override
	public List<Yparameter> getParameters() {
		List<Yparameter> parameters = new ArrayList<>();
		List<Parameter> parameterElements = this.node.getParameters();
		for (Parameter parameterElement : parameterElements) {
			Yparameter parameter = new Yparameter(parameterElement.getNameAsString(), parameterElement.getTypeAsString());
			parameters.add(parameter);
		}
		return parameters;
	}

	@Override
	public int getNameLineNumber() {
		return this.node.getName().getBegin().get().line;
	}

	@Override
	public int getEndLineNumber() {
		return 0;
	}

	@Override
	public Object getRawFunction() {
		return this.node;
	}

}
