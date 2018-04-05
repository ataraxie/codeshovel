package com.felixgrund.codestory.ast.tasks.mainclasses;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.tasks.AnalysisLevel1Task;
import com.felixgrund.codestory.ast.tasks.RunConfig;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class MainAnalysis1Task {

	private static final String TEST_CONFIG = "pocketquery-admin-updateQueryInUsageListItems";

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");

	public static void main(String[] args) throws Exception {
		String configName = TEST_CONFIG;
		ClassLoader classLoader = MainAnalysis1Task.class.getClassLoader();
		File file = new File(classLoader.getResource("stubs/" + configName + ".json").getFile());
		String json = FileUtils.readFileToString(file, "utf-8");
		Gson gson = new Gson();
		RunConfig runConfig = gson.fromJson(json, RunConfig.class);
		runConfig.setConfigName(file.getName().replace(".json", ""));

		System.out.println("Running dynamic test for config: " + configName);

		AnalysisLevel1Task task = new AnalysisLevel1Task();
		task.setRepository(CODESTORY_REPO_DIR + "/" + runConfig.getRepoName() + "/.git");
		task.setBranchName(runConfig.getBranchName());
		task.setFilePath(runConfig.getFilePath());
		task.setFileName(runConfig.getFileName());
		task.setFunctionName(runConfig.getFunctionName());
		task.setFunctionStartLine(runConfig.getFunctionStartLine());
		task.setStartCommitName(runConfig.getStartCommitName());

		task.run();

		Yresult yresult = task.getYresult();
		System.out.println("File history...");
		for (Ycommit ycommit : task.getYhistory()) {
			System.out.println(ycommit.getCommit().getName());
		}

		System.out.println("\nMethod history...");
		for (Ycommit ycommit : yresult.keySet()) {
			System.out.println("\"" + ycommit.getCommit().getName() + "\" : \"" + yresult.get(ycommit) + "\",");
		}
	}

}
