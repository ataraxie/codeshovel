package com.felixgrund.codeshovel.services.impl;

//import com.carrotsearch.sizeof.RamUsageEstimator;
import com.felixgrund.codeshovel.entities.Ydiff;
import com.felixgrund.codeshovel.entities.Yhistory;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.util.CmdUtil;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CachingRepositoryService implements RepositoryService {

	private static final int CACHE_SIZE_FILE_CONTENT = 50;
	private static final int CACHE_SIZE_COMMITS = 30;

	private static final Pattern COMMIT_NAME_PATTERN = Pattern.compile(".*([a-z0-9]{40}).*");

	private Git git;
	private Repository repository;
	private String repositoryName;
	private String repositoryPath;

	private static Map<String, String> cacheCommitFileContent = new HashMap<>();
	private static LinkedList<String> cacheCommitFileContentKeys = new LinkedList<>();

	private static Map<String, String> cacheObjectIdFileContent = new HashMap<>();
	private static LinkedList<String> cacheObjectIdFileContentKeys = new LinkedList<>();

	private static Map<String, RevCommit> cacheObjectIdCommit = new HashMap<>();
	private static LinkedList<String> cacheObjectIdCommitKeys = new LinkedList<>();

	private int cacheHits = 0;

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
	public List<Commit> getCommitsBetween(Commit oldCommit, Commit newCommit, String filePath) {

		Yhistory yhistory = getHistory(newCommit, filePath);
		List<Commit> commits = new ArrayList<>();
		for (Commit commit : yhistory.getCommits().values()) {
			if (commit.getName().equals(oldCommit.getName())) {
				break;
			} else if (commit.getCommitDate().before(oldCommit.getCommitDate())) {
				break;
			}
			commits.add(commit);
		}

		return commits;
	}

	private String getCacheKey(Commit startCommit, String filePath) {
		String cacheKeyInput = startCommit.getName();
		if (filePath != null) {
			cacheKeyInput += filePath;
		}
		return DigestUtils.md5Hex(cacheKeyInput);
	}

	@Override
	public Yhistory getHistory(Commit startCommit, String filePath) {

		LinkedHashMap<String, Commit> commits = new LinkedHashMap<>();
		LinkedHashMap<String, RevCommit> revCommits = new LinkedHashMap<>();

		try {
			LogCommand logCommand = git.log().add(startCommit.getId());
			if (filePath != null) {
				logCommand.addPath(filePath);
			}

			logCommand.setRevFilter(RevFilter.NO_MERGES);

			Iterable<RevCommit> fileRevisions = logCommand.call();
			for (RevCommit revCommit : fileRevisions) {
				revCommits.put(revCommit.getName(), revCommit);
				commits.put(revCommit.getName(), new Commit(revCommit));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Yhistory(commits, revCommits);
	}

	@Override
	public String findFileContent(Commit commit, String filePath) throws IOException {
		String fileContent = null;
		if (!Ydiff.NULL_PATH.equals(filePath)) {
			String cacheKey = getCacheKey(commit, filePath);
			fileContent = this.cacheCommitFileContent.get(cacheKey);
			if (fileContent == null) {
				RevCommit revCommit = findRevCommitById(commit.getId());
				RevTree tree = revCommit.getTree();
				TreeWalk treeWalk = new TreeWalk(this.repository);
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create(filePath));
				if (treeWalk.next()) {
					ObjectId objectId = treeWalk.getObjectId(0);
					fileContent = this.getFileContentByObjectId(objectId);
					handleCacheAdd(this.cacheCommitFileContent, this.cacheCommitFileContentKeys, cacheKey, fileContent, CACHE_SIZE_FILE_CONTENT);
				}
			} else {
				handleCacheHits();
			}
		}

		return fileContent;
	}

	private void handleCacheHits() {
		this.cacheHits += 1;
//		long cacheSize = (RamUsageEstimator.sizeOf(this.cacheCommitFileContent) +
//				RamUsageEstimator.sizeOf(this.cacheObjectIdCommit) +
//				RamUsageEstimator.sizeOf(this.cacheObjectIdFileContent)) / 1024;
//		if (this.cacheHits % 100 == 0) {
//			System.out.println("CACHE HITS: " + this.cacheHits);
//			System.out.println("TOTAL CACHE SIZE (KB): " + cacheSize);
//		}
	}

	private void handleCacheAdd(Map cache, LinkedList<String> cacheKeys, String cacheKey, Object value, int maxSize) {
//		if (cache.size() > CACHE_SIZE_FILE_CONTENT) {
//			String lastCacheKey = cacheKeys.getLast();
//			cache.remove(lastCacheKey);
//			cacheKeys.remove(lastCacheKey);
//		}
		cacheKeys.add(0, cacheKey);
		cache.put(cacheKey, value);
	}

	@Override
	public List<String> findFilesByExtension(Commit commit, String fileExtension) throws Exception {
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
		String cacheKey = objectId.getName();
		String fileContent = this.cacheObjectIdFileContent.get(cacheKey);
		if (fileContent == null) {
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
			fileContent = output.toString();
			handleCacheAdd(this.cacheObjectIdFileContent, this.cacheObjectIdFileContentKeys, cacheKey, fileContent, CACHE_SIZE_FILE_CONTENT);
		} else {
			handleCacheHits();
		}

		return fileContent;
	}

	@Override
	public Commit getPrevCommitNeglectingFile(Commit commit) throws IOException {
		Commit ret = null;
		RevCommit revCommit = findRevCommitById(commit.getId());
		if (revCommit.getParentCount() > 0) {
			ObjectId prevCommitId = revCommit.getParent(0).getId();
			ret = new Commit(this.findRevCommitById(prevCommitId));
		}

		return ret;
	}

	@Override
	public List<String> gitLogRange(String startCommitName, int rangeStart, int rangeEnd, String filePath) throws Exception {

		Commit startCommit = this.findCommitByName(startCommitName);

		LogCommand logCommandFile = git.log().add(startCommit.getId()).addPath(filePath).setRevFilter(RevFilter.NO_MERGES);
		Iterable<RevCommit> fileRevisions = logCommandFile.call();
		Map<String, Commit> fileHistory = new LinkedHashMap<>();
		for (RevCommit commit : fileRevisions) {
			fileHistory.put(commit.getName(), new Commit(commit));
		}

		List<String> commitNames = new ArrayList<>();
		File repositoryDir = repository.getDirectory().getParentFile();

		BufferedReader reader = CmdUtil.gitLog(startCommitName, repositoryDir, rangeStart, rangeEnd, filePath);

		String line = reader.readLine();
		while (line != null) {
			Matcher matcher = COMMIT_NAME_PATTERN.matcher(line);
			if (matcher.matches() && matcher.groupCount() > 0) {
				String commitName = matcher.group(1);
				Commit commit = fileHistory.get(commitName);
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
	public Commit findCommitByName(String commitName) throws IOException {
		ObjectId objectId;
		if (Constants.HEAD.equals(commitName)) {
			objectId = this.repository.resolve(Constants.HEAD);
		} else {
			objectId = ObjectId.fromString(commitName);
		}

		return new Commit(findRevCommitById(objectId));
	}

	@Override
	public RevCommit findRevCommitById(ObjectId id) throws IOException {
		String cacheKey = id.getName();
		RevCommit revCommit = this.cacheObjectIdCommit.get(cacheKey);
		if (revCommit == null){
			RevWalk revWalk = new RevWalk(this.repository);
			revCommit = revWalk.parseCommit(revWalk.lookupCommit(id));
			handleCacheAdd(this.cacheObjectIdCommit, this.cacheObjectIdCommitKeys, cacheKey, revCommit, CACHE_SIZE_COMMITS);
		} else {
			handleCacheHits();
		}

		return revCommit;
	}

}
