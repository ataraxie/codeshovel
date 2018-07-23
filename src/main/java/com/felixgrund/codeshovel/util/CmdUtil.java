package com.felixgrund.codeshovel.util;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.RevCommit;
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

	public static String gitDiffParent(String commitName, String filePath, File repositoryDir) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		String diffCommand = String.format("git diff --unified=50 %s~1:%s %s:%s", commitName, filePath, commitName, filePath);
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


	public static BufferedReader gitLog(String startCommitName, File repositoryDir,
			  int rangeStart, int rangeEnd, String filePath) throws Exception {

		Runtime runtime = Runtime.getRuntime();
		String logCommand = String.format("git log %s --no-merges -L %s,%s:%s",
				startCommitName, rangeStart, rangeEnd, filePath);

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
		return new BufferedReader(new InputStreamReader(process.getInputStream()));
	}

}
