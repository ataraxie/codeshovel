package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.util.CmdUtil;
import com.felixgrund.codestory.ast.util.Environment;

import java.util.List;
import java.util.regex.Pattern;

public class GitRangeLogTask {

	private static final Pattern COMMIT_NAME_PATTERN = Pattern.compile(".*([a-z0-9]{40}).*");

	private AnalysisTask startTask;
	private List<String> result;

	public GitRangeLogTask(AnalysisTask startTask) {
		this.startTask = startTask;
	}

	public void run() throws Exception {
		Environment startEnv = this.startTask.getStartEnv();

		int rangeStart = this.startTask.getFunctionStartLine();
		int rangeEnd = this.startTask.getFunctionEndLine();
		String filePath = this.startTask.getFilePath();
		String startCommitName = this.startTask.getStartCommitName();

		this.result = CmdUtil.gitLog(startEnv.getGit(), startEnv.getRepository(),
				startCommitName, rangeStart, rangeEnd, filePath);
	}


	public List<String> getResult() {
		return this.result;
	}

}
