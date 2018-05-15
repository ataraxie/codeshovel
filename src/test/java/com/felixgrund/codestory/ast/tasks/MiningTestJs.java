package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.execution.MiningExecution;


public class MiningTestJs {

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
	private static final String REPO = "jquery";
	private static final String START_COMMIT = "46ea7a3f0e8893a420e4c3321dc3aca40d96f754";

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		MiningExecution execution = new MiningExecution();
		execution.setRepositoryName(REPO);
		execution.setRepositoryPath(repositoryPath);
		execution.setStartCommitName(START_COMMIT);

		execution.setOnlyFilePath("build/tasks/build.js");
//		execution.setOnlyMethodName("inspectPrefiltersOrTransports");

		execution.execute();

//		Runtime runtime = Runtime.getRuntime();
////		String logCommand = String.format("git log --no-merges -L %s,%s:%s", rangeStart, rangeEnd, filePath);
//		Process process = runtime.exec("git log --no-merges -L 10,808:src/ajax.js | grep 'commit\\s' | sed 's/commit//'", null, new File(CODESTORY_REPO_DIR + "/" + REPO));
//		process.waitFor();
//		int a = 1;
	}

}
