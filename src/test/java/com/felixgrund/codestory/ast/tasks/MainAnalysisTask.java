package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.execution.ShovelExecution;
import com.felixgrund.codestory.ast.services.RepositoryService;
import com.felixgrund.codestory.ast.services.impl.CachingRepositoryService;
import com.felixgrund.codestory.ast.util.Utl;
import com.felixgrund.codestory.ast.wrappers.StartEnvironment;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;

public class MainAnalysisTask {

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

		StartEnvironment startEnv = gson.fromJson(json, StartEnvironment.class);
		String repositoryName = startEnv.getRepositoryName();
		String repositoryPath = CODESTORY_REPO_DIR + "/" + repositoryName + "/.git";
		startEnv.setRepositoryPath(repositoryPath);

		Repository repository = Utl.createRepository(repositoryPath);
		Git git = new Git(repository);
		RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);
		RevCommit startCommit = repositoryService.findCommitByName(startEnv.getStartCommitName());

		startEnv.setRepositoryService(repositoryService);
		startEnv.setStartCommit(startCommit);
		startEnv.setEnvName(configName);
		startEnv.setStartCommit(startCommit);
		startEnv.setFileName(Utl.getFileName(startEnv.getFilePath()));

		System.out.println("Running dynamic test for config: " + configName);

		ShovelExecution.runSingle(startEnv);

	}

}
