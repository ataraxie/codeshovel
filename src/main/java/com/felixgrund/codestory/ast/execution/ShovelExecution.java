package com.felixgrund.codestory.ast.execution;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.json.JsonChangeHistoryDiff;
import com.felixgrund.codestory.ast.json.JsonResult;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.tasks.AnalysisTask;
import com.felixgrund.codestory.ast.tasks.GitRangeLogTask;
import com.felixgrund.codestory.ast.tasks.RecursiveAnalysisTask;
import com.felixgrund.codestory.ast.wrappers.StartEnvironment;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ShovelExecution {

	private static final Logger log = LoggerFactory.getLogger(ShovelExecution.class);

	public static Yresult runMining(StartEnvironment startEnv, String acceptedFileExtension) throws Exception {
		Yresult yresult = new Yresult();
		printMiningStart(startEnv);
		List<String> filePaths = startEnv.getRepositoryService().findFilesByExtension(startEnv.getStartCommit(), acceptedFileExtension);
		for (String filePath : filePaths) {
			if (startEnv.getFilePath() == null || filePath.contains(startEnv.getFilePath())) {
				yresult.putAll(runSingle(startEnv));
			}
		}
		printMiningEnd(startEnv);

		return yresult;
	}

	public static Yresult runSingle(StartEnvironment startEnv) throws Exception {
		Yresult yresult = new Yresult();
		String filePath = startEnv.getFilePath();
		printFileStart(filePath);
		String startFileContent = startEnv.getRepositoryService().findFileContent(startEnv.getStartCommit(), filePath);
		Yparser parser = ParserFactory.getParser(startEnv, filePath, startFileContent, startEnv.getStartCommit());
		for (Yfunction method : parser.getAllFunctions()) {
			if (startEnv.getFunctionName() == null || startEnv.getFunctionName().equals(method.getName())) {
				if (startEnv.getFunctionStartLine() <= 0 || startEnv.getFunctionStartLine() == method.getNameLineNumber()) {
					yresult.putAll(runForMethod(startEnv, filePath, method));
				}
			}
		}
		printFileEnd(filePath);
		return yresult;
	}

	private static Yresult runForMethod(StartEnvironment startEnv, String filePath, Yfunction method) throws Exception {
		printMethodStart(method);

		String name = method.getName();
		int lineNumber = method.getNameLineNumber();
		AnalysisTask task = new AnalysisTask(startEnv);
		task.setFilePath(filePath);
		task.setFunctionName(name);
		task.setFunctionStartLine(lineNumber);

		RecursiveAnalysisTask recursiveAnalysisTask = new RecursiveAnalysisTask(startEnv, task);
		recursiveAnalysisTask.run();

		Yresult yresult = recursiveAnalysisTask.getResult();
		List<String> codestoryChangeHistory = new ArrayList<>();
		Map<String, Ychange> changeHistoryDetails = new LinkedHashMap<>();
		Map<String, String> changeHistoryShort = new LinkedHashMap<>();

		log.trace("Creating method history and writing git diffs for result history...");
		for (Ycommit ycommit : yresult.keySet()) {
			String commitName = ycommit.getName();
			codestoryChangeHistory.add(commitName);
			Ychange change = yresult.get(ycommit);
			changeHistoryDetails.put(commitName, change);
			changeHistoryShort.put(commitName, change.getTypeAsString());
			Yfunction matchedFunction = ycommit.getMatchedFunction();
			String diffFilepath = ycommit.getFilePath();
			if (matchedFunction != null) {
				diffFilepath = ycommit.getMatchedFunction().getSourceFilePath();
			}

			// FIXME: This produces empty files right now
			// Utl.writeGitDiff(commitName, diffFilepath, startEnv.getRepository(), startEnv.getRepositoryName());
		}
		JsonResult jsonResultCodestory = new JsonResult("codestory", task, codestoryChangeHistory, changeHistoryDetails, changeHistoryShort);
		Utl.writeJsonResultToFile(jsonResultCodestory);

		GitRangeLogTask gitRangeLogTask = new GitRangeLogTask(task, startEnv);
		gitRangeLogTask.run();
		List<String> gitRangeLogChangeHistory = gitRangeLogTask.getResult();
		JsonResult jsonResultLogCommand = new JsonResult("logcommand", task, gitRangeLogChangeHistory, null, null);
		Utl.printMethodHistory(gitRangeLogChangeHistory);
		Utl.writeJsonResultToFile(jsonResultLogCommand);

		List<String> onlyInCodestory = new ArrayList<>(codestoryChangeHistory);
		onlyInCodestory.removeAll(gitRangeLogChangeHistory);

		List<String> onlyInGitRangeLog = new ArrayList<>(gitRangeLogChangeHistory);
		onlyInGitRangeLog.removeAll(codestoryChangeHistory);

		if (onlyInCodestory.size() > 0 || onlyInGitRangeLog.size() > 0) {
			log.trace("Found difference in change history. Writing files.");
			JsonChangeHistoryDiff diff = new JsonChangeHistoryDiff(codestoryChangeHistory, gitRangeLogChangeHistory,
					onlyInCodestory, onlyInGitRangeLog);
			Utl.writeSemanticDiff(jsonResultCodestory, diff);

			Set<String> commitsForDiff = new HashSet<>();
			commitsForDiff.addAll(onlyInCodestory);
			commitsForDiff.addAll(onlyInGitRangeLog);
		}

		printMethodEnd(method);

		return yresult;
	}
	
	private static void printFileStart(String filePath) {
		System.out.println("#########################################################################");
		System.out.println("STARTING ANALYSIS FOR FILE " + filePath);
	}

	private static void printFileEnd(String filePath) {
		System.out.println("FINISHED ANALYSIS FOR FILE " + filePath);
		System.out.println("#########################################################################");
	}

	private static void printMethodStart(Yfunction method) {
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("STARTING ANALYSIS FOR METHOD " + method.getName());
	}

	private static void printMethodEnd(Yfunction method) {
		System.out.println("FINISHED ANALYSIS FOR METHOD " + method.getName());
		System.out.println("-------------------------------------------------------------------------");
	}

	private static void printMiningEnd(StartEnvironment startEnv) {
		System.out.println("FINISHED MINING ANALYSIS");
		System.out.println("#########################################################################");
	}

	private static void printMiningStart(StartEnvironment startEnv) {
		System.out.println("#########################################################################");
		System.out.println("STARTING MINING ANALYSIS FOR REPO AND PATH: " + startEnv.getRepositoryName() + " - " + startEnv.getFilePath());
	}

}
