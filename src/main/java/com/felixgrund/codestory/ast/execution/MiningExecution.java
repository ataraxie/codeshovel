package com.felixgrund.codestory.ast.execution;

import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.tasks.AnalysisTask;
import com.felixgrund.codestory.ast.tasks.RecursiveAnalysisTask;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;

public class MiningExecution {

	private String repositoryName;
	private String repositoryPath;
	private String filePath;
	private String fileName;
	private String startCommit;
	private String functionName;


	public void execute() throws Exception {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(this.repositoryPath))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		Git git = new Git(repository);
		RevCommit startCommit = Utl.findCommitByName(repository, this.startCommit);
		String startFileContent = Utl.findFileContent(repository, startCommit, this.filePath);

		Yparser parser = ParserFactory.getParser(this.fileName, startFileContent);
		parser.parse();

		for (Yfunction method : parser.getAllFunctions()) {
			if (this.functionName != null && !functionName.equals(method.getName())) {
				continue;
			}
			String name = method.getName();
			int lineNumber = method.getNameLineNumber();
			AnalysisTask task = new AnalysisTask();
			task.setRepositoryName(this.repositoryName);
			task.setRepository(repository);
			task.setFilePath(this.filePath);
			task.setFunctionName(name);
			task.setFunctionStartLine(lineNumber);
			task.setStartCommitName(this.startCommit);

			RecursiveAnalysisTask recursiveAnalysisTask = new RecursiveAnalysisTask(task);
//			recursiveAnalysisTask.setPrintOutput(false);
			recursiveAnalysisTask.run();
		}
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setStartCommit(String startCommit) {
		this.startCommit = startCommit;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
}
