package com.felixgrund.codeshovel.execution;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.entities.Yresult;
import com.felixgrund.codeshovel.json.JsonChangeHistoryDiff;
import com.felixgrund.codeshovel.json.JsonResult;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.tasks.AnalysisTask;
import com.felixgrund.codeshovel.tasks.GitRangeLogTask;
import com.felixgrund.codeshovel.tasks.RecursiveAnalysisTask;
import com.felixgrund.codeshovel.util.ParserFactory;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.GlobalEnv;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.github.javaparser.ParserConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ShovelExecution {

	private static long duration;

	private static final Logger log = LoggerFactory.getLogger(ShovelExecution.class);
	static {
		com.github.javaparser.JavaParser.setStaticConfiguration(
				new ParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.RAW));
	}

	public static Yresult runMining(StartEnvironment startEnv, String acceptedFileExtension) throws Exception {
		// We stopped gathering results for mining executions in heap space (which was crazy anyways).
		// So this result will remain empty. The result files are written within the executions.
		Yresult yresult = new Yresult();
		printMiningStart(startEnv);
		List<String> filePaths = startEnv.getRepositoryService().findFilesByExtension(startEnv.getStartCommit(), acceptedFileExtension);
		List<String> filePathsToConsider = new ArrayList<>();
		for (String filePath : filePaths) {
			if (startEnv.getFilePath() == null || filePath.contains(startEnv.getFilePath())) {
				filePathsToConsider.add(filePath);
			}
		}

		int numFilePaths = filePathsToConsider.size();
		System.out.println("Found " +numFilePaths+ " files to analyze");
		int index = 1;
		for (String filePath : filePathsToConsider) {
			printProgress(index, numFilePaths);
			try {
				runSingle(startEnv, filePath, false);
			} catch (Exception e) {
				log.error("Could run Shovel execution for Env {{}} with path {{}}. Skipping.", startEnv.getEnvName(), filePath, e);
			}

			index++;
		}

		printMiningEnd(startEnv);

		return yresult;
	}

	public static Yresult runSingle(StartEnvironment startEnv, String filePath, boolean accumulateResults) throws Exception {
		long now = new Date().getTime();
		Yresult yresult = new Yresult();
		printFileStart(filePath);
		String startFileContent = startEnv.getRepositoryService().findFileContent(startEnv.getStartCommit(), filePath);
		Yparser parser = ParserFactory.getParser(startEnv, filePath, startFileContent, startEnv.getStartCommit());
		for (Yfunction method : parser.getAllMethods()) {
			try {
				if (startEnv.getFunctionName() == null || startEnv.getFunctionName().equals(method.getName())) {
					if (startEnv.getFunctionStartLine() <= 0 || startEnv.getFunctionStartLine() == method.getNameLineNumber()) {
						if (accumulateResults) {
							yresult.putAll(runForMethod(startEnv, filePath, method));
						} else {
							runForMethod(startEnv, filePath, method);
						}
					}
				}
			} catch (Exception e) {
				log.error("SHOVEL_ERR: Error occurred running mining for method {} in file {}. Skipping.", method.getName(), filePath, e);
				e.printStackTrace();
			}

		}
		printFileEnd(filePath);
		duration += (new Date().getTime() - now);
		System.out.println("Total duration: " + duration);
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
		List<String> codeshovelHistory = new ArrayList<>();
		Map<String, Ychange> changeHistoryDetails = new LinkedHashMap<>();
		Map<String, String> changeHistoryShort = new LinkedHashMap<>();

		log.trace("Creating method history and writing git diffs for result history...");
		for (String commitName : yresult.keySet()) {
			codeshovelHistory.add(commitName);
			Ychange change = yresult.get(commitName);
			changeHistoryDetails.put(commitName, change);
			changeHistoryShort.put(commitName, change.getTypeAsString());
		}

		printAsJson(changeHistoryShort);

		JsonResult jsonResultCodeshovel = new JsonResult("codeshovel", task, codeshovelHistory, changeHistoryDetails, changeHistoryShort);
		jsonResultCodeshovel.setNumCommitsSeen(recursiveAnalysisTask.getNumAnalyzedCommits());
		jsonResultCodeshovel.setTimeTaken(recursiveAnalysisTask.getTimeTaken());

		if (!GlobalEnv.DISABLE_ALL_OUTPUTS) {
			if (StringUtils.isNotBlank(startEnv.getOutputFilePath())) {
				Utl.writeShovelResultFile(jsonResultCodeshovel, startEnv.getOutputFilePath());
			} else if (GlobalEnv.WRITE_RESULTS) {
				Utl.writeShovelResultFile(jsonResultCodeshovel);
			}

			if (GlobalEnv.WRITE_ORACLES) {
				Utl.writeJsonOracleToFile(jsonResultCodeshovel);
			}

		}

		if (!GlobalEnv.DISABLE_ALL_OUTPUTS && (GlobalEnv.WRITE_GITLOG || GlobalEnv.WRITE_SEMANTIC_DIFFS)) {
			GitRangeLogTask gitRangeLogTask = new GitRangeLogTask(task, startEnv);
			gitRangeLogTask.run();
			List<String> gitLogHistory = gitRangeLogTask.getResult();

			JsonResult jsonResultLogCommand = new JsonResult("logcommand", task, gitLogHistory, null, null);
			Utl.printMethodHistory(gitLogHistory);
			Utl.writeGitLogFile(jsonResultLogCommand);

			createAndWriteSemanticDiff("gitlog", jsonResultCodeshovel, codeshovelHistory, gitLogHistory);

			List<String> customBaselineHistory = startEnv.getBaseline();
			if (customBaselineHistory != null) {
				createAndWriteSemanticDiff("custom", jsonResultCodeshovel, codeshovelHistory, customBaselineHistory);
			}
		}

		printMethodEnd(method, startEnv.getOutputFilePath());

		return yresult;
	}

	private static void createAndWriteSemanticDiff(String baselineName, JsonResult result,
									   List<String> codeshovelHistory, List<String> baselineHistory) {
		List<String> onlyInCodeshovel = new ArrayList<>(codeshovelHistory);
		onlyInCodeshovel.removeAll(baselineHistory);

		List<String> onlyInBaseline = new ArrayList<>(baselineHistory);
		onlyInBaseline.removeAll(codeshovelHistory);

		log.trace("Found difference in change history. Writing files.");
		JsonChangeHistoryDiff diff = new JsonChangeHistoryDiff(codeshovelHistory, baselineHistory,
				onlyInCodeshovel, onlyInBaseline);
		Utl.writeSemanticDiff(baselineName, result, diff);
	}

	private static void printAsJson(Map<String, String> changeHistoryShort) {
		System.out.println("");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%% RESULT %%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		for (String commitName : changeHistoryShort.keySet()) {
			System.out.println("\""+ commitName +"\": " + "\""+ changeHistoryShort.get(commitName) +"\",");
		}
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

	private static void printMethodEnd(Yfunction method, String outputFilePath) {
		System.out.println("FINISHED ANALYSIS FOR METHOD " + method.getName());
		if (StringUtils.isNotBlank(outputFilePath)) {
			System.out.println("RESULT FILE WRITTEN TO " + outputFilePath);
		}
		System.out.println("-------------------------------------------------------------------------");
	}

	private static void printMiningEnd(StartEnvironment startEnv) {
		System.out.println("FINISHED MINING ANALYSIS");
		System.out.println(new Date());
		System.out.println("#########################################################################");
	}

	private static void printMiningStart(StartEnvironment startEnv) {
		System.out.println("#########################################################################");
		System.out.println("STARTING MINING ANALYSIS FOR REPO AND PATH: " + startEnv.getRepositoryName() + " - " + startEnv.getFilePath());
		System.out.println(new Date());
	}

	private static void printProgress(int index, int numFilePaths) {
		System.out.println("");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%% FILE " + index + " / " + numFilePaths + " %%%%%%%%%%%%%%%%%%%%%");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%% " + new Date() + " %%%%%%%%%%");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("");
	}

}
