package com.felixgrund.codestory.ast.parser;

public class AbstractParser {

	protected String filePath;
	protected String fileContent;
	protected String commitName;
	protected String repoName;

	public AbstractParser(String repoName, String filePath, String fileContent, String commitName) {
		this.repoName = repoName;
		this.filePath = filePath;
		this.fileContent = fileContent;
		this.commitName = commitName;
	}

}
