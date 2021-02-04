package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.execution.ShovelExecution;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.services.impl.CachingRepositoryService;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.GlobalEnv;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.io.File;
import java.util.Date;

public class MainSingleOracle {

	private static final String ORACLE_DIR = "oracles/" + GlobalEnv.LANG;

	public static void main(String[] args) throws Exception {
		long start = new Date().getTime();
		execute();
		long end = new Date().getTime();
		System.out.println("Time taken: " + (end - start) / 1000 + "sec");
	}

	public static void execute() throws Exception {
		String configName = GlobalEnv.ENV_NAMES.get(0);
		ClassLoader classLoader = MainSingleOracle.class.getClassLoader();
		File file = new File(classLoader.getResource(ORACLE_DIR + "/" + configName + ".json").getFile());
		String json = FileUtils.readFileToString(file, "utf-8");
		Gson gson = new Gson();

		StartEnvironment startEnv = gson.fromJson(json, StartEnvironment.class);
		String repositoryName = startEnv.getRepositoryName();
		String repositoryPath = GlobalEnv.REPO_DIR + "/" + repositoryName + "/.git";
		startEnv.setRepositoryPath(repositoryPath);

		Repository repository = Utl.createRepository(repositoryPath);
		Git git = new Git(repository);
		RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);
		Commit startCommit = repositoryService.findCommitByName(startEnv.getStartCommitName());

		startEnv.setRepositoryService(repositoryService);
		startEnv.setStartCommit(startCommit);
		startEnv.setEnvName(configName);
		startEnv.setStartCommit(startCommit);
		startEnv.setFileName(Utl.getFileName(startEnv.getFilePath()));

		System.out.println("Running dynamic test for config: " + configName);

		ShovelExecution.runSingle(startEnv, startEnv.getFilePath(), true);

	}

}
