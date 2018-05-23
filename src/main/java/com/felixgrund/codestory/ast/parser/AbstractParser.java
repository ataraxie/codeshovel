package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.*;

import java.util.List;

public abstract class AbstractParser implements Yparser {

	protected String filePath;
	protected String fileContent;
	protected String commitName;
	protected String repoName;

	public abstract boolean functionNamesConsideredEqual(String aName, String bName);

	public AbstractParser(String repoName, String filePath, String fileContent, String commitName) {
		this.repoName = repoName;
		this.filePath = filePath;
		this.fileContent = fileContent;
		this.commitName = commitName;
	}

	protected Yreturntypechange getReturnTypeChange(Ycommit commit, Yfunction compareFunction) {
		Yreturntypechange ret = null;
		Yreturn returnA = compareFunction.getReturnStmt();
		Yreturn returnB = commit.getMatchedFunction().getReturnStmt();
		if (returnA != null && !returnA.equals(returnB)) {
			ret = new Yreturntypechange(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Yinfilerename getFunctionRename(Ycommit commit, Yfunction compareFunction) {
		Yinfilerename ret = null;
		if (compareFunction != null) {
			if (!functionNamesConsideredEqual(commit.getMatchedFunction().getName(), compareFunction.getName())) {
				ret = new Yinfilerename(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
			}
		}
		return ret;
	}

	protected Yparameterchange getParametersChange(Ycommit commit, Yfunction compareFunction) {
		Yparameterchange ret = null;
		List<Yparameter> parametersA = compareFunction.getParameters();
		List<Yparameter> parametersB = commit.getMatchedFunction().getParameters();
		if (!parametersA.equals(parametersB)) {
			ret = new Yparameterchange(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Yexceptionschange getExceptionsChange(Ycommit commit, Yfunction compareFunction) {
		Yexceptionschange ret = null;
		Yexceptions exceptionsA = compareFunction.getExceptions();
		Yexceptions exceptionsB = commit.getMatchedFunction().getExceptions();
		if (exceptionsA != null && !exceptionsA.equals(exceptionsB)) {
			ret = new Yexceptionschange(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Ymodifierchange getModifiersChange(Ycommit commit, Yfunction compareFunction) {
		Ymodifierchange ret = null;
		Ymodifiers modifiersA = compareFunction.getModifiers();
		Ymodifiers modifiersB = commit.getMatchedFunction().getModifiers();
		if (modifiersA != null && !modifiersA.equals(modifiersB)) {
			ret = new Ymodifierchange(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Ybodychange getBodyChange(Ycommit commit, Yfunction compareFunction) {
		Ybodychange ret = null;
		Yfunction function = commit.getMatchedFunction();
		if (function != null && compareFunction != null && !function.getBody().equals(compareFunction.getBody())) {
			ret = new Ybodychange(commit);
		}
		return ret;
	}

}
