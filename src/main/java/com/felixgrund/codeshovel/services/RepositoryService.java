package com.felixgrund.codeshovel.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.RevCommit;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public interface RepositoryService {

	Repository getRepository();
	String getRepositoryName();
	String getRepositoryPath();
	Git getGit();

	List<RevCommit> getCommitsBetween(RevCommit oldCommit, RevCommit newCommit, String filePath);

	LinkedHashMap<String, RevCommit> getHistory(RevCommit startCommit, String filePath);

	String findFileContent(RevCommit commit, String filePath) throws IOException;

	List<String> findFilesByExtension(RevCommit commit, String fileExtension) throws Exception;

	String getFileContentByObjectId(ObjectId objectId) throws IOException;

	RevCommit findCommitByName(String commitName) throws IOException;

	RevCommit getPrevCommitNeglectingFile(RevCommit commit) throws IOException;

	List<String> gitLogRange(String startCommitName, int rangeStart, int rangeEnd, String filePath) throws Exception;

	org.eclipse.jgit.revwalk.RevCommit findRevCommitById(ObjectId id) throws IOException;

}
