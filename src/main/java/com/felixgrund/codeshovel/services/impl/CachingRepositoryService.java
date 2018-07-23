package com.felixgrund.codeshovel.services.impl;

import com.felixgrund.codeshovel.changes.Yhistory;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.util.CmdUtil;
import com.felixgrund.codeshovel.wrappers.RevCommit;
import com.sun.istack.internal.Nullable;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CachingRepositoryService implements RepositoryService {

	private static final Pattern COMMIT_NAME_PATTERN = Pattern.compile(".*([a-z0-9]{40}).*");

	private Git git;
	private Repository repository;
	private String repositoryName;
	private String repositoryPath;

	public CachingRepositoryService(Git git, Repository repository, String repositoryName, String repositoryPath) {
		this.git = git;
		this.repository = repository;
		this.repositoryName = repositoryName;
		this.repositoryPath = repositoryPath;
	}

	@Override
	public Git getGit() {
		return git;
	}

	@Override
	public Repository getRepository() {
		return repository;
	}

	@Override
	public String getRepositoryName() {
		return repositoryName;
	}

	@Override
	public String getRepositoryPath() {
		return repositoryPath;
	}

	@Override
	public List<RevCommit> getCommitsBetween(RevCommit oldCommit, RevCommit newCommit, String filePath) {

		Yhistory yhistory = getHistory(newCommit, filePath);
		List<RevCommit> commits = new ArrayList<>();
		for (RevCommit commit : yhistory.getCommits().values()) {
			if (commit.getName().equals(oldCommit.getName())) {
				break;
			} else if (commit.getCommitDate().before(oldCommit.getCommitDate())) {
				break;
			}
			commits.add(commit);
		}

		return commits;
	}

	private String getCacheKey(RevCommit startCommit, @Nullable String filePath) {
		String cacheKeyInput = startCommit.getName();
		if (filePath != null) {
			cacheKeyInput += filePath;
		}
		return DigestUtils.md5Hex(cacheKeyInput);
	}

	@Override
	public Yhistory getHistory(RevCommit startCommit, @Nullable String filePath) {

		LinkedHashMap<String, RevCommit> commits = new LinkedHashMap<>();
		LinkedHashMap<String, org.eclipse.jgit.revwalk.RevCommit> revCommits = new LinkedHashMap<>();

		try {
			LogCommand logCommand = git.log().add(startCommit.getId());
			if (filePath != null) {
				logCommand.addPath(filePath);
			}

			logCommand.setRevFilter(RevFilter.NO_MERGES);

			Iterable<org.eclipse.jgit.revwalk.RevCommit> fileRevisions = logCommand.call();
			for (org.eclipse.jgit.revwalk.RevCommit commit : fileRevisions) {
				revCommits.put(commit.getName(), commit);
				commits.put(commit.getName(), new RevCommit(commit));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Yhistory(commits, revCommits);
	}

	@Override
	public String findFileContent(RevCommit commit, String filePath) throws IOException {
		String ret = null;
		org.eclipse.jgit.revwalk.RevCommit revCommit = findRevCommitById(commit.getId());
		RevTree tree = revCommit.getTree();
		TreeWalk treeWalk = new TreeWalk(this.repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.create(filePath));
		if (treeWalk.next()) {
			ObjectId objectId = treeWalk.getObjectId(0);
			ret = this.getFileContentByObjectId(objectId);
		}
		return ret;
	}

	@Override
	public List<String> findFilesByExtension(RevCommit commit, String fileExtension) throws Exception {
		List<String> ret = new ArrayList<>();
		RevTree tree = findRevCommitById(commit.getId()).getTree();
		TreeWalk treeWalk = new TreeWalk(this.repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			String pathString = treeWalk.getPathString();
			if (treeWalk.getPathString().endsWith(fileExtension)) {
				ret.add(pathString);
			}
		}
		return ret;
	}

	@Override
	public String getFileContentByObjectId(ObjectId objectId) throws IOException {
		ObjectLoader loader = this.repository.open(objectId);
		OutputStream output = new OutputStream()
		{
			private StringBuilder string = new StringBuilder();
			@Override
			public void write(int b) {
				this.string.append((char) b);
			}
			public String toString(){
				return this.string.toString();
			}
		};
		loader.copyTo(output);
		return output.toString();
	}

	@Override
	public RevCommit getPrevCommitNeglectingFile(RevCommit commit) throws IOException {
		RevCommit ret = null;
		org.eclipse.jgit.revwalk.RevCommit revCommit = findRevCommitById(commit.getId());
		if (revCommit.getParentCount() > 0) {
			ObjectId prevCommitId = revCommit.getParent(0).getId();
			ret = new RevCommit(this.findRevCommitById(prevCommitId));
		}

		return ret;
	}

	@Override
	public List<String> gitLogRange(String startCommitName, int rangeStart, int rangeEnd, String filePath) throws Exception {

		RevCommit startCommit = this.findCommitByName(startCommitName);

		LogCommand logCommandFile = git.log().add(startCommit.getId()).addPath(filePath).setRevFilter(RevFilter.NO_MERGES);
		Iterable<org.eclipse.jgit.revwalk.RevCommit> fileRevisions = logCommandFile.call();
		Map<String, RevCommit> fileHistory = new LinkedHashMap<>();
		for (org.eclipse.jgit.revwalk.RevCommit commit : fileRevisions) {
			fileHistory.put(commit.getName(), new RevCommit(commit));
		}

		List<String> commitNames = new ArrayList<>();
		File repositoryDir = repository.getDirectory().getParentFile();

		BufferedReader reader = CmdUtil.gitLog(startCommitName, repositoryDir, rangeStart, rangeEnd, filePath);

		String line = reader.readLine();
		while (line != null) {
			Matcher matcher = COMMIT_NAME_PATTERN.matcher(line);
			if (matcher.matches() && matcher.groupCount() > 0) {
				String commitName = matcher.group(1);
				RevCommit commit = fileHistory.get(commitName);
				if (commit != null && commit.getCommitTime() <= startCommit.getCommitTime()) {
					commitNames.add(commitName);
				}
			}
			line = reader.readLine();
		}

		reader.close();

		return commitNames;
	}

	@Override
	public RevCommit findCommitByName(String commitName) throws IOException {
		ObjectId objectId = ObjectId.fromString(commitName);
		return new RevCommit(findRevCommitById(objectId));
	}

	@Override
	public org.eclipse.jgit.revwalk.RevCommit findRevCommitById(ObjectId id) throws IOException {
		RevWalk revWalk = new RevWalk(this.repository);
		return revWalk.parseCommit(revWalk.lookupCommit(id));
	}

}
