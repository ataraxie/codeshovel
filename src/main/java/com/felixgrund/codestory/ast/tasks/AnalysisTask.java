package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.exceptions.NoParserFoundException;
import com.felixgrund.codestory.ast.exceptions.ParseException;
import com.felixgrund.codestory.ast.interpreters.CrossFileInterpreter;
import com.felixgrund.codestory.ast.interpreters.InFileInterpreter;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AnalysisTask {

	private static boolean CACHE_ENABLED = false;
	private static boolean CROSS_FILE = true;

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

	public AnalysisTask(AnalysisTask baseAnalysisTask, String compareCommitName, Yfunction compareFunction) throws Exception {
		this();
		this.setRepositoryName(baseAnalysisTask.getRepositoryName());
		this.setRepository(baseAnalysisTask.getRepository());
		this.setFilePath(compareFunction.getSourceFilePath());
		this.setStartCommitName(compareCommitName);
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

	private void createResult() throws Exception {
		this.yresult = new Yresult();
		for (Ycommit ycommit : this.yhistory) {
			Ychange ychange = new InFileInterpreter(ycommit).interpret();
			if (!(ychange instanceof Ynochange)) {
				if (CROSS_FILE && ychange instanceof Yintroduced) {
					CrossFileInterpreter cfi = new CrossFileInterpreter(
							this.repository, this.repositoryName, ycommit);
					Ychange crossFileChange = cfi.interpret();
					if (crossFileChange != null) {
						ychange = crossFileChange;
					}
				}
				this.yresult.put(ycommit, ychange);
			}
			if (hasMajorChange(ychange)) {
				this.lastMajorChange = ychange;
			}
		}
	}

	private boolean hasMajorChange(Ychange ychange) {
		if (isMajorChange(ychange)) {
			return true;
		} else if (ychange instanceof Ymultichange) {
			for (Ychange subChange : ((Ymultichange) ychange).getChanges()) {
				if (isMajorChange(subChange)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isMajorChange(Ychange ychange) {
		return ychange instanceof Yparameterchange || ychange instanceof Yrename
				|| ychange instanceof Ycrossfilechange;
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

		Yparser startParser = ParserFactory.getParser(this.repositoryName, this.filePath, startFileContent, this.startCommitName);

		this.startFunction = startParser.findFunctionByNameAndLine(this.functionName, this.functionStartLine);
		Utl.checkNotNull("startFunctionNode", this.startFunction);

		this.startCommit = getOrCreateYcommit(startCommitRaw, null);
		Utl.checkNotNull("startCommit", this.startCommit);

		this.functionEndLine = this.startFunction.getEndLineNumber();
		Utl.checkPositiveInt("functionEndLine", this.functionEndLine);

		String functionPath = this.startFunction.getName();
		Utl.checkNotNull("functionPath", functionPath);

	}

	private void createCommitCollection() throws IOException, GitAPIException, NoParserFoundException {

		LogCommand logCommandFile = this.git.log().add(this.startCommit.getCommit()).addPath(this.filePath).setRevFilter(RevFilter.NO_MERGES);
		Iterable<RevCommit> fileRevisions = logCommandFile.call();
		this.fileHistory = new LinkedHashMap<>();
		for (RevCommit commit : fileRevisions) {
			this.fileHistory.put(commit.getName(), commit);
		}

		Ycommit lastConsideredCommit = null;
		for (RevCommit commit : this.fileHistory.values()) {
			try {
				Ycommit ycommit = getOrCreateYcommit(commit, lastConsideredCommit);
				if (ycommit.getMatchedFunction() == null) {
					break;
				}
				if (commit.getParentCount() > 0) {
					RevCommit parentCommit = commit.getParent(0);
					Ycommit parentYcommit = getOrCreateYcommit(parentCommit, ycommit);
					ycommit.setPrev(parentYcommit);
					Ydiff ydiff = new Ydiff(this.repository, commit, parentCommit, false);
					ycommit.setYdiff(ydiff);
				}
				lastConsideredCommit = ycommit;
				this.yhistory.add(ycommit);
			} catch (ParseException e) {
				System.err.println("ParseException occurred for commit or its parent. Skipping. Commit: " + commit.getName());
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
			Yparser parser = ParserFactory.getParser(this.repositoryName, ycommit.getFilePath(), ycommit.getFileContent(), ycommit.getName());
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

	private void createOutputs() {

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

	public Yfunction getStartFunction() {
		return startFunction;
	}
}
