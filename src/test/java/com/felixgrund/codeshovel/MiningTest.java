package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.execution.ShovelExecution;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.services.impl.CachingRepositoryService;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;

class MiningTest {

	static String TARGET_FILE_EXTENSION;
	static String TARGET_FILE_PATH;
	static String TARGET_METHOD;
	static int TARGET_METHOD_STARTLINE;
	static String CODESTORY_REPO_DIR;
	static String REPO;
	static String START_COMMIT;

	static void execMining() throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		Repository repository = Utl.createRepository(repositoryPath);
		Git git = new Git(repository);

		RepositoryService repositoryService = new CachingRepositoryService(git, repository, REPO, repositoryPath);

		Commit startCommit = repositoryService.findCommitByName(START_COMMIT);

		StartEnvironment env = new StartEnvironment(repositoryService);
		env.setFilePath(TARGET_FILE_PATH);
		env.setFileName(Utl.getFileName(TARGET_FILE_PATH));
		env.setStartCommitName(START_COMMIT);
		env.setStartCommit(startCommit);

		if (TARGET_METHOD != null) {
			env.setFunctionName(TARGET_METHOD);
			if (TARGET_METHOD_STARTLINE != 0) {
				env.setFunctionStartLine(TARGET_METHOD_STARTLINE);
			}
		}

		ShovelExecution.runMining(env, TARGET_FILE_EXTENSION);
	}

}
