package com.felixgrund.codestory.ast;

import com.felixgrund.codestory.ast.entities.CommitInfo;
import com.felixgrund.codestory.ast.interpreters.Interpreter;
import com.felixgrund.codestory.ast.tasks.CreateCommitInfoCollectionTask;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;

public class MainPocketQuery {

	private static final String PROJECT_DIR = System.getProperty("user.dir");

	public static void main(String[] args) {
		try {
			execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void execute() throws Exception {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File("/Users/felix/dev/projects_scandio/pocketquery/.git"))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		Git git = new Git(repository);

		CreateCommitInfoCollectionTask task = new CreateCommitInfoCollectionTask(repository);
		task.setBranchName("master");
		task.setFilePath("src/main/resources/pocketquery/js/pocketquery-admin.js");
		task.setFileName("pocketquery-admin.js");
		task.setFunctionName("onFormSubmit");
		task.setFunctionStartLine(438);
		task.setStartCommitName("0540bb23561ef9921f55a83bd8bf7cc91d471bf3");

		task.run();

		for (CommitInfo commitInfo : task.getResult()) {
			Interpreter interpreter = new Interpreter(commitInfo);
			interpreter.interpret();

			if (!interpreter.getFindings().isEmpty()) {
				System.out.println("\n"+commitInfo);
				System.out.println(interpreter.getFindings());
			}

		}

	}


}
