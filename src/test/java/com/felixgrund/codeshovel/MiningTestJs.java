package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.execution.ShovelExecution;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.services.impl.CachingRepositoryService;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.util.Utl;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class MiningTestJs {

	private static final boolean METHOD_MODE = false;

	private static final String TARGET_FILE_EXTENSION = ".js";
	private static final String TARGET_FILE_PATH = "src/css.js";
	private static final String TARGET_METHOD = "style";
	private static final int TARGET_METHOD_STARTLINE = 208;
	private static final String CODESTORY_REPO_DIR = System.getenv("codeshovel.repo.dir");
	private static final String REPO = "jquery";
	private static final String START_COMMIT = "45f085882597016e521436f01a8459daf3e4000e";

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		Repository repository = Utl.createRepository(repositoryPath);
		Git git = new Git(repository);

		RepositoryService repositoryService = new CachingRepositoryService(git, repository, REPO, repositoryPath);

		RevCommit startCommit = repositoryService.findCommitByName(START_COMMIT);

		StartEnvironment env = new StartEnvironment(repositoryService);

		env.setFilePath(TARGET_FILE_PATH);
		env.setStartCommitName(START_COMMIT);
		env.setStartCommit(startCommit);

		if (METHOD_MODE) {
			env.setFunctionName(TARGET_METHOD);
			env.setFunctionStartLine(TARGET_METHOD_STARTLINE);
		}

		ShovelExecution.runMining(env, TARGET_FILE_EXTENSION);
	}

}
