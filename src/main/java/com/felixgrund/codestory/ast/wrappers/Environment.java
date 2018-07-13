package com.felixgrund.codestory.ast.wrappers;

import com.felixgrund.codestory.ast.services.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class Environment {

	private RepositoryService repositoryService;

	private String fileName;
	private String filePath;
	private String startCommitName;
	private String methodName;
	private int startLine;
	private String fileExtension;
	private RevCommit startCommit;

	public Environment(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public String getRepositoryName() {
		return repositoryService.getRepositoryName();
	}

	public Repository getRepository() {
		return repositoryService.getRepository();
	}

	public Git getGit() {
		return repositoryService.getGit();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		String[] pathSplit = filePath.split("/");
		this.fileName = pathSplit[pathSplit.length-1];
	}

	public String getStartCommitName() {
		return startCommitName;
	}

	public void setStartCommitName(String startCommitName) {
		this.startCommitName = startCommitName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public String getRepositoryPath() {
		return repositoryService.getRepositoryPath();
	}

	public RevCommit getStartCommit() {
		return startCommit;
	}

	public void setStartCommit(RevCommit startCommit) {
		this.startCommit = startCommit;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public String getFileName() {
		return fileName;
	}
}
