package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Ydiff;
import com.felixgrund.codestory.ast.entities.Yhistory;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.exceptions.NoParserFoundException;
import com.felixgrund.codestory.ast.exceptions.ParseException;
import com.felixgrund.codestory.ast.interpreters.CrossFileInterpreter;
import com.felixgrund.codestory.ast.interpreters.InFileInterpreter;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.Environment;
import com.felixgrund.codestory.ast.util.ParserFactory;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnalysisTask {

	private static boolean CROSS_FILE = true;

	private Environment startEnv;

	private Repository repository;
	private String repositoryName;
	private String filePath;
	private String fileName;
	private String functionName;
	private String startCommitName;
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

	public AnalysisTask(Environment startEnv) {
		this.startEnv = startEnv;
		this.repository = startEnv.getRepository();
		this.repositoryName = this.repositoryName;
		this.startCommitName = startEnv.getStartCommitName();
		this.currentCommitCache = new HashMap<>();
		this.yhistory = new Yhistory();
	}

	public AnalysisTask(Environment startEnv, Yfunction oldFunction) throws Exception {
		this(startEnv);
		this.setStartCommitName(oldFunction.getCommitName());
		this.setFilePath(oldFunction.getSourceFilePath());
		this.setFunctionName(oldFunction.getName());
		this.setFunctionStartLine(oldFunction.getNameLineNumber());
		this.setFunctionEndLine(oldFunction.getEndLineNumber());

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
			InFileInterpreter ifi = new InFileInterpreter(this.startEnv, ycommit);
			Ychange ychange = ifi.interpret();
			if (!(ychange instanceof Ynochange)) {
				if (CROSS_FILE && ychange instanceof Yintroduced) {
					CrossFileInterpreter cfi = new CrossFileInterpreter(this.startEnv, ycommit);
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
		Utl.checkNotNull("repository", startEnv.getRepository());
		Utl.checkNotNull("startCommitName", this.getStartCommitName());
		Utl.checkNotNull("filePath", this.filePath);
		Utl.checkNotNull("fileName", this.fileName);
		Utl.checkNotNull("functionName", this.functionName);
		Utl.checkNotNull("functionStartLine", this.functionStartLine);

		RevCommit startCommitRaw = Utl.findCommitByName(startEnv.getRepository(), this.startCommitName);
		Utl.checkNotNull("startCommit", startCommitRaw);

		String startFileContent = Utl.findFileContent(startEnv.getRepository(), startCommitRaw, this.filePath);
		Utl.checkNotNull("startFileContent", startFileContent);

		Yparser startParser = ParserFactory.getParser(this.startEnv, this.filePath, startFileContent, this.getStartCommitName());

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

		LogCommand logCommandFile = startEnv.getGit().log().add(this.startCommit.getCommit()).addPath(this.filePath).setRevFilter(RevFilter.NO_MERGES);
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
					Ydiff ydiff = new Ydiff(startEnv.getRepository(), commit, parentCommit, false);
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
			Yparser parser = ParserFactory.getParser(this.startEnv, ycommit.getFilePath(), ycommit.getFileContent(), ycommit.getName());
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

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
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

	public Map<String, RevCommit> getFileHistory() {
		return fileHistory;
	}

	public void setFileHistory(Map<String, RevCommit> fileHistory) {
		this.fileHistory = fileHistory;
	}

	public Yfunction getStartFunction() {
		return startFunction;
	}

	public void setStartCommitName(String startCommitName) {
		this.startCommitName = startCommitName;
	}

	public String getStartCommitName() {
		return startCommitName;
	}

	public Environment getStartEnv() {
		return startEnv;
	}
}
