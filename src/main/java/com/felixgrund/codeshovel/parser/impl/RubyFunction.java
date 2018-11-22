package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.jrubyparser.ast.MethodDefNode;
import org.jrubyparser.ast.NamedNode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RubyFunction extends AbstractFunction<MethodDefNode> implements Yfunction {

	public RubyFunction(MethodDefNode method, Commit commit, String sourceFilePath, String sourceFileContent) {
		super(method, commit, sourceFilePath, sourceFileContent);
	}

	@Override
	protected String getInitialName(MethodDefNode method) {
		return method.getName();
	}

	@Override
	protected String getInitialType(MethodDefNode method) {
		return null;
	}

	@Override
	protected Ymodifiers getInitialModifiers(MethodDefNode method) {
		return Ymodifiers.NONE;
	}

	@Override
	protected Yexceptions getInitialExceptions(MethodDefNode method) {
		return Yexceptions.NONE;
	}

	@Override
	protected List<Yparameter> getInitialParameters(MethodDefNode method) {
		List<String> parameterStrings = method.getArgs().getNormativeParameterNameList(false);
		List<Yparameter> parametersList = new ArrayList<>();
		for (String parameterString : parameterStrings) {
			Yparameter parameter = new Yparameter(parameterString, null);
			parametersList.add(parameter);
		}
		return parametersList;
	}

	@Override
	protected String getInitialBody(MethodDefNode method) {
		return method.getBody().toString();
	}

	@Override
	protected int getInitialBeginLine(MethodDefNode method) {
		return method.getNamePosition().getStartLine();
	}

	@Override
	protected int getInitialEndLine(MethodDefNode method) {
		return method.getPosition().getEndLine();
	}

	@Override
	protected String getInitialParentName(MethodDefNode method) {
		MethodDefNode node = method;
		if (node.getParent() instanceof NamedNode) {
			return ((NamedNode) node.getParent()).getName();
		}
		return null;
	}

	@Override
	protected String getInitialFunctionPath(MethodDefNode method) {
		return null;
	}

}
