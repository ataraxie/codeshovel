package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.execution.MiningExecution;

public class MiningTestJs {

	private static final String TARGET_FILE_EXTENSION = ".js";
//	private static final String TARGET_FILE_PATH = "src";
	private static final String TARGET_FILE_PATH = "src/ajax.js";
	private static final String TARGET_METHOD = "ajax";
//	private static final String TARGET_METHOD = "cloneCopyEvent";
	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
	private static final String REPO = "jquery";
//	private static final String START_COMMIT = "46ea7a3f0e8893a420e4c3321dc3aca40d96f754";
	private static final String START_COMMIT = "ab3ba4a81252c4357a7aab5f24d765d41d47986e";
	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		MiningExecution execution = new MiningExecution(TARGET_FILE_EXTENSION);
		execution.setRepositoryName(REPO);
		execution.setRepositoryPath(repositoryPath);
		execution.setStartCommitName(START_COMMIT);

		execution.setOnlyFilePath(TARGET_FILE_PATH);
//		execution.setOnlyMethodName(TARGET_METHOD);

		execution.execute();
	}

}
