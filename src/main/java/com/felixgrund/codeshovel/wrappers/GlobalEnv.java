package com.felixgrund.codeshovel.wrappers;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GlobalEnv {

	public static final String REPO_DIR = System.getenv("REPO_DIR");
	public static final String ENV_NAME = getEnvOptional("ENV_NAME", ""); // = System.getenv("ENV_NAME");
	public static final String OUTPUT_DIR = getEnvOptional("OUTPUT_DIR", System.getProperty("user.dir") + "/output");

	public static final boolean DISABLE_ALL_OUTPUTS = Boolean.valueOf(System.getenv("DISABLE_ALL_OUTPUTS"));
	public static final boolean WRITE_GIT_DIFFS = Boolean.valueOf(System.getenv("WRITE_GIT_DIFFS"));
	public static final boolean WRITE_SEMANTIC_DIFFS = Boolean.valueOf(System.getenv("WRITE_SEMANTIC_DIFFS"));
	public static final boolean WRITE_RESULTS = Boolean.valueOf(System.getenv("WRITE_RESULTS"));
	public static final boolean WRITE_GITLOG = Boolean.valueOf(System.getenv("WRITE_GITLOG"));
	public static final boolean WRITE_STUBS = Boolean.valueOf(System.getenv("WRITE_STUBS"));
	public static final boolean WRITE_SIMILARITIES = Boolean.valueOf(System.getenv("WRITE_SIMILARITIES"));

	public static final String LANG = getEnvOptional("LANG", "java");

	// Getters/Setters skipping
	public static final boolean SKIP_GETTERS_AND_SETTERS = Boolean.valueOf(System.getenv("SKIP_GETTERS_AND_SETTERS"));

	// Only for mining:
	public static final String TARGET_FILE_PATH = System.getenv("TARGET_FILE_PATH");
	public static final String REPO = System.getenv("REPO");
	public static final String START_COMMIT = System.getenv("START_COMMIT");

	// For mining and testing
	public static int MAX_RUNS = -1;
	public static int BEGIN_INDEX = -1;
	static {
		try {
			MAX_RUNS = Integer.parseInt(System.getenv("MAX_RUNS"));
			BEGIN_INDEX = Integer.parseInt(System.getenv("BEGIN_INDEX"));
		} catch (NumberFormatException e) {
			// leave -1
		}
	}

	public static List<String> SKIP_ENVS = new ArrayList<>();
	static {
		String skipString = System.getenv("SKIP_ENVS");
		if (StringUtils.isNotBlank(skipString)) {
			String[] commaSplit = StringUtils.split(skipString, ",");
			if (commaSplit.length > 0) {
				SKIP_ENVS = Arrays.asList(commaSplit);
			}
		}
	}

	private static String getEnvOptional(String envVar, String defaultVal) {
		return Optional.ofNullable(
				System.getenv(envVar)).orElse(defaultVal);
	}


}
