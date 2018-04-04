package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.entities.YCommit;
import com.felixgrund.codestory.ast.entities.YCollection;
import com.felixgrund.codestory.ast.entities.YDiff;
import com.felixgrund.codestory.ast.entities.YFunction;
import com.felixgrund.codestory.ast.parser.JsParser;
import com.felixgrund.codestory.ast.util.Utl;
import jdk.nashorn.internal.ir.FunctionNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class YAnalysisTask {

	private static boolean CACHE_ENABLED = false;

	private Git git;
	private Repository repository;
	private String filePath;
	private String fileName;
	private String branchName;
	private String startCommitName;
	private String functionName;
	private int functionStartLine;

	private String functionPath;

	private String startFileContent;
	private FunctionNode startFunctionNode;
	private RevCommit startCommit;

	private YCommit startCommitInfo;
	private YCollection result;

	public void run() throws Exception {
		long start = new Date().getTime();
		this.buildAndValidate();
		String hash = this.createUuidHash();
		if (CACHE_ENABLED) {
			this.result = Utl.loadFromCache(hash);
		}
		if (this.result == null) {
			this.createCommitCollection();
			if (CACHE_ENABLED) {
				System.out.println("NOT FOUND IN CACHE");
				Utl.saveToCache(hash, this.result);
			}
		} else if (CACHE_ENABLED) {
			System.out.println("FOUND IN CACHE");
		}

		long timeTakenSeconds = (new Date().getTime() - start) / 1000;
		System.out.println("MEASURE YAnalysisTask in seconds: " + timeTakenSeconds);
	}

	private void buildAndValidate() throws Exception {
		Utl.checkNotNull("repository", this.repository);
		Utl.checkNotNull("startCommitName", this.startCommitName);
		Utl.checkNotNull("filePath", this.filePath);
		Utl.checkNotNull("fileName", this.fileName);
		Utl.checkNotNull("functionName", this.functionName);
		Utl.checkNotNull("functionStartLine", this.functionStartLine);

		this.startCommit = Utl.findCommitByName(this.repository, this.startCommitName);
		Utl.checkNotNull("startCommit", this.startCommit);

		this.startFileContent = Utl.findFileContent(this.repository, this.startCommit, this.filePath);
		Utl.checkNotNull("startFileContent", this.startFileContent);

		JsParser startParser = new JsParser(this.fileName, this.startFileContent);
		this.startFunctionNode = startParser.findFunctionByNameAndLine(this.functionName, this.functionStartLine);
		Utl.checkNotNull("startFunctionNode", this.startFunctionNode);

		this.functionPath = this.startFunctionNode.getName();
		Utl.checkNotNull("functionPath", this.functionPath);

		this.startCommitInfo = createCommitInfo(this.startCommit);
		Utl.checkNotNull("startCommitInfo", this.startCommitInfo);
	}

	private void createCommitCollection() throws IOException, GitAPIException {
		this.result = new YCollection();
		this.result.add(this.startCommitInfo);

		LogCommand logCommand = git.log().addPath(filePath);

		YCommit yCommitAfter = this.startCommitInfo;
		Iterable<RevCommit> commits = logCommand.call();

		for (RevCommit commit : commits) {
			if (commit.getCommitTime() < startCommit.getCommitTime()) { // start commit for log command doesn't work
				System.out.println(commit.getName());
				YCommit currentCommitInfo = createCommitInfo(commit);
				currentCommitInfo.setNext(yCommitAfter);
				yCommitAfter.setPrev(currentCommitInfo);
				yCommitAfter.setYDiff(createDiffInfo(yCommitAfter.getCommit(), commit));
				this.result.add(currentCommitInfo);
				yCommitAfter = currentCommitInfo;
			}
		}
	}

	private YCommit createCommitInfo(RevCommit commit) throws IOException {
		YCommit yCommit = createBaseCommitInfo(commit);
		if (yCommit.isFileFound()) {
			JsParser parser = new JsParser(yCommit.getFileName(), yCommit.getFileContent());
			yCommit.setParser(parser);
			List<FunctionNode> matchedNodes = parser.findFunctionByNode(this.startFunctionNode);
			int numMatchedNodes = matchedNodes.size();
			if (numMatchedNodes >= 1) {
				yCommit.setMatchedFunctionInfo(new YFunction(matchedNodes.get(0)));
				if (numMatchedNodes > 1) {
					System.err.println("More than one matching function found. Taking first.");
				}
			}
		}
		return yCommit;
	}

	private YCommit createBaseCommitInfo(RevCommit commit) throws IOException {
		YCommit ret = new YCommit(commit);
		ret.setFileName(this.fileName);
		ret.setFilePath(this.filePath);

		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(this.repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.create(this.filePath));

		if (treeWalk.next()) {
			ObjectId objectId = treeWalk.getObjectId(0);
			String fileContent = Utl.getFileContentByObjectId(this.repository, objectId);
			ret.setFileContent(fileContent);
		}

		return ret;

	}

	private YDiff createDiffInfo(RevCommit commit, RevCommit prevCommit) throws IOException, GitAPIException {
		YDiff ret = null;
		ObjectReader objectReader = this.repository.newObjectReader();
		CanonicalTreeParser treeParserNew = new CanonicalTreeParser();
		OutputStream outputStream = System.out;
		DiffFormatter formatter = new DiffFormatter(outputStream);
		formatter.setRepository(this.repository);
		formatter.setDiffComparator(RawTextComparator.DEFAULT);
		treeParserNew.reset(objectReader, commit.getTree());
		CanonicalTreeParser treeParserOld = new CanonicalTreeParser();
		treeParserOld.reset(objectReader, prevCommit.getTree());
		List<DiffEntry> diff = formatter.scan(treeParserOld, treeParserNew);
		for (DiffEntry entry : diff) {
			if (entry.getOldPath().equals(this.filePath)) {
				FileHeader fileHeader = formatter.toFileHeader(entry);
				ret = new YDiff(entry, fileHeader.toEditList(), formatter);
				break;
			}
		}
		return ret;
	}

	private String createUuidHash() {
		StringBuilder builder = new StringBuilder();
		builder.append(branchName)
				.append(filePath)
				.append(fileName)
				.append(functionName)
				.append(functionStartLine)
				.append(startCommitName);
		return DigestUtils.md5Hex(builder.toString());
	}

	public YCollection getResult() {
		return result;
	}

	public void setRepository(String repositoryPath) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		this.repository = builder.setGitDir(new File(repositoryPath))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		this.git = new Git(repository);
	}

	public void setStartCommitName(String startCommitName) {
		this.startCommitName = startCommitName;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public void setFunctionStartLine(int functionStartLine) {
		this.functionStartLine = functionStartLine;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



}
