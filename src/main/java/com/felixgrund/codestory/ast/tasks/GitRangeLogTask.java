package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.util.CmdUtil;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitRangeLogTask {

	private static final Pattern COMMIT_NAME_PATTERN = Pattern.compile(".*([a-z0-9]{40}).*");

	private AnalysisTask startTask;
	private List<String> result;

	public GitRangeLogTask(AnalysisTask startTask) {
		this.startTask = startTask;
	}

	public void run() throws Exception {
		File repositoryDir = this.startTask.getRepository().getDirectory().getParentFile();
		int rangeStart = this.startTask.getFunctionStartLine();
		int rangeEnd = this.startTask.getFunctionEndLine();
		String filePath = this.startTask.getFilePath();
		String startCommitName = this.startTask.getStartCommitName();

		BufferedReader reader = CmdUtil.gitLog(startCommitName, rangeStart, rangeEnd, filePath, repositoryDir);

		this.result = new ArrayList<>();
		String line = reader.readLine();
		while (line != null) {
			Matcher matcher = COMMIT_NAME_PATTERN.matcher(line);
			if (matcher.matches() && matcher.groupCount() > 0) {
				String commitName = matcher.group(1);
				RevCommit commit = this.startTask.getFileHistory().get(commitName);
				RevCommit startCommit = startTask.getStartCommit().getCommit();
				if (commit != null && commit.getCommitTime() <= startCommit.getCommitTime()) {
					this.result.add(matcher.group(1));
				}
			}
			line = reader.readLine();
		}

		reader.close();
	}


	public List<String> getResult() {
		return this.result;
	}

}
