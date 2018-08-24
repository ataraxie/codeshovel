package com.felixgrund.codeshovel.scripts;

import com.felixgrund.codeshovel.util.Utl;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class GetCommitTimestamps {

	static final String REPO_DIR = System.getenv("REPO_DIR");
	static final String OUTPUT_FILE = System.getenv("OUTPUT_FILE");

	public static void main(String[] args) throws Exception {
		log("START. Reading repos in " + REPO_DIR);
		List<String> repoNames = getSubdirectories(REPO_DIR);
		Map<String, Map<String, Long>> commitDates = new HashMap<>();
		for (String repoName : repoNames) {
			log("Repo: " + repoName);
			try {
				Map<String, Long> repoCommitDates = new HashMap<>();
				String repoPath = REPO_DIR + "/" + repoName;
				String gitPath = repoPath + "/.git";
				Repository repository = Utl.createRepository(gitPath);
				Git git = new Git(repository);
				LogCommand logCommand = git.log();
				Iterable<RevCommit> commitsIter = logCommand.call();
				List<RevCommit> commits = Lists.newArrayList(commitsIter);
				for (RevCommit commit : commits) {
					Date commitDate = new Date((long) 1000 * commit.getCommitTime());
					repoCommitDates.put(commit.getName(), commitDate.getTime());
				}
				commitDates.put(repoName, repoCommitDates);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		String json = new Gson().toJson(commitDates);
		FileUtils.writeStringToFile(new File(OUTPUT_FILE), json, "utf-8");
		log("DONE. File written to " + OUTPUT_FILE);
	}

	private static List<String> getSubdirectories(String repoDir) {
		File file = new File(repoDir);
		List<String> directories = Arrays.asList(file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		}));
		return directories;
	}

	private static void log(Object object) {
		System.out.println(object);
	}

}
