package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.entities.Ycommit;
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

		Runtime runtime = Runtime.getRuntime();
		String logCommand = String.format("git log %s --no-merges -L %s,%s:%s", startCommitName, rangeStart, rangeEnd, filePath);
		logCommand += " | grep 'commit\\s' | sed 's/commit//'";
		String[] cmd = {
			"/bin/sh",
			"-c",
			logCommand
		};

		System.out.println("\n==================================");
		System.out.println("LogCommand: " + logCommand);

		Process process = runtime.exec(cmd, null, repositoryDir);
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

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
