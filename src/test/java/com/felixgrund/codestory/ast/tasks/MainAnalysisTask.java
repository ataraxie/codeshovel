package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.util.Environment;
import com.felixgrund.codestory.ast.util.Utl;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainAnalysisTask {

	public static final boolean PRINT_RESULT = false;

//	private static final String LANG = "java";
	private static final String LANG = "js";
//	private static final String TEST_CONFIG = "checkstyle-Checker-fireErrors";
	private static final String TEST_CONFIG = "pocketquery-admin-onFormSubmit";

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");

	public static void main(String[] args) throws Exception {
		execute();
	}

	public static void execute() throws Exception {
		String configName = TEST_CONFIG;
		ClassLoader classLoader = MainAnalysisTask.class.getClassLoader();
		File file = new File(classLoader.getResource("stubs/" + LANG + "/" + configName + ".json").getFile());
		String json = FileUtils.readFileToString(file, "utf-8");
		Gson gson = new Gson();
		RunConfig runConfig = gson.fromJson(json, RunConfig.class);
		runConfig.setConfigName(configName);

		String[] pathSplit = runConfig.getFilePath().split("/");
		String filename = pathSplit[pathSplit.length-1];

		System.out.println("Running dynamic test for config: " + configName);

		Environment env = new Environment();
		env.setRepository(Utl.createRepository(CODESTORY_REPO_DIR + "/" + runConfig.getRepoName() + "/.git"));
		env.setStartCommitName(runConfig.getStartCommitName());

		AnalysisTask task = new AnalysisTask(env);

		task.setFilePath(runConfig.getFilePath());
		task.setFunctionName(runConfig.getFunctionName());
		task.setFunctionStartLine(runConfig.getFunctionStartLine());


		new RecursiveAnalysisTask(env, task).run();

	}

	private static void printResult(AnalysisTask task, RunConfig runConfig) {

		Yresult yresult = task.getYresult();

		List<String> jsonMessages = new ArrayList<>();
		for (Ycommit ycommit : yresult.keySet()) {
			String message = "\"" + ycommit.getCommit().getName() + "\"";
			message = "\"" + ycommit.getCommit().getName() + "\" : \"" + yresult.get(ycommit) + "\"";
			jsonMessages.add(message);
		}
		System.out.println("\nMethod history JSON...");
		System.out.println(StringUtils.join(jsonMessages, ",\n"));

		Set<String> intellijLog = runConfig.getIntellijLog();
		Set<String> codestoryLog = runConfig.getCodestoryLog();
		if (intellijLog != null && codestoryLog != null) {
			Set<String> onlyIntellijLog = new LinkedHashSet<>(intellijLog);
			onlyIntellijLog.removeAll(codestoryLog);
			Set<String> onlyCodestoryLog = new LinkedHashSet<>(codestoryLog);
			onlyCodestoryLog.removeAll(intellijLog);

			System.out.println("\nOnly found in IntelliJ log...");
			System.out.println(StringUtils.join(onlyIntellijLog, "\n"));
			System.out.println("\nOnly found in CodeStory log...");
			System.out.println(StringUtils.join(onlyCodestoryLog, "\n"));
		}
	}

}
