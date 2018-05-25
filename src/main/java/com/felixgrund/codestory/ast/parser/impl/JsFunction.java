package com.felixgrund.codestory.ast.parser.impl;

import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.util.Utl;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;

import java.util.ArrayList;
import java.util.List;

public class JsFunction implements Yfunction {

	private FunctionNode node;

	// temp
	private String commitName;

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
		return this.node.getName().replaceAll(":", "__").replaceAll("#", "__");
	}

	public JsFunction(FunctionNode node, String commitName) {
		this.node = node;
		this.commitName = commitName;
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
