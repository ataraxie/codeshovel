package com.felixgrund.codestory.ast.exceptions;


public class ParseException extends Exception {

	private String fileName;
	private String fileContent;

	public ParseException(String message, String fileName, String fileContent) {
		super(message);
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileContent() {
		return fileContent;
	}
}
