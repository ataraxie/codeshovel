package com.felixgrund.codeshovel.entities;

import com.github.javaparser.printer.lexicalpreservation.Difference;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
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

	private static final int RENAME_SCORE = 60;

	private Repository repository;
	private RevCommit commit;
	private RevCommit prevCommit;

	private List<DiffEntry> diffEntries;
	private Map<String, DiffEntry> diff;
	private DiffFormatter diffFormatter;

	private List<String> oldPaths;

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

	public List<String> getOldPaths() {
		if (this.oldPaths == null) {
			this.oldPaths = new ArrayList<>();
			for (DiffEntry diffEntry : this.diffEntries) {
				String oldPath = diffEntry.getOldPath();
				if (!NULL_PATH.equals(oldPath)) {
					this.oldPaths.add(oldPath);
				}
			}
		}
		return this.oldPaths;
	}

}
