package com.felixgrund.codeshovel.entities;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.wrappers.RevCommit;

public class Ycommit {

	private String hash;

	private RevCommit commit;
	private Ycommit prev;

	private Yfunction matchedFunction;

	private Ydiff ydiff;

	private String fileName;
	private String fileContent;
	private String filePath;

	private Yparser parser;

	// Only for serialization
	public Ycommit() {}

	public Ycommit(RevCommit commit) {
		this.commit = commit;
		this.hash = commit.getName();
	}

	public RevCommit getCommit() {
		return commit;
	}

	public Yfunction getMatchedFunction() {
		return matchedFunction;
	}

	public void setMatchedFunction(Yfunction matchedFunction) {
		this.matchedFunction = matchedFunction;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public Ycommit getPrev() {
		return prev;
	}

	public void setPrev(Ycommit prev) {
		this.prev = prev;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Yparser getParser() {
		return parser;
	}

	public void setParser(Yparser parser) {
		this.parser = parser;
	}

	public String toString() {
		return this.getCommit().getCommitDate() + " " + hash;
	}

	public String getName() {
		return this.commit.getName();
	}

	public String getShortName() {
		return commit.getCommitNameShort();
	}

	public Ydiff getYdiff() {
		return ydiff;
	}

	public void setYdiff(Ydiff ydiff) {
		this.ydiff = ydiff;
	}
}