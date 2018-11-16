package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;

public abstract class Ychange {

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
		obj.addProperty("commitDate", commit.getCommitDate().getTime());
		obj.addProperty("commitName", commit.getName());
		obj.addProperty("commitAuthor", commit.getAuthorName());
		return obj;
	}

	public String getTypeAsString() {
		return getClass().getSimpleName();
	}

}
