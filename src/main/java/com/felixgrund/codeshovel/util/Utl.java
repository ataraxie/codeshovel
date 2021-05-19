package com.felixgrund.codeshovel.util;

import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.json.JsonSimilarity;
import com.felixgrund.codeshovel.json.JsonOracle;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.FunctionSimilarity;
import com.felixgrund.codeshovel.json.JsonChangeHistoryDiff;
import com.felixgrund.codeshovel.json.JsonResult;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.tasks.AnalysisTask;
import com.felixgrund.codeshovel.wrappers.GlobalEnv;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utl {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy, HH:mm");

	private static final Logger log = LoggerFactory.getLogger(Utl.class);

	public static Repository createRepository(String repositoryPath) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.setGitDir(new File(repositoryPath))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
	}

	public static void checkNotNull(String field, Object object) throws Exception {
		if (object == null) {
			String msg = String.format("Field '%s' was not set", field);
			throw new Exception(msg);
		}
	}

	public static void checkPositiveInt(String field, Object object) throws Exception {
		checkNotNull(field, object);
		try {
			int number = Integer.parseInt(Objects.toString(object));
			if (number <= 0) {
				throw new Exception("Value was an integer but was not positive");
			}
		} catch (NumberFormatException e) {
			throw new Exception("Value was not an integer");
		}

	}

	public static String projectDir() {
		return System.getProperty("user.dir");
	}

	public static double getBodySimilarity(Yfunction aFunction, Yfunction bFunction) {
		return new JaroWinklerDistance().apply(aFunction.getBody(), bFunction.getBody());
	}

	public static double getNameSimilarity(Yfunction aFunction, Yfunction bFunction) {
		return new JaroWinklerDistance().apply(aFunction.getName(), bFunction.getName());
	}

	public static void printMethodHistory(AnalysisTask task) {
		System.out.println("\nCodeShovel Change History: " + task.getYresult().toString());
	}

	public static void printMethodHistory(List<String> commitNames) {
		System.out.println("\nGit Log Change History:");
		for (String commitName : commitNames) {
			System.out.println(commitName);
		}
	}

	public static void printAnalysisRun(AnalysisTask task) {
		System.out.println("====================================================");
		System.out.println(String.format("Running Analysis\nCommit: %s\nMethod: %s\nLines: %s-%s",
				task.getStartCommitName(), task.getFunctionName(), task.getFunctionStartLine(), task.getFunctionEndLine()));
		System.out.println("====================================================");
	}

	public static void writeOutputFile(String subdir, String commitName, String filePath,
				String functionId, String repoName, String content, String fileExtension) {

		if (GlobalEnv.DISABLE_ALL_OUTPUTS) {
			return;
		}

		try {
			String baseDir = GlobalEnv.OUTPUT_DIR + "/" + subdir;
			String commitNameShort = commitName.substring(0, 5);
			String targetDirPath = baseDir + "/" + repoName;
			if (functionId != null) {
				targetDirPath +=  "/" + commitNameShort + "/" + filePath;
			} else {
				targetDirPath +=  "/" + filePath;
			}

			File targetDir = new File(targetDirPath);
			targetDir.mkdirs();

			File file;
			String filename;
			if (functionId != null) {
				filename = functionId;
			} else {
				filename = commitName;

			}
			if (filename.length() > 150) {
				filename = filename.substring(0, 149);
			}
			filename += fileExtension;
			file = new File(targetDirPath + "/" + filename);

			FileUtils.writeStringToFile(file, content, "utf-8");
		} catch (Exception e) {
			log.error("Could not write file for function {{}}. Skipping.", functionId, e);
		}
	}

	private static final void writeOutputFileWithPath(String filePath, String content) throws IOException {
		File file = new File(filePath);
		FileUtils.writeStringToFile(file, content, "utf-8");
	}

	public static void writeShovelResultFile(JsonResult jsonResult) {
		if (GlobalEnv.DISABLE_ALL_OUTPUTS || !GlobalEnv.WRITE_RESULTS) {
			return;
		}
		writeOutputFile(
				"codeshovel", jsonResult.getStartCommitName(), jsonResult.getSourceFilePath(),
				jsonResult.getFunctionId(), jsonResult.getRepositoryName(), jsonResult.toJson(), ".json"
		);
	}

	public static void writeShovelResultFile(JsonResult jsonResult, String outFilePath) throws IOException {
		File file = new File(outFilePath);
		FileUtils.writeStringToFile(file, jsonResult.toJson(), "utf-8");
	}

	public static void writeGitLogFile(JsonResult jsonResult) {
		if (GlobalEnv.DISABLE_ALL_OUTPUTS || !GlobalEnv.WRITE_GITLOG) {
			return;
		}
		writeOutputFile(
				"logcommand", jsonResult.getStartCommitName(), jsonResult.getSourceFilePath(),
				jsonResult.getFunctionId(), jsonResult.getRepositoryName(), jsonResult.toJson(), ".json"
		);
	}

	public static void writeJsonOracleToFile(JsonResult jsonResult) {
		if (GlobalEnv.DISABLE_ALL_OUTPUTS || !GlobalEnv.WRITE_ORACLES) {
			return;
		}

		try {
			String filePath = jsonResult.getSourceFilePath();
			String[] filePathSplit = filePath.split("/");
			String className = filePathSplit[filePathSplit.length-1].replace(".java", "");
			String baseDir = GlobalEnv.OUTPUT_DIR + "/oracles";
			File file = new File(baseDir + "/" + jsonResult.getRepositoryName() + "-" + className + "-" + jsonResult.getFunctionName() + ".json");
			JsonOracle oracle = new JsonOracle(jsonResult);
			FileUtils.writeStringToFile(file, oracle.toJson(), "utf-8");
		} catch (Exception e) {
			log.error("Could not write oracle file for function {{}}. Skipping.", jsonResult.getFunctionId(), e);
		}
	}

	public static void writeSimilarityToFile(
			JsonSimilarity jsonSimilarity,
			String functionId,
			String repoName,
			String filePath) {

		if (GlobalEnv.DISABLE_ALL_OUTPUTS || !GlobalEnv.WRITE_SIMILARITIES) {
			return;
		}

		writeOutputFile(
				"similarity", jsonSimilarity.getCommitName(), filePath, functionId,
				repoName, jsonSimilarity.toJson(), ".json"
		);

		StringBuilder builder = new StringBuilder();
		builder.append("=== COMPARE FUNCTION ===\n\n");
		builder.append(jsonSimilarity.getFunction());
		builder.append("\n\n=== MOST SIMILAR FUNCTION ===\n\n");
		builder.append(jsonSimilarity.getMostSimilarFunction());
		builder.append("\n\n=== SIMILARITY ===\n");
		builder.append(jsonSimilarity.getSimilarity());

		writeOutputFile(
				"similarity_plain", jsonSimilarity.getCommitName(), filePath, functionId,
				repoName, builder.toString(), ".out"
		);
	}

	public static void writeSemanticDiff(String baselineName, JsonResult originalJsonResult, JsonChangeHistoryDiff diff) {
		if (GlobalEnv.DISABLE_ALL_OUTPUTS || !GlobalEnv.WRITE_SEMANTIC_DIFFS) {
			return;
		}
		writeOutputFile(
				"diff_semantic_" + baselineName, originalJsonResult.getStartCommitName(), originalJsonResult.getSourceFilePath(),
				originalJsonResult.getFunctionId(), originalJsonResult.getRepositoryName(), diff.toJson(), ".json"
		);
	}

	public static void writeGitDiff(String commitName, String filePath, Repository repository,
									String repositoryName) throws Exception {
		if (GlobalEnv.DISABLE_ALL_OUTPUTS || !GlobalEnv.WRITE_GIT_DIFFS) {
			return;
		}
		String diff = CmdUtil.gitDiffParent(commitName, filePath, repository.getDirectory().getParentFile());
		writeOutputFile("diff_git", commitName, filePath, null, repositoryName , diff, ".diff");
	}

	public static void writeJsonSimilarity(String repoName, String filePath, Yfunction compareFunction,
										   Yfunction mostSimilarFunction, FunctionSimilarity similarity) {
		if (GlobalEnv.DISABLE_ALL_OUTPUTS || !GlobalEnv.WRITE_SIMILARITIES) {
			return;
		}
		JsonSimilarity.FunctionEntry compareEntry = new JsonSimilarity.FunctionEntry(
				compareFunction.getCommitName(),
				compareFunction.getName(),
				compareFunction.getId(),
				compareFunction.getSourceFragment());
		JsonSimilarity.FunctionEntry mostSimilarEntry = new JsonSimilarity.FunctionEntry(
				mostSimilarFunction.getCommitName(),
				mostSimilarFunction.getName(),
				mostSimilarFunction.getId(),
				mostSimilarFunction.getSourceFragment());


		JsonSimilarity jsonSimilarity = new JsonSimilarity(compareEntry, mostSimilarEntry, similarity);
		Utl.writeSimilarityToFile(jsonSimilarity, compareFunction.getId(), repoName, filePath);
	}

	public static String getTextFragment(String text, int beginLine, int endLine) {
		List<String> lines = getLines(text);
		List<String> fragmentLines = lines.subList(beginLine - 1, endLine);
		return StringUtils.join(fragmentLines, "\n");
	}

	public static List<String> getLines(String string) {
		return Arrays.asList(string.split("\r\n|\r|\n"));
	}

	public static int countLineNumbers(String string) {
		return getLines(string).size();
	}

	public static long getMsBetweenCommits(Commit oldCommit, Commit newCommit) {
		long newCommitTime = newCommit.getCommitDate().getTime();
		long oldCommitTime = oldCommit.getCommitDate().getTime();
		return newCommitTime - oldCommitTime;
	}

	public static double getDaysBetweenCommits(Commit oldCommit, Commit newCommit) {
		long msBetweenCommits = getMsBetweenCommits(oldCommit, newCommit);
		double daysBetweenNotRounded = (double) msBetweenCommits / (1000*60*60*24);
		BigDecimal decimal = new BigDecimal(daysBetweenNotRounded);
		decimal = decimal.setScale(2, RoundingMode.HALF_UP);
		return decimal.doubleValue();
	}

	public static int getLineNumberDistance(Yfunction aFunction, Yfunction bFunction) {
		return Math.abs(aFunction.getNameLineNumber() - bFunction.getNameLineNumber());
	}

	public static double getLineNumberSimilarity(Yfunction aFunction, Yfunction bFunction) {
		String aFileSource = aFunction.getSourceFileContent();
		int aNumLines = Utl.countLineNumbers(aFileSource);
		String bFileSource = bFunction.getSourceFileContent();
		int bNumLines = Utl.countLineNumbers(bFileSource);
		double maxLines = Math.max(aNumLines, bNumLines);
		double lineNumberDistance = getLineNumberDistance(aFunction, bFunction);
		double similarity = (maxLines - lineNumberDistance) / maxLines;
		return similarity;
	}

	public static String getFileExtensionWithoutDot(String filePath) {
		String[] dotSplit = filePath.split("\\.");
		return dotSplit[dotSplit.length - 1];
	}

	public static String getFileName(String filePath) {
		String[] pathSplit = filePath.split("/");
		return pathSplit[pathSplit.length-1];
	}

	public static String sanitizeFunctionId(String ident) {
		return ident.replaceAll(":", "__").replaceAll("#", "__").replaceAll("<", "__").replaceAll(">", "__");
	}

	public static String resultMapToString(Map<String, String> result) {
		StringBuilder builder = new StringBuilder();
		for (String commitName : result.keySet()) {
			builder.append("\n").append(commitName).append(":").append(result.get(commitName));
		}

		return builder.toString();
	}

	public static boolean parametersMetadataEqual(List<Yparameter> parametersA, List<Yparameter> parametersB) {
		Map<String, Yparameter> parameterMapA = new HashMap<>();
		for (Yparameter parameter : parametersA) {
			parameterMapA.put(parameter.getNameTypeString(), parameter);
		}

		for (Yparameter paramB : parametersB) {
			String paramString = paramB.getNameTypeString();
			Yparameter paramA = parameterMapA.get(paramString);
			if (paramA != null && !paramA.getMetadataString().equals(paramB.getMetadataString())) {
				return false;
			}
		}

		return true;
	}

	public static Map<String, Yfunction> functionsToIdMap(List<Yfunction> functions) {
		Map<String, Yfunction> ret = new HashMap<>();
		for (Yfunction function : functions) {
			ret.put(function.getId(), function);
		}
		return ret;
	}

	public static boolean parentNamesMatch(Yfunction function, Yfunction compareFunction) {
		String aParentName = function.getParentName();
		String bParentName = compareFunction.getParentName();
		if (aParentName != null && bParentName != null) {
			if (aParentName.equals(bParentName)) {
				return true;
			}
		}

		return false;
	}

	/***
	 * Check for formatting changes
	 * <p>
	 *     If indentation, whitespace or any sort of formatting
	 *     was performed on a method the function will return true.
	 *     Otherwise it will return false.
	 * </p>
	 * @param function current matched function
	 * @param compareFunction previous function to compare with
	 * @return boolean True/False
	 */
	public static boolean isFormatChange(Yfunction function, Yfunction compareFunction) {
		// if lexically preserved method's body (unformatted body) are not equal
		// but pretty printed method's body (ignoring whitespace, indentation etc.,) are equal
		// then the change was of Yformatchange type
		if (function != null && compareFunction != null &&
				function.getBody().equals(compareFunction.getBody()) &&
				!function.getUnformattedBody().equals(compareFunction.getUnformattedBody())) {
			return true;
		}
		return false;
	}
}
