package com.felixgrund.codestory.ast.execution;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.tasks.AnalysisTask;
import com.felixgrund.codestory.ast.tasks.GitRangeLogTask;
import com.felixgrund.codestory.ast.tasks.RecursiveAnalysisTask;
import com.felixgrund.codestory.ast.json.JsonChangeHistoryDiff;
import com.felixgrund.codestory.ast.json.JsonResult;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class MiningExecution {

	private static final Logger log = LoggerFactory.getLogger(MiningExecution.class);

	private String repositoryName;
	private String repositoryPath;
	private String startCommitName;
	private String onlyMethodName;
	private String onlyFilePath;

	private Repository repository;
	private RevCommit startCommit;

	private String targetFileExtension;

	private Set<String> fileHistoryCommits;

	public MiningExecution(String targetFileExtension) {
		this.targetFileExtension = targetFileExtension;
	}

	public void execute() throws Exception {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		this.repository = builder.setGitDir(new File(this.repositoryPath))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		new Git(this.repository);
		this.startCommit = Utl.findCommitByName(this.repository, this.startCommitName);

		List<String> filePaths = Utl.findFilesByExtension(this.repository, this.startCommit, this.targetFileExtension);
		for (String filePath : filePaths) {
			if (this.onlyFilePath == null || filePath.contains(this.onlyFilePath)) {
				runForFile(filePath);

				if (this.fileHistoryCommits != null) {
					log.info("Writing git diffs for file history...");
					for (String commitName : this.fileHistoryCommits) {
						Utl.writeGitDiff(commitName, filePath, this.repository, this.repositoryName);
					}
				}
			}
		}
	}

	private void runForFile(String filePath) throws Exception {
		printFileStart(filePath);
		String startFileContent = Utl.findFileContent(this.repository, this.startCommit, filePath);
		Yparser parser = ParserFactory.getParser(this.repositoryName, filePath, startFileContent, this.startCommitName);
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

		if (this.fileHistoryCommits == null) {
			this.fileHistoryCommits = task.getFileHistory().keySet();
		}

		Yresult yresult = recursiveAnalysisTask.getResult();
		List<String> codestoryChangeHistory = new ArrayList<>();
		Map<String, Ychange> changeHistoryDetails = new LinkedHashMap<>();
		for (Ycommit ycommit : yresult.keySet()) {
			String commitName = ycommit.getName();
			codestoryChangeHistory.add(commitName);
			changeHistoryDetails.put(commitName, yresult.get(ycommit));
		}
		JsonResult jsonResultCodestory = new JsonResult("codestory", task, codestoryChangeHistory, changeHistoryDetails);
		Utl.writeJsonResultToFile(jsonResultCodestory);

		GitRangeLogTask gitRangeLogTask = new GitRangeLogTask(task);
		gitRangeLogTask.run();
		List<String> gitRangeLogChangeHistory = gitRangeLogTask.getResult();
		JsonResult jsonResultLogCommand = new JsonResult("logcommand", task, gitRangeLogChangeHistory, null);
		Utl.printMethodHistory(gitRangeLogChangeHistory);
		Utl.writeJsonResultToFile(jsonResultLogCommand);

		List<String> onlyInCodestory = new ArrayList<>(codestoryChangeHistory);
		onlyInCodestory.removeAll(gitRangeLogChangeHistory);

		List<String> onlyInGitRangeLog = new ArrayList<>(gitRangeLogChangeHistory);
		onlyInGitRangeLog.removeAll(codestoryChangeHistory);

		if (onlyInCodestory.size() > 0 || onlyInGitRangeLog.size() > 0) {
			log.info("Found difference in change history. Writing files.");
			JsonChangeHistoryDiff diff = new JsonChangeHistoryDiff(codestoryChangeHistory, gitRangeLogChangeHistory,
					onlyInCodestory, onlyInGitRangeLog);
			Utl.writeSemanticDiff(jsonResultCodestory, diff);

			Set<String> commitsForDiff = new HashSet<>();
			commitsForDiff.addAll(onlyInCodestory);
			commitsForDiff.addAll(onlyInGitRangeLog);
		}

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
