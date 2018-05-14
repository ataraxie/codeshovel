package com.felixgrund.codestory.ast.execution;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.tasks.AnalysisTask;
import com.felixgrund.codestory.ast.tasks.GitRangeLogTask;
import com.felixgrund.codestory.ast.tasks.RecursiveAnalysisTask;
import com.felixgrund.codestory.ast.util.JsonResult;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MiningExecution {

	private String repositoryName;
	private String repositoryPath;
	private String startCommitName;
	private String onlyMethodName;
	private String onlyFilePath;

	private Repository repository;
	private RevCommit startCommit;

	public void execute() throws Exception {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		this.repository = builder.setGitDir(new File(this.repositoryPath))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		Git git = new Git(repository);
		this.startCommit = Utl.findCommitByName(repository, this.startCommitName);

		if (this.onlyFilePath != null) {
			runForFile(this.onlyFilePath);
		} else {
			List<String> filePaths = Utl.findFilesByExtension(repository, startCommit, ".js");
			for (String filePath : filePaths) {
				runForFile(filePath);
			}
		}
	}

	private void runForFile(String filePath) throws Exception {
		printFileStart(filePath);
		String startFileContent = Utl.findFileContent(this.repository, this.startCommit, filePath);
		Yparser parser = ParserFactory.getParser(filePath, startFileContent);
		parser.parse();
		for (Yfunction method : parser.getAllFunctions()) {
			if (this.onlyMethodName == null || this.onlyMethodName.equals(method.getName())) {
				runForMethod(filePath, method);
			}
		}
		printFileEnd(filePath);
	}

	private void runForMethod(String filePath, Yfunction method) throws Exception {
		printMethodStart(method);

		String name = method.getName();
		int lineNumber = method.getNameLineNumber();
		AnalysisTask task = new AnalysisTask();
		task.setRepositoryName(this.repositoryName);
		task.setRepository(this.repository);
		task.setFilePath(filePath);
		task.setFunctionName(name);
		task.setFunctionStartLine(lineNumber);
		task.setStartCommitName(this.startCommitName);

		RecursiveAnalysisTask recursiveAnalysisTask = new RecursiveAnalysisTask(task);
		recursiveAnalysisTask.run();

		Yresult yresult = recursiveAnalysisTask.getResult();
		List<String> changeHistory = new ArrayList<>();
		List<String> changeHistoryDetails = new ArrayList<>();
		for (Ycommit ycommit : yresult.keySet()) {
			changeHistory.add(ycommit.getName());
			changeHistoryDetails.add(ycommit.getName() + ":" + yresult.get(ycommit));
		}
		JsonResult jsonResult = new JsonResult("codestory", task, changeHistory, changeHistoryDetails);
		Utl.writeJsonResultToFile(jsonResult);


		GitRangeLogTask gitRangeLogTask = new GitRangeLogTask(task);
		gitRangeLogTask.run();
		changeHistory = gitRangeLogTask.getResult();
		jsonResult = new JsonResult("logcommand", task, changeHistory, null);
		Utl.printMethodHistory(changeHistory);
		Utl.writeJsonResultToFile(jsonResult);

		printMethodEnd(method);
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public void setOnlyFilePath(String onlyFilePath) {
		this.onlyFilePath = onlyFilePath;
	}

	public void setStartCommitName(String startCommitName) {
		this.startCommitName = startCommitName;
	}

	public void setOnlyMethodName(String onlyMethodName) {
		this.onlyMethodName = onlyMethodName;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	private void printFileStart(String filePath) {
		System.out.println("#########################################################################");
		System.out.println("STARTING ANALYSIS FOR FILE " + filePath);
	}

	private void printFileEnd(String filePath) {
		System.out.println("FINISHED ANALYSIS FOR FILE " + filePath);
		System.out.println("#########################################################################");
	}

	private void printMethodStart(Yfunction method) {
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("STARTING ANALYSIS FOR METHOD " + method.getName());
	}

	private void printMethodEnd(Yfunction method) {
		System.out.println("FINISHED ANALYSIS FOR METHOD " + method.getName());
		System.out.println("-------------------------------------------------------------------------");
	}
}
