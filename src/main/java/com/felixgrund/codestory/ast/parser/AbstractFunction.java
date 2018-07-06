package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.entities.Yparameter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFunction implements Yfunction {

	private String sourceFilePath;
	private String sourceFileContent;
	private String commitName;

	public AbstractFunction(String commitName, String sourceFilePath, String sourceFileContent) {
		this.commitName = commitName;
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
	public String getCommitName() {
		return commitName;
	}

	@Override
	public String getCommitNameShort() {
		return commitName.substring(0, 6);
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
}
