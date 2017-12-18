package com.felixgrund.codestory.ast.util;

import com.google.common.collect.Lists;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class Utl {

	public static String getFileContentByObjectId(Repository repository, ObjectId objectId) throws IOException {
		ObjectLoader loader = repository.open(objectId);
		OutputStream output = new OutputStream()
		{
			private StringBuilder string = new StringBuilder();

			@Override
			public void write(int b) throws IOException {
				this.string.append((char) b);
			}
		};
		loader.copyTo(output);
		return output.toString();
	}

	public static RevCommit findCommitByName(Repository repository, String commitName) throws IOException {
		ObjectId objectId = ObjectId.fromString(commitName);
		RevWalk revWalk = new RevWalk(repository);
		RevCommit revCommit = revWalk.parseCommit(revWalk.lookupCommit(objectId));
		return revCommit;
	}

	public static List<RevCommit> findCommitsForBranch(Repository repository, String branchName) throws IOException {
		Ref masterRef = repository.findRef(branchName);
		ObjectId masterId = masterRef.getObjectId();

		RevWalk walk = new RevWalk(repository);
		RevCommit headCommit = walk.parseCommit(masterId);

		walk.markStart(headCommit);
		Iterator<RevCommit> iterator = walk.iterator();
		List<RevCommit> commits = Lists.newArrayList(iterator);
		return commits;
	}

	public static String findFileContent(Repository repository, RevCommit commit, String filePath) throws IOException {
		String ret = null;
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.create(filePath));
		if (treeWalk.next()) {
			ObjectId objectId = treeWalk.getObjectId(0);
			ret = Utl.getFileContentByObjectId(repository, objectId);
		}
		return ret;
	}


	public static void checkNotNull(String field, Object object) throws Exception {
		if (object == null) {
			String msg = String.format("Field '%s' was not set", field);
			throw new Exception(msg);
		}
	}

}
