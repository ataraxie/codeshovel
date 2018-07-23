package com.felixgrund.codeshovel.parser;

import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.util.Utl;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.RevCommit;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFunction implements Yfunction {

	private Repository repository;
	private String sourceFilePath;
	private String sourceFileContent;
	private RevCommit commit;

	public AbstractFunction(Repository repository, RevCommit commit, String sourceFilePath, String sourceFileContent) {
		this.repository = repository;
		this.commit = commit;
		this.sourceFilePath = sourceFilePath;
		this.sourceFileContent = sourceFileContent;
	}

	public String getIdParameterString() {
		List<String> parts = new ArrayList<>();
		for (Yparameter parameter : getParameters()) {
			String part = parameter.getName();
			if (StringUtils.isNotBlank(parameter.getType())) {
				part += "-" + parameter.getType();
			}
			parts.add(part);
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
	public Repository getRepository() {
		return repository;
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
	public RevCommit getCommit() {
		return commit;
	}
}
