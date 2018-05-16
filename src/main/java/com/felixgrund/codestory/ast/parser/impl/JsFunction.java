package com.felixgrund.codestory.ast.parser.impl;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yparameter;
import com.felixgrund.codestory.ast.entities.Yreturn;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.util.Utl;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;

import java.util.ArrayList;
import java.util.List;

public class JsFunction implements Yfunction {

	private FunctionNode node;

	// temp
	private Ycommit commit;
	@Override
	public void setCommit(Ycommit commit) {
		this.commit = commit;
	}
	@Override
	public Ycommit getCommit() {
		return commit;
	}

	public JsFunction(FunctionNode node) {
		this.node = node;
	}

	@Override
	public String getName() {
		return this.node.getIdent().getName();
	}

	@Override
	public Yreturn getReturnStmt() {
		return new Yreturn(Yreturn.TYPE_NONE);
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
