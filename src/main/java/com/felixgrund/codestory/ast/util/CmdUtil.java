package com.felixgrund.codestory.ast.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdUtil {

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

}
