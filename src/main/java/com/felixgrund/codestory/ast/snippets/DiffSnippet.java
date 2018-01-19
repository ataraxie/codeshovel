package com.felixgrund.codestory.ast.snippets;

import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

public class DiffSnippet {

	public static void main(String[] args) throws Exception {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File("/Users/felix/dev/projects_scandio/pocketquery/.git"))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();

		String path = "src/main/resources/pocketquery/js/pocketquery-admin.js";

		String bCommit = "8f6aa73c953a763fc3b5db301c44f545fd3c942c";
		RevCommit commitNew = Utl.findCommitByName(repository, bCommit);

		String aCommit = "41e0a4005afbe638961979439165c799b7842b97";
		RevCommit commitOld = Utl.findCommitByName(repository, aCommit);

		ObjectReader objectReader = repository.newObjectReader();
		CanonicalTreeParser treeParserNew = new CanonicalTreeParser();
		OutputStream outputStream = System.out;
		DiffFormatter formatter = new DiffFormatter(outputStream);
		formatter.setRepository(repository);
		formatter.setDiffComparator(RawTextComparator.DEFAULT);
		treeParserNew.reset(objectReader, commitNew.getTree());
		CanonicalTreeParser treeParserOld = new CanonicalTreeParser();
		treeParserOld.reset(objectReader, commitOld.getTree());
		List<DiffEntry> diff = formatter.scan(treeParserOld, treeParserNew);

		String contentOld = Utl.findFileContent(repository, commitOld, path);
		String contentNew = Utl.findFileContent(repository, commitNew, path);
		RawText aText = new RawText(contentOld.getBytes());
		RawText bText = new RawText(contentNew.getBytes());

		for (DiffEntry diffEntry : diff) {
			if (diffEntry.getOldPath().equals(path)) {
				FileHeader fileHeader = formatter.toFileHeader(diffEntry);
				EditList editList = fileHeader.toEditList();
				formatter.format(editList, aText, bText);
				break;
			}
		}
	}

}
