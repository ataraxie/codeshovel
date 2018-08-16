package com.felixgrund.codeshovel.entities;

import com.felixgrund.codeshovel.services.RepositoryService;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ydiff {

	private static final String NULL_PATH = "/dev/null";

	private static final int RENAME_SCORE = 50;

	private RepositoryService repositoryService;
	private Repository repository;

	private List<DiffEntry> diffEntries;
	private Map<String, DiffEntry> diff;
	private DiffFormatter diffFormatter;

	private Map<String, String> pathMapping;

	public Ydiff(RepositoryService repositoryService, Commit commit, Commit prevCommit, boolean detectRenames) throws IOException {
		this.repositoryService = repositoryService;
		this.repository = repositoryService.getRepository();
		init(commit, prevCommit, detectRenames);
	}

	private void init(Commit commit, Commit prevCommit, boolean detectRenames) throws IOException {
		ObjectReader objectReader = this.repository.newObjectReader();
		CanonicalTreeParser treeParserNew = new CanonicalTreeParser();
		OutputStream outputStream = System.out;
		this.diffFormatter = new DiffFormatter(outputStream);
		this.diffFormatter.setRepository(this.repository);
		this.diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		RevCommit revCommit = repositoryService.findRevCommitById(commit.getId());
		RevCommit prevRevCommit = repositoryService.findRevCommitById(prevCommit.getId());
		treeParserNew.reset(objectReader, revCommit.getTree());
		CanonicalTreeParser treeParserOld = new CanonicalTreeParser();
		treeParserOld.reset(objectReader, prevRevCommit.getTree());
		this.diffEntries = this.diffFormatter.scan(treeParserOld, treeParserNew);
		if (detectRenames) {
			RenameDetector rd = new RenameDetector(repository);
			rd.addAll(diffEntries);
			rd.setRenameScore(RENAME_SCORE);
			diffEntries = rd.compute();
		}

		this.diff = new HashMap<>();
		for (DiffEntry diffEntry : diffEntries) {
			this.diff.put(diffEntry.getNewPath(), diffEntry);
		}
	}

	public EditList getSingleEditList(String filePathEndsWith) {
		EditList editList = null;
		for (DiffEntry entry : this.diffEntries) {
			FileHeader fileHeader = null;
			try {
				fileHeader = this.diffFormatter.toFileHeader(entry);
				if (entry.getOldPath().endsWith(filePathEndsWith)) {
					editList = fileHeader.toEditList();
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return editList;
	}

	public Map<String, DiffEntry> getDiff() {
		return diff;
	}

	public Map<String, String> getPathMapping() {
		if (this.pathMapping == null) {
			this.pathMapping = new HashMap<>();
			for (DiffEntry diffEntry : this.diffEntries) {
				String oldPath = diffEntry.getOldPath();
				String newPath = diffEntry.getNewPath();
				if (!NULL_PATH.equals(oldPath)) {
					this.pathMapping.put(oldPath, newPath);
				}
			}
		}
		return this.pathMapping;
	}

}
