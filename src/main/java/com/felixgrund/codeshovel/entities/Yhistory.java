package com.felixgrund.codeshovel.entities;

import com.felixgrund.codeshovel.wrappers.Commit;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.LinkedHashMap;

public class Yhistory {

	private LinkedHashMap<String, Commit> commits = new LinkedHashMap<>();
	private LinkedHashMap<String, RevCommit> revCommits = new LinkedHashMap<>();

	public Yhistory(LinkedHashMap<String, Commit> commits, LinkedHashMap<String, RevCommit> revCommits) {
		this.commits = commits;
		this.revCommits = revCommits;
	}

	public LinkedHashMap<String, Commit> getCommits() {
		return commits;
	}

	public LinkedHashMap<String, RevCommit> getRevCommits() {
		return revCommits;
	}
}
