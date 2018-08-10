package com.felixgrund.codeshovel.util;

import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.entities.Yresult;
import com.felixgrund.codeshovel.json.JsonSimilarity;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.FunctionSimilarity;
import com.felixgrund.codeshovel.json.JsonChangeHistoryDiff;
import com.felixgrund.codeshovel.json.JsonResult;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.tasks.AnalysisTask;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Utl {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
	private static final String OUTPUT_BASE_DIR = Optional.ofNullable(
			System.getenv("OUTPUT_DIR")).orElse(System.getProperty("user.dir") + "/output");

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

	private static String cacheFilePath(String hash) {
		return projectDir() + "/cache/" + hash + ".codeshovel";
	}

//	public static Yhistory loadFromCache(String hash) {
//		Yhistory ret = null;
//		Kryo kryo = new Kryo();
//		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
//		Input input = null;
//		try {
//			input = new Input(new FileInputStream(cacheFilePath(hash)));
//			ret = kryo.readObject(input, Yhistory.class);
//			input.close();
//		} catch (FileNotFoundException e) {
//			// return null
//		}
//		return ret;
//	}

//	public static void saveToCache(String hash, Yhistory collection) throws FileNotFoundException {
//		Kryo kryo = new Kryo();
//		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
//		Output output = new Output(new FileOutputStream(cacheFilePath(hash)));
//		kryo.writeObject(output, collection);
//		output.close();
//	}

	public static double getBodySimilarity(Yfunction aFunction, Yfunction bFunction) {
		return new JaroWinklerDistance().apply(aFunction.getBody(), bFunction.getBody());
	}

	public static double getNameSimilarity(Yfunction aFunction, Yfunction bFunction) {
		return new JaroWinklerDistance().apply(aFunction.getName(), bFunction.getName());
	}

	public static void printMethodHistory(AnalysisTask task) {
		Yresult yresult = task.getYresult();
		System.out.println("\nCodeShovel Change History:");
		for (Ycommit ycommit : yresult.keySet()) {
			System.out.println(ycommit.getShortName() + ": " + yresult.get(ycommit));
		}
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

		String baseDir = OUTPUT_BASE_DIR + "/" + subdir;
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
		if (functionId != null) {
			file = new File(targetDirPath + "/" + functionId + fileExtension);
		} else {
			file = new File(targetDirPath + "/" + commitName + fileExtension);
		}

		try {
			FileUtils.writeStringToFile(file, content, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeJsonResultToFile(JsonResult jsonResult) {
		writeOutputFile(
				jsonResult.getOrigin(), jsonResult.getStartCommitName(), jsonResult.getSourceFilePath(),
				jsonResult.getFunctionId(), jsonResult.getRepositoryName(), jsonResult.toJson(), ".json"
		);
	}

	public static void writeSimilarityToFile(
			JsonSimilarity jsonSimilarity,
			String functionId,
			String repoName,
			String filePath) {

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
		writeOutputFile(
				"diff_semantic_" + baselineName, originalJsonResult.getStartCommitName(), originalJsonResult.getSourceFilePath(),
				originalJsonResult.getFunctionId(), originalJsonResult.getRepositoryName(), diff.toJson(), ".json"
		);
	}

	public static void writeGitDiff(String commitName, String filePath, Repository repository,
									String repositoryName) throws Exception {

		String diff = CmdUtil.gitDiffParent(commitName, filePath, repository.getDirectory().getParentFile());
		writeOutputFile("diff_git", commitName, filePath, null, repositoryName , diff, ".diff");
	}

	public static void writeJsonSimilarity(String repoName, String filePath, Yfunction compareFunction,
										   Yfunction mostSimilarFunction, FunctionSimilarity similarity) {

		JsonSimilarity.FunctionEntry compareEntry = new JsonSimilarity.FunctionEntry(
				compareFunction.getCommitName(),
				compareFunction.getName(),
				compareFunction.getId(),
				compareFunction.getBody());
		JsonSimilarity.FunctionEntry mostSimilarEntry = new JsonSimilarity.FunctionEntry(
				mostSimilarFunction.getCommitName(),
				mostSimilarFunction.getName(),
				mostSimilarFunction.getId(),
				mostSimilarFunction.getBody());


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
}
