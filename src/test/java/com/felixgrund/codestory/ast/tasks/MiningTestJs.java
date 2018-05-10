package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.execution.MiningExecution;


public class MiningTestJs {

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
	private static final String REPO = "jquery";
	private static final String FILE_PATH = "src/ajax.js";
	private static final String FILENAME = "ajax.js";
	private static final String START_COMMIT = "46ea7a3f0e8893a420e4c3321dc3aca40d96f754";

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		MiningExecution execution = new MiningExecution();
		execution.setRepositoryPath(repositoryPath);
		execution.setFileName(FILENAME);
		execution.setFilePath(FILE_PATH);
		execution.setStartCommit(START_COMMIT);
		execution.setFunctionName("inspectPrefiltersOrTransports");

		execution.execute();

	}

}
