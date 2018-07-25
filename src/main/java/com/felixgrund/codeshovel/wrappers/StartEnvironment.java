package com.felixgrund.codeshovel.wrappers;

import com.felixgrund.codeshovel.services.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.util.LinkedHashMap;
import java.util.List;

public class StartEnvironment {

	private String envName;

	private RepositoryService repositoryService;

	private String repositoryName;
	private String repositoryPath;

	private String fileName;
	private String filePath;
	private String startCommitName;
	private String functionName;
	private int functionStartLine;
	private Commit startCommit;

	private LinkedHashMap<String, String> expectedResult;
	private List<String> baseline;

	public StartEnvironment(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
		this.repositoryName = repositoryService.getRepositoryName();
		this.repositoryPath = repositoryService.getRepositoryPath();
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
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
	}

	public String getStartCommitName() {
		return startCommitName;
	}

	public void setStartCommitName(String startCommitName) {
		this.startCommitName = startCommitName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Commit getStartCommit() {
		return startCommit;
	}

	public void setStartCommit(Commit startCommit) {
		this.startCommit = startCommit;
	}

	public int getFunctionStartLine() {
		return functionStartLine;
	}

	public void setFunctionStartLine(int functionStartLine) {
		this.functionStartLine = functionStartLine;
	}

	public String getFileName() {
		return fileName;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public LinkedHashMap<String, String> getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(LinkedHashMap<String, String> expectedResult) {
		this.expectedResult = expectedResult;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setBaseline(List<String> baseline) {
		this.baseline = baseline;
	}

	public List<String> getBaseline() {
		return baseline;
	}
}