package com.felixgrund.codestory.ast.util;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdUtil {

	private static final Pattern COMMIT_NAME_PATTERN = Pattern.compile(".*([a-z0-9]{40}).*");

	public static String gitDiffParent(String commitName, String filePath, File repositoryDir) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		String diffCommand = String.format("git diff --unified=50 %s~1 %s %s", commitName, commitName, filePath);
		String[] cmd = {
				"/bin/sh",
				"-c",
				diffCommand
		};

		Process process = runtime.exec(cmd, null, repositoryDir);
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		return IOUtils.toString(reader);
	}


	public static List<String> gitLog(Git git, Repository repository, String startCommitName,
									  int rangeStart, int rangeEnd, String filePath) throws Exception {

		RevCommit startCommit = Utl.findCommitByName(repository, startCommitName);

		LogCommand logCommandFile = git.log().add(startCommit).addPath(filePath).setRevFilter(RevFilter.NO_MERGES);
		Iterable<RevCommit> fileRevisions = logCommandFile.call();
		Map<String, RevCommit> fileHistory = new LinkedHashMap<>();
		for (RevCommit commit : fileRevisions) {
			fileHistory.put(commit.getName(), commit);
		}

		List<String> commitNames = new ArrayList<>();
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

		File repositoryDir = repository.getDirectory().getParentFile();
		Process process = runtime.exec(cmd, null, repositoryDir);
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line = reader.readLine();
		while (line != null) {
			Matcher matcher = COMMIT_NAME_PATTERN.matcher(line);
			if (matcher.matches() && matcher.groupCount() > 0) {
				String commitName = matcher.group(1);
				RevCommit commit = fileHistory.get(commitName);
				if (commit != null && commit.getCommitTime() <= startCommit.getCommitTime()) {
					commitNames.add(commitName);
				}
			}
			line = reader.readLine();
		}

		reader.close();

		return commitNames;
	}

}
