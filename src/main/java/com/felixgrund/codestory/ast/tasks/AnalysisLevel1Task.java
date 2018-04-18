package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.changes.Ynochange;
import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.exceptions.NoParserFoundException;
import com.felixgrund.codestory.ast.exceptions.ParseException;
import com.felixgrund.codestory.ast.interpreters.InterpreterLevel1;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import com.google.common.collect.Lists;
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
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AnalysisLevel1Task {

	private static boolean CACHE_ENABLED = false;

	private Git git;
	private Repository repository;
	private String filePath;
	private String fileName;
	private String branchName;
	private String startCommitName;
	private String functionName;
	private int functionStartLine;

	private List<RevCommit> history;

	private String functionPath;

	private String startFileContent;
	private Yfunction startFunction;
	private RevCommit startCommit;

	private Ycommit startCommitInfo;
	private Yhistory yhistory;
	private Yresult yresult;

	private HashMap<String, Ycommit> yCommitCache;

	public void run() throws Exception {
		this.yCommitCache = new HashMap<>();
		long start = new Date().getTime();
		this.buildAndValidate();
		String hash = this.createUuidHash();
		if (CACHE_ENABLED) {
			this.yhistory = Utl.loadFromCache(hash);
		}
		if (this.yhistory == null) {
			this.createCommitCollection();
			if (CACHE_ENABLED) {
				System.out.println("NOT FOUND IN CACHE");
				Utl.saveToCache(hash, this.yhistory);
			}
		} else if (CACHE_ENABLED) {
			System.out.println("FOUND IN CACHE");
		}

		this.createResult();

		long timeTakenSeconds = (new Date().getTime() - start) / 1000;
		System.out.println("MEASURE AnalysisLevel1Task in seconds: " + timeTakenSeconds);
	}

	private void createResult() throws IOException {
		this.yresult = new Yresult();
		for (Ycommit ycommit : this.getYhistory()) {
			InterpreterLevel1 interpreter = new InterpreterLevel1(ycommit);
			interpreter.interpret();
			Ychange ychange = interpreter.getInterpretation();
			if (!(ychange instanceof Ynochange)) {
				this.yresult.put(ycommit, interpreter.getInterpretation());
			}
		}
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

		Yparser startParser = ParserFactory.getParser(this.fileName, this.startFileContent);
		startParser.parse();
		this.startFunction = startParser.findFunctionByNameAndLine(this.functionName, this.functionStartLine);
		Utl.checkNotNull("startFunctionNode", this.startFunction);

		this.functionPath = this.startFunction.getName();
		Utl.checkNotNull("functionPath", this.functionPath);

		this.startCommitInfo = getOrCreateYcommit(this.startCommit);
		Utl.checkNotNull("startCommitInfo", this.startCommitInfo);
	}

	private void createCommitCollection() throws IOException, GitAPIException, NoParserFoundException {

		this.yhistory = new Yhistory();

		LogCommand logCommand = this.git.log().addPath(this.filePath).setRevFilter(RevFilter.NO_MERGES);

		Iterable<RevCommit> revisions = logCommand.call();
		this.history = Lists.newArrayList(revisions);

		for (RevCommit commit : this.history) {
			try {
				Ycommit ycommit = getOrCreateYcommit(commit);
				if (commit.getParentCount() > 0) {
					RevCommit parentCommit = commit.getParent(0);
					Ycommit parentYcommit = getOrCreateYcommit(parentCommit);
					ycommit.setParent(parentYcommit);
					ycommit.setYdiff(createDiffInfo(commit, parentCommit));
				}
				this.yhistory.add(ycommit);
			} catch (ParseException e) {
				System.err.println("ParseException occurred for commit or its parent. Skipping. Commit: " + commit.getName());
			}
		}
	}

	private Ycommit getOrCreateYcommit(RevCommit commit) throws ParseException, IOException, NoParserFoundException {
		String commitName = commit.getName();
		Ycommit ycommit = yCommitCache.get(commitName);
		if (ycommit != null) {
			return ycommit;
		}

		ycommit = createBaseYcommit(commit);
		if (ycommit.isFileFound()) {
			Yparser parser = ParserFactory.getParser(ycommit.getFileName(), ycommit.getFileContent());
			parser.parse();
			ycommit.setParser(parser);
			List<Yfunction> matchedFunctions = parser.findFunctionsByOtherFunction(this.startFunction);
			int numMatchedNodes = matchedFunctions.size();
			if (numMatchedNodes >= 1) {
				ycommit.setMatchedFunction(matchedFunctions.get(0));
				if (numMatchedNodes > 1) {
					System.err.println("More than one matching function found. Taking first.");
				}
			}
		}
		yCommitCache.put(commitName, ycommit);
		return ycommit;
	}

	private Ycommit createBaseYcommit(RevCommit commit) throws IOException {
		Ycommit ret = new Ycommit(commit);
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

	private Ydiff createDiffInfo(RevCommit commit, RevCommit prevCommit) throws IOException, GitAPIException {
		Ydiff ret = null;
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
				ret = new Ydiff(entry, fileHeader.toEditList(), formatter);
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

	public Yhistory getYhistory() {
		return yhistory;
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

	public List<RevCommit> getHistory() {
		return history;
	}

	public Yresult getYresult() {
		return yresult;
	}
}
