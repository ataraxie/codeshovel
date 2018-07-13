package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.execution.MiningExecution;
import com.felixgrund.codestory.ast.services.RepositoryService;
import com.felixgrund.codestory.ast.services.impl.CachingRepositoryService;
import com.felixgrund.codestory.ast.wrappers.Environment;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class MiningTestJs {

	private static final boolean METHOD_MODE = true;

	private static final String TARGET_FILE_EXTENSION = ".js";
	private static final String TARGET_FILE_PATH = "src/css.js";
	private static final String TARGET_METHOD = "style";
	private static final int TARGET_METHOD_STARTLINE = 208;
	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
	private static final String REPO = "jquery";
	private static final String START_COMMIT = "45f085882597016e521436f01a8459daf3e4000e";

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		Repository repository = Utl.createRepository(repositoryPath);
		Git git = new Git(repository);

		RepositoryService repositoryService = new CachingRepositoryService(git, repository, REPO, repositoryPath);

		RevCommit startCommit = repositoryService.findCommitByName(START_COMMIT);

		Environment env = new Environment(repositoryService);

		env.setFilePath(TARGET_FILE_PATH);
		env.setStartCommitName(START_COMMIT);
		env.setMethodName(TARGET_METHOD);
		env.setStartLine(TARGET_METHOD_STARTLINE);
		env.setFileExtension(TARGET_FILE_EXTENSION);
		env.setStartCommit(startCommit);

		MiningExecution execution = new MiningExecution(env);
		execution.execute();
	}

}
