package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.util.Utl;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ReferenceType;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.List;

public class JavaFunction extends AbstractFunction implements Yfunction {

	private MethodDeclaration node;

	public JavaFunction(MethodDeclaration node, Repository repository, Commit commit, String sourceFilePath, String sourceFileContent) {
		super(repository, commit, sourceFilePath, sourceFileContent);
		this.node = node;
	}

	@Override
	public String getId() {
		String ident = this.getName();
		String idParameterString = this.getIdParameterString();
		if (StringUtils.isNotBlank(idParameterString)) {
			ident += "___" + idParameterString;
		}
		return Utl.sanitizeFunctionId(ident);
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
