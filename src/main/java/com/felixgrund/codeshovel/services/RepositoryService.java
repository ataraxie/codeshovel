package com.felixgrund.codeshovel.services;

import com.felixgrund.codeshovel.entities.Yhistory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.List;

public interface RepositoryService {

	Repository getRepository();
	String getRepositoryName();
	String getRepositoryPath();
	Git getGit();

	List<Commit> getCommitsBetween(Commit oldCommit, Commit newCommit, String filePath);

	Yhistory getHistory(Commit startCommit, String filePath);

	String findFileContent(Commit commit, String filePath) throws IOException;

	List<String> findFilesByExtension(Commit commit, String fileExtension) throws Exception;

	String getFileContentByObjectId(ObjectId objectId) throws IOException;

	Commit findCommitByName(String commitName) throws IOException;

	Commit getPrevCommitNeglectingFile(Commit commit) throws IOException;

	List<String> gitLogRange(String startCommitName, int rangeStart, int rangeEnd, String filePath) throws Exception;

	RevCommit findRevCommitById(ObjectId id) throws IOException;

}
