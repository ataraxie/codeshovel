package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.changes.Ymetachange;
import com.felixgrund.codestory.ast.changes.Ymultichange;
import com.felixgrund.codestory.ast.changes.Ynochange;
import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.exceptions.NoParserFoundException;
import com.felixgrund.codestory.ast.exceptions.ParseException;
import com.felixgrund.codestory.ast.interpreters.Interpreter;
import com.felixgrund.codestory.ast.parser.Yfunction;
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
import java.util.*;

public class AnalysisTask {

	private static boolean CACHE_ENABLED = false;

	private Git git;
	private String repositoryName;
	private Repository repository;
	private String filePath;
	private String fileName;
	private String startCommitName;
	private String functionName;
	private int functionStartLine;
	private int functionEndLine;

	private boolean wasBuilt;

	private Map<String, RevCommit> fileHistory;

	private Yfunction startFunction;
	private Ycommit startCommit;

	private Yhistory yhistory;
	private Yresult yresult;

	private Ychange lastMajorChange;

	private HashMap<String, Ycommit> currentCommitCache;

	public AnalysisTask() {
		this.currentCommitCache = new HashMap<>();
		this.yhistory = new Yhistory();
	}

	public AnalysisTask(AnalysisTask baseAnalysisTask, Ycommit compareCommit, Yfunction compareFunction) throws Exception {
		this();
		this.setRepositoryName(baseAnalysisTask.getRepositoryName());
		this.setRepository(baseAnalysisTask.getRepository());
		this.setFilePath(baseAnalysisTask.getFilePath());
		this.setFileHistory(baseAnalysisTask.getFileHistory());
		this.setStartCommitName(compareCommit.getName());
		this.setFunctionName(compareFunction.getName());
		this.setFunctionStartLine(compareFunction.getNameLineNumber());
		this.setFunctionEndLine(compareFunction.getEndLineNumber());
		this.buildAndValidate();
	}

	public void build() throws Exception {
		this.buildAndValidate();
		this.wasBuilt = true;
	}

	public void run() throws Exception {
		if (!wasBuilt) {
			throw new Exception("Task was not built yet. Make sure build() is called before run()");
		}
		this.createCommitCollection();
		this.createResult();
	}

	private void createResult() throws IOException {
		this.yresult = new Yresult();
		for (Ycommit ycommit : this.yhistory) {
			Ychange ychange = new Interpreter(ycommit).interpret();
			if (!(ychange instanceof Ynochange)) {
				this.yresult.put(ycommit, ychange);
			}
			if (ychange instanceof Ymetachange || ychange instanceof Ymultichange) {
				this.lastMajorChange = ychange;
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

		RevCommit startCommitRaw = Utl.findCommitByName(this.repository, this.startCommitName);
		Utl.checkNotNull("startCommit", startCommitRaw);

		String startFileContent = Utl.findFileContent(this.repository, startCommitRaw, this.filePath);
		Utl.checkNotNull("startFileContent", startFileContent);

		Yparser startParser = ParserFactory.getParser(this.fileName, startFileContent);
		startParser.parse();
		this.startFunction = startParser.findFunctionByNameAndLine(this.functionName, this.functionStartLine);
		Utl.checkNotNull("startFunctionNode", this.startFunction);

		this.functionEndLine = this.startFunction.getEndLineNumber();
		Utl.checkPositiveInt("functionEndLine", this.functionEndLine);

		String functionPath = this.startFunction.getName();
		Utl.checkNotNull("functionPath", functionPath);

		this.startCommit = getOrCreateYcommit(startCommitRaw, null);
		Utl.checkNotNull("startCommit", startCommit);
	}

	private void createCommitCollection() throws IOException, GitAPIException, NoParserFoundException {

		if (this.fileHistory == null) {
			LogCommand logCommand = this.git.log().addPath(this.filePath).setRevFilter(RevFilter.NO_MERGES);
			Iterable<RevCommit> revisions = logCommand.call();
			this.fileHistory = new LinkedHashMap<>();
			for (RevCommit commit : revisions) {
				this.fileHistory.put(commit.getName(), commit);
			}
		}

		for (RevCommit commit : this.fileHistory.values()) {
			if (commit.getCommitTime() <= this.startCommit.getCommit().getCommitTime()) {
				try {
					Ycommit ycommit = getOrCreateYcommit(commit, null);
					if (ycommit.getMatchedFunction() == null) {
						break;
					}
					if (commit.getParentCount() > 0) {
						RevCommit parentCommit = commit.getParent(0);
						Ycommit parentYcommit = getOrCreateYcommit(parentCommit, ycommit);
						ycommit.setParent(parentYcommit);
						ycommit.setYdiff(createDiffInfo(commit, parentCommit));
					}
					this.yhistory.add(ycommit);
				} catch (ParseException e) {
					System.err.println("ParseException occurred for commit or its parent. Skipping. Commit: " + commit.getName());
				}
			}
		}

	}

	private Ycommit getOrCreateYcommit(RevCommit commit, Ycommit fromChildCommit)
			throws ParseException, IOException, NoParserFoundException {

		String commitName = commit.getName();
		Ycommit ycommit = currentCommitCache.get(commitName);
		if (ycommit != null) {
			return ycommit;
		}


		Yfunction compareFunction = this.startFunction;
		if (fromChildCommit != null && fromChildCommit.getMatchedFunction() != null) {
			compareFunction = fromChildCommit.getMatchedFunction();
		}

		ycommit = createBaseYcommit(commit);
		if (ycommit.getFileContent() != null) {
			Yparser parser = ParserFactory.getParser(ycommit.getFileName(), ycommit.getFileContent());
			parser.parse();
			ycommit.setParser(parser);
			Yfunction matchedFunction = parser.findFunctionByOtherFunction(compareFunction);

			ycommit.setMatchedFunction(matchedFunction);
		}
		currentCommitCache.put(commitName, ycommit);
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
		builder.append(filePath)
				.append(fileName)
				.append(functionName)
				.append(functionStartLine)
				.append(startCommitName);
		return DigestUtils.md5Hex(builder.toString());
	}

	public void setRepository(String repositoryPath) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		this.repository = builder.setGitDir(new File(repositoryPath))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		this.git = new Git(repository);
	}

	public void setRepository(Repository repository) throws IOException {
		this.repository = repository;
		this.git = new Git(repository);
	}

	public AnalysisTask cloneTask() throws IOException {
		AnalysisTask task = new AnalysisTask();
		task.setRepository(this.repository);
		task.setFilePath(this.filePath);
		task.setFileHistory(this.fileHistory);
		return task;
	}

	public void setStartCommitName(String startCommitName) {
		this.startCommitName = startCommitName;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		String[] pathSplit = filePath.split("/");
		this.fileName = pathSplit[pathSplit.length-1];
	}

	public void setFunctionStartLine(int functionStartLine) {
		this.functionStartLine = functionStartLine;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Yresult getYresult() {
		return yresult;
	}

	public void setYhistory(Yhistory yhistory) {
		this.yhistory = yhistory;
	}

	public Ychange getLastMajorChange() {
		return lastMajorChange;
	}

	public Repository getRepository() {
		return repository;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getStartCommitName() {
		return startCommitName;
	}

	public Ycommit getStartCommit() {
		return startCommit;
	}

	public String getFunctionName() {
		return functionName;
	}

	public int getFunctionStartLine() {
		return functionStartLine;
	}

	public int getFunctionEndLine() {
		return functionEndLine;
	}

	public void setFunctionEndLine(int functionEndLine) {
		this.functionEndLine = functionEndLine;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public Map<String, RevCommit> getFileHistory() {
		return fileHistory;
	}

	public void setFileHistory(Map<String, RevCommit> fileHistory) {
		this.fileHistory = fileHistory;
	}
}
