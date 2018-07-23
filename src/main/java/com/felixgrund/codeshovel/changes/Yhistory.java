package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.wrappers.RevCommit;

import java.util.LinkedHashMap;

public class Yhistory {

	private LinkedHashMap<String, RevCommit> commits = new LinkedHashMap<>();
	private LinkedHashMap<String, org.eclipse.jgit.revwalk.RevCommit> revCommits = new LinkedHashMap<>();

	public Yhistory(LinkedHashMap<String, RevCommit> commits, LinkedHashMap<String, org.eclipse.jgit.revwalk.RevCommit> revCommits) {
		this.commits = commits;
		this.revCommits = revCommits;
	}

	public LinkedHashMap<String, RevCommit> getCommits() {
		return commits;
	}

	public LinkedHashMap<String, org.eclipse.jgit.revwalk.RevCommit> getRevCommits() {
		return revCommits;
	}
}
