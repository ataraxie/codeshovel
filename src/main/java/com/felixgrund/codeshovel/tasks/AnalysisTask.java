package com.felixgrund.codeshovel.tasks;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.entities.Ydiff;
import com.felixgrund.codeshovel.entities.Yresult;
import com.felixgrund.codeshovel.exceptions.NoParserFoundException;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.interpreters.CrossFileInterpreter;
import com.felixgrund.codeshovel.interpreters.InFileInterpreter;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.parser.Yparser;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.util.ParserFactory;
import com.felixgrund.codeshovel.util.Utl;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisTask {

	private static boolean CROSS_FILE = true;

	private StartEnvironment startEnv;
	private RepositoryService repositoryService;

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
	private List<Ycommit> taskSpecificHistory;

	private Yfunction startFunction;
	private Ycommit startCommit;

	private Yresult yresult;

	private Ychange lastMajorChange;

	private HashMap<String, Ycommit> currentCommitCache;

	public AnalysisTask(StartEnvironment startEnv) {
		this.startEnv = startEnv;
		this.repositoryService = startEnv.getRepositoryService();
		this.repository = this.repositoryService.getRepository();
		this.repositoryName = this.repositoryService.getRepositoryName();
		this.startCommitName = startEnv.getStartCommitName();
		this.currentCommitCache = new HashMap<>();
		this.taskSpecificHistory = new ArrayList<>();

		// These must be overwritten by setters if this is not the starting task:
		this.filePath = startEnv.getFilePath();
		this.fileName = startEnv.getFileName();
		this.functionStartLine = startEnv.getFunctionStartLine();
		this.functionName = startEnv.getFunctionName();
	}

	public AnalysisTask(StartEnvironment startEnv, Yfunction oldFunction) throws Exception {
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
		for (Ycommit ycommit : this.taskSpecificHistory) {
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

		RevCommit startCommitRaw = repositoryService.findCommitByName(this.startCommitName);
		Utl.checkNotNull("startCommit", startCommitRaw);

		String startFileContent = repositoryService.findFileContent(startCommitRaw, this.filePath);
		Utl.checkNotNull("startFileContent", startFileContent);

		Yparser startParser = ParserFactory.getParser(this.startEnv, this.filePath, startFileContent, startCommitRaw);

		this.startFunction = startParser.findFunctionByNameAndLine(this.functionName, this.functionStartLine);
		Utl.checkNotNull("startFunctionNode", this.startFunction);

		this.startCommit = getOrCreateYcommit(startCommitRaw, null);
		Utl.checkNotNull("startCommit", this.startCommit);

		this.functionEndLine = this.startFunction.getEndLineNumber();
		Utl.checkPositiveInt("functionEndLine", this.functionEndLine);

		String functionPath = this.startFunction.getName();
		Utl.checkNotNull("functionPath", functionPath);

	}

	private void createCommitCollection() throws IOException, NoParserFoundException {

		this.fileHistory = repositoryService.getHistory(this.startCommit.getCommit(), this.filePath);

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
				this.taskSpecificHistory.add(ycommit);
			} catch (ParseException e) {
				System.err.println("ParseException occurred for commit or its parent. Skipping. Commit: " + Utl.getCommitNameShort(commit));
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
			Yparser parser = ParserFactory.getParser(this.startEnv, ycommit.getFilePath(), ycommit.getFileContent(), ycommit.getCommit());
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
			String fileContent = repositoryService.getFileContentByObjectId(objectId);
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

	public void setTaskSpecificHistory(List<Ycommit> taskSpecificHistory) {
		this.taskSpecificHistory = taskSpecificHistory;
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

	public StartEnvironment getStartEnv() {
		return startEnv;
	}
}
