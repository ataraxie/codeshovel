package com.felixgrund.codestory.ast.execution;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.json.JsonChangeHistoryDiff;
import com.felixgrund.codestory.ast.json.JsonResult;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.services.RepositoryService;
import com.felixgrund.codestory.ast.tasks.AnalysisTask;
import com.felixgrund.codestory.ast.tasks.GitRangeLogTask;
import com.felixgrund.codestory.ast.tasks.RecursiveAnalysisTask;
import com.felixgrund.codestory.ast.wrappers.Environment;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MiningExecution {

	private static final Logger log = LoggerFactory.getLogger(MiningExecution.class);

	private Environment startEnv;
	private RepositoryService repositoryService;

	private Set<String> fileHistoryCommits;

	public MiningExecution(Environment startEnv) {
		this.startEnv = startEnv;
		this.repositoryService = startEnv.getRepositoryService();
	}

	public void execute() throws Exception {

		List<String> filePaths = repositoryService.findFilesByExtension(startEnv.getStartCommit(), startEnv.getFileExtension());
		for (String filePath : filePaths) {
			if (startEnv.getFilePath() == null || filePath.contains(startEnv.getFilePath())) {
				runForFile(filePath);
			}
		}
	}

	private void runForFile(String filePath) throws Exception {
		printFileStart(filePath);
		String startFileContent = repositoryService.findFileContent(startEnv.getStartCommit(), filePath);
		Yparser parser = ParserFactory.getParser(this.startEnv, filePath, startFileContent, startEnv.getStartCommit());
		for (Yfunction method : parser.getAllFunctions()) {
			if (startEnv.getMethodName() == null || startEnv.getMethodName().equals(method.getName())) {
				if (startEnv.getStartLine() <= 0 || startEnv.getStartLine() == method.getNameLineNumber()) {
					runForMethod(filePath, method);
				}
			}
		}
		printFileEnd(filePath);
	}

	private void runForMethod(String filePath, Yfunction method) throws Exception {
		printMethodStart(method);

		String name = method.getName();
		int lineNumber = method.getNameLineNumber();
		AnalysisTask task = new AnalysisTask(startEnv);
		task.setFilePath(filePath);
		task.setFunctionName(name);
		task.setFunctionStartLine(lineNumber);

		RecursiveAnalysisTask recursiveAnalysisTask = new RecursiveAnalysisTask(startEnv, task);
		recursiveAnalysisTask.run();

		if (this.fileHistoryCommits == null) {
			this.fileHistoryCommits = task.getFileHistory().keySet();
		}

		Yresult yresult = recursiveAnalysisTask.getResult();
		List<String> codestoryChangeHistory = new ArrayList<>();
		Map<String, Ychange> changeHistoryDetails = new LinkedHashMap<>();

		log.info("Creating method history and writing git diffs for result history...");
		for (Ycommit ycommit : yresult.keySet()) {
			String commitName = ycommit.getName();
			codestoryChangeHistory.add(commitName);
			changeHistoryDetails.put(commitName, yresult.get(ycommit));
			Yfunction matchedFunction = ycommit.getMatchedFunction();
			String diffFilepath = ycommit.getFilePath();
			if (matchedFunction != null) {
				diffFilepath = ycommit.getMatchedFunction().getSourceFilePath();
			}
			Utl.writeGitDiff(commitName, diffFilepath, startEnv.getRepository(), startEnv.getRepositoryName());
		}
		JsonResult jsonResultCodestory = new JsonResult("codestory", task, codestoryChangeHistory, changeHistoryDetails);
		Utl.writeJsonResultToFile(jsonResultCodestory);

		GitRangeLogTask gitRangeLogTask = new GitRangeLogTask(task, startEnv);
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
