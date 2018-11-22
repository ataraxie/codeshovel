package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.util.Utl;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.List;

public class JsFunction extends AbstractFunction<FunctionNode> implements Yfunction {


	public JsFunction(FunctionNode node, Commit commit, String sourceFilePath, String sourceFileContent) {
		super(node, commit, sourceFilePath, sourceFileContent);
	}

	@Override
	public List<Yparameter> getInitialParameters(FunctionNode method) {
		List<Yparameter> parameters = new ArrayList<>();
		List<IdentNode> parameterNodes = method.getParameters();
		for (IdentNode node : parameterNodes) {
			parameters.add(new Yparameter(node.getName(), Yparameter.TYPE_NONE));
		}
		return parameters;
	}

	@Override
	protected String getInitialName(FunctionNode method) {
		return method.getIdent().getName();
	}

	@Override
	protected String getInitialType(FunctionNode method) {
		return null;
	}

	@Override
	protected Ymodifiers getInitialModifiers(FunctionNode method) {
		return Ymodifiers.NONE;
	}

	@Override
	protected Yexceptions getInitialExceptions(FunctionNode method) {
		return Yexceptions.NONE;
	}

	@Override
	protected String getInitialBody(FunctionNode method) {
		FunctionNode node = method;
		return getSourceFileContent().substring(node.getStart(), node.getFinish());
	}

	@Override
	protected int getInitialBeginLine(FunctionNode method) {
		return method.getLineNumber();
	}

	@Override
	protected int getInitialEndLine(FunctionNode method) {
		FunctionNode node = method;
		String fileSource = node.getSource().getString();
		String sourceTillEndOfNode = fileSource.substring(0, node.getFinish());
		return Utl.countLineNumbers(sourceTillEndOfNode);
	}

	@Override
	protected String getInitialParentName(FunctionNode method) {
		return null;
	}

	@Override
	protected String getInitialFunctionPath(FunctionNode method) {
		return method.getName();
	}

}
