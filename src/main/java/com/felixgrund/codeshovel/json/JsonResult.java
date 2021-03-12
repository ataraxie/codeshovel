package com.felixgrund.codeshovel.json;

import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.tasks.AnalysisTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

public class JsonResult {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().
			registerTypeAdapter(Ychange.class, new ChangeSerializer()).create();

	private String origin;
	private String repositoryName;
	private String repositoryPath;
	private String startCommitName;
	private String sourceFileName;
	private String functionName;
	private String functionId;
	private String sourceFilePath;
	private String functionAnnotation;
	private String functionDoc;
	private int functionStartLine;
	private int functionEndLine;
	private int numCommitsSeen;
	private long timeTaken;
	private List<String> changeHistory;
	private Map<String, String> changeHistoryShort;
	private Map<String, Ychange> changeHistoryDetails;

	public JsonResult(String origin, AnalysisTask startTask, List<String> changeHistory,
					  Map<String, Ychange> changeHistoryDetails, Map<String, String> changeHistoryShort) {

		StartEnvironment startEnv = startTask.getStartEnv();

		this.origin = origin;
		this.repositoryName = startEnv.getRepositoryName();
		this.repositoryPath = startEnv.getRepositoryPath();

		this.startCommitName = startTask.getStartCommitName();
		this.sourceFileName = startTask.getFileName();
		this.sourceFilePath = startTask.getFilePath();
		this.functionName = startTask.getFunctionName();
		this.functionStartLine = startTask.getFunctionStartLine();
		this.functionAnnotation = startTask.getFunctionAnnotation();
		this.functionDoc = startTask.getFunctionDoc();
		this.functionEndLine = startTask.getFunctionEndLine();
		this.functionId = startTask.getStartFunction().getId();

		this.changeHistory = changeHistory;
		this.changeHistoryDetails = changeHistoryDetails;
		this.changeHistoryShort = changeHistoryShort;
	}

	public String getStartCommitName() {
		return startCommitName;
	}

	public String getSourceFileName() {
		return sourceFileName;
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

	public List<String> getChangeHistory() {
		return changeHistory;
	}

	public String toJson() {
		return GSON.toJson(this);
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getOrigin() {
		return origin;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public String getFunctionId() {
		return functionId;
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public Map<String, Ychange> getChangeHistoryDetails() {
		return changeHistoryDetails;
	}

	public Map<String, String> getChangeHistoryShort() {
		return changeHistoryShort;
	}

	public int getNumCommitsSeen() {
		return numCommitsSeen;
	}

	public long getTimeTaken() {
		return timeTaken;
	}

	public void setNumCommitsSeen(int numCommitsSeen) {
		this.numCommitsSeen = numCommitsSeen;
	}

	public void setTimeTaken(long timeTaken) {
		this.timeTaken = timeTaken;
	}
}
