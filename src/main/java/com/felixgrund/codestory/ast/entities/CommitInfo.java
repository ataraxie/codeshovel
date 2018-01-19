package com.felixgrund.codestory.ast.entities;

import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommitInfo {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy:HH:mm");

	private String hash;
	private Date date;

	private RevCommit commit;
	private CommitInfo prev;
	private CommitInfo next;

	private List<String> functionNameOccurrences;
	private FunctionInfo matchedFunctionInfo;

	private DiffInfo diffInfo;

	private boolean firstFunctionOccurrence = false;

	private String fileName;
	private String fileContent;
	private String filePath;

	// Only for serialization
	public CommitInfo() {}

	public CommitInfo(RevCommit commit) {
		this.commit = commit;
		this.hash = commit.getName();
		this.date = commit.getAuthorIdent().getWhen();
	}

	public RevCommit getCommit() {
		return commit;
	}

	public FunctionInfo getMatchedFunctionInfo() {
		return matchedFunctionInfo;
	}

	public void setMatchedFunctionInfo(FunctionInfo matchedFunctionInfo) {
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

	public CommitInfo getPrev() {
		return prev;
	}

	public void setPrev(CommitInfo prev) {
		this.prev = prev;
	}

	public CommitInfo getNext() {
		return next;
	}

	public void setNext(CommitInfo next) {
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

	public DiffInfo getDiffInfo() {
		return diffInfo;
	}

	public void setDiffInfo(DiffInfo diffInfo) {
		this.diffInfo = diffInfo;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String toString() {
		return DATE_FORMAT.format(date) + " " + hash;
	}
}