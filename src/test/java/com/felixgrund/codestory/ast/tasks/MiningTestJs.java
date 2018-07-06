package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.execution.MiningExecution;

public class MiningTestJs {

	private static final boolean METHOD_MODE = true;

	private static final String TARGET_FILE_EXTENSION = ".js";
	private static final String TARGET_FILE_PATH = "src/css.js";
	private static final String TARGET_METHOD = "style";
	private static final int TARGET_METHOD_STARTLINE = 208;
	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
	private static final String REPO = "jquery";
	private static final String START_COMMIT = "45f085882597016e521436f01a8459daf3e4000e";

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		MiningExecution execution = new MiningExecution(TARGET_FILE_EXTENSION);
		execution.setRepositoryName(REPO);
		execution.setRepositoryPath(repositoryPath);
		execution.setStartCommitName(START_COMMIT);

		execution.setOnlyFilePath(TARGET_FILE_PATH);

		if (METHOD_MODE) {
			execution.setOnlyMethodName(TARGET_METHOD);
			execution.setOnlyStartLine(TARGET_METHOD_STARTLINE);
		}

		execution.execute();
	}

}
