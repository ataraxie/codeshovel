package com.felixgrund.codestory.ast.entities;

import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ydiff {

	private Repository repository;
	private RevCommit commit;
	private RevCommit prevCommit;

	private List<DiffEntry> diffEntries;
	private Map<String, DiffEntry> diff;
	private DiffFormatter diffFormatter;

	public Ydiff(Repository repository, RevCommit commit, RevCommit prevCommit, boolean detectRenames) throws IOException {
		this.repository = repository;
		this.commit = commit;
		this.prevCommit = prevCommit;
		init(detectRenames);
	}

	private void init(boolean detectRenames) throws IOException {
		ObjectReader objectReader = this.repository.newObjectReader();
		CanonicalTreeParser treeParserNew = new CanonicalTreeParser();
		OutputStream outputStream = System.out;
		this.diffFormatter = new DiffFormatter(outputStream);
		this.diffFormatter.setRepository(this.repository);
		this.diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		treeParserNew.reset(objectReader, this.commit.getTree());
		CanonicalTreeParser treeParserOld = new CanonicalTreeParser();
		treeParserOld.reset(objectReader, this.prevCommit.getTree());
		this.diffEntries = this.diffFormatter.scan(treeParserOld, treeParserNew);
		if (detectRenames) {
			RenameDetector rd = new RenameDetector(repository);
			rd.addAll(diffEntries);
			rd.setRenameScore(45);
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
}
