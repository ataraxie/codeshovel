package com.felixgrund.codestory.ast.entities;

import jdk.nashorn.internal.ir.FunctionNode;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

public class CommitInfo {

	private RevCommit commit;
	private RevCommit prevCommit;
	private RevCommit nextCommit;

	private List<String> functionNameOccurrences;
	private FunctionNode matchedFunctionNode;

	private String fileName;
	private String fileContent;

	public RevCommit getCommit() {
		return commit;
	}

	public void setCommit(RevCommit commit) {
		this.commit = commit;
	}

	public FunctionNode getMatchedFunctionNode() {
		return matchedFunctionNode;
	}

	public void setMatchedFunctionNode(FunctionNode matchedFunctionNode) {
		this.matchedFunctionNode = matchedFunctionNode;
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

	public RevCommit getPrevCommit() {
		return prevCommit;
	}

	public void setPrevCommit(RevCommit prevCommit) {
		this.prevCommit = prevCommit;
	}

	public RevCommit getNextCommit() {
		return nextCommit;
	}

	public void setNextCommit(RevCommit nextCommit) {
		this.nextCommit = nextCommit;
	}
}