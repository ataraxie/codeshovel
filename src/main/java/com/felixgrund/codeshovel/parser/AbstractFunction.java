package com.felixgrund.codeshovel.parser;

import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.util.Utl;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFunction implements Yfunction {

	private String sourceFilePath;
	private String sourceFileContent;
	private Commit commit;

	public AbstractFunction(Commit commit, String sourceFilePath, String sourceFileContent) {
		this.commit = commit;
		this.sourceFilePath = sourceFilePath;
		this.sourceFileContent = sourceFileContent;
	}

	public String getIdParameterString() {
		List<String> parts = new ArrayList<>();
		for (Yparameter parameter : getParameters()) {
			parts.add(parameter.toString());
		}
		return StringUtils.join(parts, "__");
	}

	@Override
	public String getSourceFragment() {
		int beginLine = getNameLineNumber();
		int endLine = getEndLineNumber();
		String source = getSourceFileContent();
		return Utl.getTextFragment(source, beginLine, endLine);
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
}
