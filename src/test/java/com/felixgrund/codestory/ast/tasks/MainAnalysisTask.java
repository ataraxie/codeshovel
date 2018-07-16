package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.services.RepositoryService;
import com.felixgrund.codestory.ast.services.impl.CachingRepositoryService;
import com.felixgrund.codestory.ast.util.Utl;
import com.felixgrund.codestory.ast.wrappers.StartEnvironment;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainAnalysisTask {

	public static final boolean PRINT_RESULT = true;

	private static final String LANG = "java";
	private static final String TEST_CONFIG = "checkstyle-Checker-fireErrors";

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

		String repositoryName = runConfig.getRepoName();
		String repositoryPath = CODESTORY_REPO_DIR + "/" + repositoryName + "/.git";
		String filePath = runConfig.getFilePath();


		Repository repository = Utl.createRepository(repositoryPath);
		Git git = new Git(repository);

		System.out.println("Running dynamic test for config: " + configName);

		RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);

		RevCommit startCommit = repositoryService.findCommitByName(runConfig.getStartCommitName());

		StartEnvironment env = new StartEnvironment(repositoryService);
		env.setFilePath(filePath);
		env.setStartCommitName(runConfig.getStartCommitName());
		env.setMethodName(runConfig.getMethodName());
		env.setStartLine(runConfig.getMethodStartLine());
		env.setFileExtension("." + LANG);
		env.setStartCommit(startCommit);

		AnalysisTask task = new AnalysisTask(env);

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

		Set<String> gitLog = runConfig.getGitLog();
		Set<String> codestoryLog = runConfig.getCodestoryLog();
		if (gitLog != null && codestoryLog != null) {
			Set<String> onlyGitLog = new LinkedHashSet<>(gitLog);
			onlyGitLog.removeAll(codestoryLog);
			Set<String> onlyCodestoryLog = new LinkedHashSet<>(codestoryLog);
			onlyCodestoryLog.removeAll(gitLog);

			System.out.println("\nOnly found in IntelliJ log...");
			System.out.println(StringUtils.join(onlyGitLog, "\n"));
			System.out.println("\nOnly found in CodeStory log...");
			System.out.println(StringUtils.join(onlyCodestoryLog, "\n"));
		}
	}

}
