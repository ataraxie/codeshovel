package com.felixgrund.codestory.ast.entities;

import com.felixgrund.codestory.ast.parser.JsParser;
import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Ycommit {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy:HH:mm");

	private String hash;
	private Date date;

	private RevCommit commit;
	private Ycommit prev;
	private Ycommit next;

	private List<String> functionNameOccurrences;
	private Yfunction matchedFunctionInfo;

	private Ydiff Ydiff;

	private boolean firstFunctionOccurrence = false;

	private String fileName;
	private String fileContent;
	private String filePath;

	private JsParser parser;

	// Only for serialization
	public Ycommit() {}

	public Ycommit(RevCommit commit) {
		this.commit = commit;
		this.hash = commit.getName();
		this.date = commit.getAuthorIdent().getWhen();
	}

	public RevCommit getCommit() {
		return commit;
	}

	public Yfunction getMatchedFunctionInfo() {
		return matchedFunctionInfo;
	}

	public void setMatchedFunctionInfo(Yfunction matchedFunctionInfo) {
		this.matchedFunctionInfo = matchedFunctionInfo;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public List<String> getFunctionNameOccurrences() {
		return functionNameOccurrences;
	}

	public void setFunctionNameOccurrences(List<String> functionNameOccurrences) {
		this.functionNameOccurrences = functionNameOccurrences;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isFileFound() {
		return this.fileContent != null;
	}
	public boolean isFunctionFound() {
		return this.matchedFunctionInfo != null;
	}

	public Ycommit getPrev() {
		return prev;
	}

	public void setPrev(Ycommit prev) {
		this.prev = prev;
	}

	public Ycommit getNext() {
		return next;
	}

	public void setNext(Ycommit next) {
		this.next = next;
	}

	public boolean isFirstFunctionOccurrence() {
		return firstFunctionOccurrence;
	}

	public void setFirstFunctionOccurrence(boolean firstFunctionOccurrence) {
		this.firstFunctionOccurrence = firstFunctionOccurrence;
	}

	public String getHash() {
		return hash;
	}

	public Date getDate() {
		return date;
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

	public JsParser getParser() {
		return parser;
	}

	public void setParser(JsParser parser) {
		this.parser = parser;
	}

	public String toString() {
		return DATE_FORMAT.format(date) + " " + hash;
	}
}