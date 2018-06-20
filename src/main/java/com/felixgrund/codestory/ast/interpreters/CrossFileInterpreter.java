package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yparser;
import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Map;

public class CrossFileInterpreter {

	private Ycommit ycommit;
	private Repository repository;

	public CrossFileInterpreter(Repository repository, Ycommit ycommit) {
		this.ycommit = ycommit;
		this.repository = repository;
	}

	public Ychange interpret() throws Exception {
		Yparser parser = ycommit.getParser();
		RevCommit commit = Utl.findCommitByName(repository, ycommit.getName());
		if (commit.getParentCount() > 0) {
			String parentName = commit.getParent(0).getName();
			RevCommit parentCommit = Utl.findCommitByName(repository, parentName);
			Map<String, EditList> editLists = Utl.getEditLists(repository, commit, parentCommit, parser.getAcceptedFileExtension(), false);
			int a = 1;
		}
		return null;
	}

}
