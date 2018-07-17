package com.felixgrund.codeshovel.entities;

import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.util.Utl;
import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ycommit {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy:HH:mm");

	private String hash;
	private Date date;

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
		this.date = Utl.getCommitDate(commit);
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
		return DATE_FORMAT.format(date) + " " + hash;
	}

	public String getName() {
		return this.commit.getName();
	}

	public String getShortName() {
		return Utl.getCommitNameShort(this.commit);
	}

	public Ydiff getYdiff() {
		return ydiff;
	}

	public void setYdiff(Ydiff ydiff) {
		this.ydiff = ydiff;
	}
}