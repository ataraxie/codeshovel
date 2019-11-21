package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;
import org.eclipse.jgit.diff.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public abstract class Ychange {

	protected static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat();

	protected Commit commit;
	protected RepositoryService repositoryService;

	public Ychange(StartEnvironment startEnv, Commit commit) {
		this.commit = commit;
		this.repositoryService = startEnv.getRepositoryService();
	}

	public Commit getCommit() {
		return commit;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public JsonObject toJsonObject() {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", getTypeAsString());
		obj.addProperty("commitMessage", commit.getCommitMessage());
		obj.addProperty("commitDate", DATE_FORMATTER.format(commit.getCommitDate()));
		obj.addProperty("commitName", commit.getName());
		obj.addProperty("commitAuthor", commit.getAuthorName());
		return obj;
	}

	public String getTypeAsString() {
		return getClass().getSimpleName();
	}

	protected String getDiffAsString(String sourceOldString, String sourceNewString) throws IOException {
		RawText sourceOld = new RawText(sourceOldString.getBytes());
		RawText sourceNew = new RawText(sourceNewString.getBytes());
		DiffAlgorithm diffAlgorithm = new HistogramDiff();
		RawTextComparator textComparator = RawTextComparator.DEFAULT;
		EditList editList = diffAlgorithm.diff(textComparator, sourceOld, sourceNew);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DiffFormatter formatter = new DiffFormatter(out);
		formatter.setContext(1000);
		formatter.format(editList, sourceOld, sourceNew);
		return out.toString(StandardCharsets.UTF_8.name());

	}

}
