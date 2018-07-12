package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.util.Environment;
import com.felixgrund.codestory.ast.util.Utl;
import com.felixgrund.codestory.ast.wrappers.CommitWrap;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.List;

public abstract class Ycomparefunctionchange extends Ychange {

	protected Yfunction newFunction;
	protected Yfunction oldFunction;

	private CommitWrap newCommit;
	private CommitWrap oldCommit;
	private long timeBetweenCommits;
	private List<RevCommit> commitsBetweenForRepo;
	private List<RevCommit> commitsBetweenForFile;

	public Ycomparefunctionchange(Environment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction.getCommitName());
		this.newFunction = newFunction;
		this.oldFunction = oldFunction;
	}

	@Override
	public String toString() {
		String template = "%s(%s:%s:%s => %s:%s:%s)";
		String string = String.format(template,
				getClass().getSimpleName(),
				oldFunction.getCommitNameShort(),
				oldFunction.getName(),
				oldFunction.getNameLineNumber(),
				oldFunction.getCommitNameShort(),
				oldFunction.getName(),
				oldFunction.getNameLineNumber()
		);

		return string;

	}

	private CommitWrap getNewCommit() throws IOException {
		if (this.newCommit == null) {
			RevCommit revCommit = Utl.findCommitByName(newFunction.getRepository(), newFunction.getCommitName());
			this.newCommit = new CommitWrap(revCommit);
		}
		return newCommit;
	}

	private CommitWrap getOldCommit() throws Exception {
		if (this.oldCommit == null) {
			RevCommit revCommit = Utl.findCommitByName(newFunction.getRepository(), oldFunction.getCommitName());
			this.oldCommit = new CommitWrap(revCommit);
		}
		return oldCommit;
	}

	public Yfunction getNewFunction() {
		return newFunction;
	}

	public Yfunction getOldFunction() {
		return oldFunction;
	}

	public long getTimeBetweenCommits() throws Exception {
		return getNewCommit().getCommitDate().getTime() - getOldCommit().getCommitDate().getTime();
	}

	public List<RevCommit> getCommitsBetweenForRepo() throws Exception {
		if (commitsBetweenForRepo == null) {

		}
		return commitsBetweenForRepo;
	}

	public List<RevCommit> getCommitsBetweenForFile() throws Exception {
		if (commitsBetweenForFile == null) {

		}
		return commitsBetweenForFile;
	}

}
