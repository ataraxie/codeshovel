package com.felixgrund.codestory.ast.entities;

import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.parser.impl.JsParser;
import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Ycommit {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy:HH:mm");

	private String hash;
	private Date date;

	private RevCommit commit;
	private Ycommit parent;

	private Yfunction matchedFunction;

	private Ydiff Ydiff;

	private String fileName;
	private String fileContent;
	private String filePath;

	private Yparser parser;

	// Only for serialization
	public Ycommit() {}

	public Ycommit(RevCommit commit) {
		this.commit = commit;
		this.hash = commit.getName();
		this.date = new Date((long) 1000 * commit.getCommitTime());
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

	public Ycommit getParent() {
		return parent;
	}

	public void setParent(Ycommit parent) {
		this.parent = parent;
	}

	public Ydiff getYdiff() {
		return Ydiff;
	}

	public void setYdiff(Ydiff ydiff) {
		this.Ydiff = ydiff;
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
		return DATE_FORMAT.format(date) + " " + hash;
	}

	public String getName() {
		return this.commit.getName();
	}

	public String getShortName() {
		return this.commit.getName().substring(0, 6);
	}
}