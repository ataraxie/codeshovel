package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.entities.*;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.RevCommit;

import java.util.ArrayList;
import java.util.List;

public class JsFunction extends AbstractFunction implements Yfunction {

	private FunctionNode node;

	@Override
	public String getId() {
		String ident = Utl.sanitizeFunctionId(this.node.getName());
		String idParameterString = this.getIdParameterString();
		if (StringUtils.isNotBlank(idParameterString)) {
			ident += "___" + idParameterString;
		}
		return ident;
	}

	public JsFunction(FunctionNode node, Repository repository, RevCommit commit, String sourceFilePath, String sourceFileContent) {
		super(repository, commit, sourceFilePath, sourceFileContent);
		this.node = node;
	}

	@Override
	public String getName() {
		return this.node.getIdent().getName();
	}

	@Override
	public Yreturn getReturnStmt() {
		return Yreturn.NONE;
	}

	@Override
	public Ymodifiers getModifiers() {
		return Ymodifiers.NONE;
	}

	@Override
	public Yexceptions getExceptions() {
		return Yexceptions.NONE;
	}

	@Override
	public int getNameLineNumber() {
		return this.node.getLineNumber();
	}

	@Override
	public int getEndLineNumber() {
		String fileSource = this.node.getSource().getString();
		String sourceTillEndOfNode = fileSource.substring(0, this.node.getFinish());
		return Utl.countLineNumbers(sourceTillEndOfNode);
	}

	@Override
	public Object getRawFunction() {
		return this.node;
	}

	@Override
	public String getBody() {
		String fileSource = this.node.getSource().getString();
		return fileSource.substring(this.node.getStart(), this.node.getFinish());
	}

	@Override
	public List<Yparameter> getParameters() {
		List<Yparameter> parameters = new ArrayList<>();
		List<IdentNode> parameterNodes = this.node.getParameters();
		for (IdentNode node : parameterNodes) {
			parameters.add(new Yparameter(node.getName(), Yparameter.TYPE_NONE));
		}
		return parameters;
	}

}
