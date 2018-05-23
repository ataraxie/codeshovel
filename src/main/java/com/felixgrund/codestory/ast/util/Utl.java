package com.felixgrund.codestory.ast.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.shaded.org.objenesis.strategy.StdInstantiatorStrategy;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.entities.Yhistory;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.tasks.AnalysisTask;
import com.felixgrund.codestory.ast.json.JsonChangeHistoryDiff;
import com.felixgrund.codestory.ast.json.JsonResult;
import com.felixgrund.codestory.ast.json.JsonSimilarity;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Utl {

	public static String getFileContentByObjectId(Repository repository, ObjectId objectId) throws IOException {
		ObjectLoader loader = repository.open(objectId);
		OutputStream output = new OutputStream()
		{
			private StringBuilder string = new StringBuilder();
			@Override
			public void write(int b) throws IOException {
				this.string.append((char) b);
			}
			public String toString(){
				return this.string.toString();
			}
		};
		loader.copyTo(output);
		return output.toString();
	}

	public static RevCommit findCommitByName(Repository repository, String commitName) throws IOException {
		ObjectId objectId = ObjectId.fromString(commitName);
		RevWalk revWalk = new RevWalk(repository);
		RevCommit revCommit = revWalk.parseCommit(revWalk.lookupCommit(objectId));
		return revCommit;
	}

	public static RevCommit findHeadCommit(Repository repository, String branchName) throws IOException {
		Ref masterRef = repository.findRef(branchName);
		ObjectId masterId = masterRef.getObjectId();
		RevWalk walk = new RevWalk(repository);
		return walk.parseCommit(masterId);
	}

	public static List<RevCommit> findCommitsForBranch(Repository repository, String branchName) throws IOException {
		Ref masterRef = repository.findRef(branchName);
		ObjectId masterId = masterRef.getObjectId();

		RevWalk walk = new RevWalk(repository);
		RevCommit headCommit = walk.parseCommit(masterId);

		walk.markStart(headCommit);
		Iterator<RevCommit> iterator = walk.iterator();
		List<RevCommit> commits = Lists.newArrayList(iterator);
		return commits;
	}

	public static List<String> findFilesByExtension(Repository repository, RevCommit commit, String fileExtension) throws Exception {
		List<String> ret = new ArrayList<>();
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			String pathString = treeWalk.getPathString();
			if (treeWalk.getPathString().endsWith(fileExtension)) {
				ret.add(pathString);
			}
		}
		return ret;
	}

	public static String findFileContent(Repository repository, RevCommit commit, String filePath) throws IOException {
		String ret = null;
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.create(filePath));
		if (treeWalk.next()) {
			ObjectId objectId = treeWalk.getObjectId(0);
			ret = Utl.getFileContentByObjectId(repository, objectId);
		}
		return ret;
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
		return projectDir() + "/cache/" + hash + ".codestory";
	}

	public static Yhistory loadFromCache(String hash) {
		Yhistory ret = null;
		Kryo kryo = new Kryo();
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		Input input = null;
		try {
			input = new Input(new FileInputStream(cacheFilePath(hash)));
			ret = kryo.readObject(input, Yhistory.class);
			input.close();
		} catch (FileNotFoundException e) {
			// return null
		}
		return ret;
	}

	public static void saveToCache(String hash, Yhistory collection) throws FileNotFoundException {
		Kryo kryo = new Kryo();
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		Output output = new Output(new FileOutputStream(cacheFilePath(hash)));
		kryo.writeObject(output, collection);
		output.close();
	}

	public static boolean isFunctionBodySimilar(Yfunction aFunction, Yfunction bFunction) {
		double similarity = getBodySimilarity(aFunction, bFunction);
		return similarity > 0.7;
	}

	public static double getBodySimilarity(Yfunction aFunction, Yfunction bFunction) {
		return new JaroWinklerDistance().apply(aFunction.getBody(), bFunction.getBody());
	}

	public static void printMethodHistory(AnalysisTask task) {
		Yresult yresult = task.getYresult();
		System.out.println("\nCodeStory Change History:");
		for (Ycommit ycommit : yresult.keySet()) {
			System.out.println(ycommit.getCommit().getName() + ": " + yresult.get(ycommit));
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
									   String functionName, String repoName, String content, String fileExtension) {
		String baseDir = System.getProperty("user.dir") + "/output/" + subdir;
		String commitNameShort = commitName.substring(0, 5);
		String targetDirPath = baseDir + "/" + repoName + "/" + commitNameShort + "/" + filePath;
		File targetDir = new File(targetDirPath);
		targetDir.mkdirs();
		String sanitizedFunctionName = functionName.replaceAll(":", "_CLN_").replaceAll("#", "_HSH_");
		File file = new File(targetDirPath + "/" + sanitizedFunctionName + fileExtension);
		try {
			FileUtils.writeStringToFile(file, content, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeJsonResultToFile(JsonResult jsonResult) {
		writeOutputFile(
				jsonResult.getOrigin(), jsonResult.getStartCommitName(), jsonResult.getSourceFilePath(),
				jsonResult.getFunctionName(), jsonResult.getRepositoryName(), jsonResult.toJson(), ".json"
		);
	}

	public static void writeSimilarityToFile(
			JsonSimilarity jsonSimilarity,
			String functionPath,
			String repoName,
			String filePath) {

		writeOutputFile(
				"similarity", jsonSimilarity.getCommitName(), filePath, functionPath,
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
				"similarity_plain", jsonSimilarity.getCommitName(), filePath, functionPath,
				repoName, builder.toString(), ".out"
		);
	}

	public static void writeChangeHistoryDiff(JsonResult originalJsonResult, JsonChangeHistoryDiff diff) {
		writeOutputFile(
				"history_diff", originalJsonResult.getStartCommitName(), originalJsonResult.getSourceFilePath(),
				originalJsonResult.getFunctionName(), originalJsonResult.getRepositoryName(), diff.toJson(), ".json"
		);
	}

	public static int countLineNumbers(String string) {
		return string.split("\r\n|\r|\n").length;
	}

}
