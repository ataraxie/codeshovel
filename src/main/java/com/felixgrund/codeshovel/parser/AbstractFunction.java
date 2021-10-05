package com.felixgrund.codeshovel.parser;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.util.Utl;
import org.apache.commons.lang3.StringUtils;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFunction<E> implements Yfunction {

	private String sourceFilePath;
	private String sourceFileContent;
	private Commit commit;

	/**
	 * These are the generic fields we need for methods. The getInitial* methods will be called immediately in the
	 * constructor and set as members. The actual get* methods are then simple getters, which increases performance
	 * immensely.
	 */

	private String id;
	private String name;
	protected abstract String getInitialName(E rawMethod);
	private String type;
	protected abstract String getInitialType(E rawMethod);
	private Ymodifiers modifiers;
	protected abstract Ymodifiers getInitialModifiers(E rawMethod);
	private Yexceptions exceptions;
	protected abstract Yexceptions getInitialExceptions(E rawMethod);
	private Yreturn returnStmt;
	private List<Yparameter> parameters;
	protected abstract List<Yparameter> getInitialParameters(E rawMethod);
	private String body;
	protected abstract String getInitialBody(E rawMethod);
	private int beginLine;
	protected abstract int getInitialBeginLine(E rawMethod);
	private int endLine;
	protected abstract int getInitialEndLine(E rawMethod);
	private String parentName;
	protected abstract String getInitialParentName(E rawMethod);
	private String functionPath;
	protected abstract String getInitialFunctionPath(E rawMethod);
	private String annotation;
	protected abstract String getInitialAnnotation(E rawMethod);
	private String sourceFragment;
	private String functionDoc;
	protected abstract String getInitialDoc(E rawMethod);
	private String unformattedBody;
	protected abstract String getInitialUnformattedBody(E rawMethod);

	public AbstractFunction(E rawMethod, Commit commit, String sourceFilePath, String sourceFileContent) {
		this.commit = commit;
		this.sourceFilePath = sourceFilePath;
		this.sourceFileContent = sourceFileContent;

		// Assign all the values in sequence.
		// NOTE: the order of these calls does matter!
		this.name = getInitialName(rawMethod);
		this.parameters = getInitialParameters(rawMethod);
		this.parentName = getInitialParentName(rawMethod);
		this.id = getInitialId(rawMethod);
		this.type = getInitialType(rawMethod);
		this.modifiers = getInitialModifiers(rawMethod);
		this.exceptions = getInitialExceptions(rawMethod);
		this.body = getInitialBody(rawMethod);
		this.beginLine = getInitialBeginLine(rawMethod);
		this.endLine = getInitialEndLine(rawMethod);
		this.functionPath = getInitialFunctionPath(rawMethod);
		this.returnStmt = getInitialReturnStmt(rawMethod); // Must be called after getInitialType
		this.annotation = getInitialAnnotation(rawMethod);
		this.functionDoc = getInitialDoc(rawMethod);
		this.unformattedBody = getInitialUnformattedBody(rawMethod);
		this.sourceFragment = getInitialSourceFragment(rawMethod); // Must be called after begin/endLine
	}

	protected String getIdParameterString() {
		List<String> parts = new ArrayList<>();
		for (Yparameter parameter : getParameters()) {
			parts.add(parameter.toString());
		}
		return StringUtils.join(parts, "__");
	}

	protected String getInitialId(E rawMethod) {
		String ident = getParentName() + "#" + getName();
		String idParameterString = this.getIdParameterString();
		if (StringUtils.isNotBlank(idParameterString)) {
			ident += "___" + idParameterString;
		}
		return Utl.sanitizeFunctionId(ident);
	}

	protected Yreturn getInitialReturnStmt(E rawMethod) {
		if (this.type == null) {
			return Yreturn.NONE;
		} else {
			return new Yreturn(this.type);
		}
	}

	protected String getInitialSourceFragment(E rawMethod) {
		// Naive implementation
		// Will not work if there is more than one function on a line
		int beginLine = getNameLineNumber();
		int endLine = getEndLineNumber();
		String source = getSourceFileContent();
		return Utl.getTextFragment(source, beginLine, endLine);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getSourceFragment() {
		return this.sourceFragment;
	}

	@Override
	public String getCommitName() {
		return this.commit.getName();
	}

	@Override
	public String getCommitNameShort() {
		return this.commit.getName().substring(0, 6);
	}

	@Override
	public String getSourceFilePath() {
		return sourceFilePath;
	}

	@Override
	public String getSourceFileContent() {
		return sourceFileContent;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + getId();
	}

	@Override
	public Commit getCommit() {
		return commit;
	}

	@Override
	public String getFunctionPath() {
		return this.functionPath;
	}

	@Override
	public String getParentName() {
		return this.parentName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Yreturn getReturnStmt() {
		return this.returnStmt;
	}

	@Override
	public Ymodifiers getModifiers() {
		return this.modifiers;
	}

	@Override
	public Yexceptions getExceptions() {
		return this.exceptions;
	}

	@Override
	public String getBody() {
		return this.body;
	}

	@Override
	public List<Yparameter> getParameters() {
		return this.parameters;
	}

	@Override
	public int getNameLineNumber() {
		return this.beginLine;
	}

	@Override
	public int getEndLineNumber() {
		return this.endLine;
	}

	@Override
	public String getAnnotation() {
		return this.annotation;
	}

	@Override
	public String getFunctionDoc() {
		return this.functionDoc;
	}

	@Override
	public String getUnformattedBody() {
		return this.unformattedBody;
	}
}
