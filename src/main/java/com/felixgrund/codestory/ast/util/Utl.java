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
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.*;
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
		String aBody = aFunction.getBody();
		String bBody = bFunction.getBody();
		double similarity = new JaroWinklerDistance().apply(aBody, bBody);
		return similarity > 0.7;
	}

	public static void printMethodHistory(AnalysisTask task) {
		Yresult yresult = task.getYresult();
		System.out.println("\nMethod history...");
		for (Ycommit ycommit : yresult.keySet()) {
			System.out.println(ycommit.getCommit().getName() + ": " + yresult.get(ycommit));
		}
	}

	public static void printAnalysisRun(AnalysisTask task) {
		System.out.println("\n====================================================");
		System.out.println(String.format("Running Level 1 Analysis\nCommit: %s\nMethod: %s\nLines: %s-%s",
				task.getStartCommitName(), task.getFunctionName(), task.getFunctionStartLine(), task.getFunctionEndLine()));
		System.out.println("====================================================");
	}

	public static void writeJsonResultToFile(JsonResult jsonResult) throws IOException {
		String dir = System.getProperty("user.dir") + "/output/" + jsonResult.getOrigin();
		String commitNameShort = jsonResult.getStartCommitName().substring(0, 5);
		String sourceFileName = jsonResult.getSourceFileName();
		String functionName = jsonResult.getFunctionName();
		String repoName = jsonResult.getRepositoryName();
		File file = new File(dir + "/" + repoName + "-" + commitNameShort + "-" + sourceFileName + "-" + functionName);
		FileUtils.writeStringToFile(file, jsonResult.toJsonString(), "utf-8");
	}

}
