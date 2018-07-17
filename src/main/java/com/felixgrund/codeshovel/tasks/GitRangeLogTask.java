package com.felixgrund.codeshovel.tasks;

import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

import java.util.List;

public class GitRangeLogTask {

	private AnalysisTask startTask;
	private List<String> result;

	private RepositoryService repositoryService;

	public GitRangeLogTask(AnalysisTask startTask, StartEnvironment startEnv) {
		this.startTask = startTask;
		this.repositoryService = startEnv.getRepositoryService();
	}

	public void run() throws Exception {
		int rangeStart = this.startTask.getFunctionStartLine();
		int rangeEnd = this.startTask.getFunctionEndLine();
		String filePath = this.startTask.getFilePath();
		String startCommitName = this.startTask.getStartCommitName();

		this.result = repositoryService.gitLogRange(startCommitName, rangeStart, rangeEnd, filePath);
	}


	public List<String> getResult() {
		return this.result;
	}

}
