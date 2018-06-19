package com.felixgrund.codestory.ast.parser.impl;

import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.parser.AbstractFunction;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ReferenceType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class JavaFunction extends AbstractFunction implements Yfunction {

	private MethodDeclaration node;
	private String commitName;

	public JavaFunction(MethodDeclaration node, String commitName, String sourceFilePath, String sourceFileContent) {
		super(sourceFilePath, sourceFileContent);
		this.node = node;
		this.commitName = commitName;
	}

	@Override
	public String getCommitName() {
		return commitName;
	}

	@Override
	public void setCommitName(String commitName) {
		this.commitName = commitName;
	}

	@Override
	public String getId() {
		String ident = this.getName();
		String idParameterString = this.getIdParameterString();
		if (StringUtils.isNotBlank(idParameterString)) {
			ident += "___" + idParameterString;
		}
		return ident;
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
	public Ymodifiers getModifiers() {
		List<String> modifiers = new ArrayList<>();
		for (Modifier modifier : this.node.getModifiers()) {
			modifiers.add(modifier.asString());
		}
		return new Ymodifiers(modifiers);
	}

	@Override
	public Yexceptions getExceptions() {
		List<String> exceptions = new ArrayList<>();
		for (ReferenceType type : this.node.getThrownExceptions()) {
			exceptions.add(type.asString());
		}
		return new Yexceptions(exceptions);
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
		return this.node.getEnd().get().line;
	}

	@Override
	public Object getRawFunction() {
		return this.node;
	}

}
