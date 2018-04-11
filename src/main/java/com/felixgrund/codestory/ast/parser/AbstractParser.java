package com.felixgrund.codestory.ast.parser;

public class AbstractParser {

	protected String fileName;
	protected String fileContent;

	public AbstractParser(String fileName, String fileContent) {
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

}
