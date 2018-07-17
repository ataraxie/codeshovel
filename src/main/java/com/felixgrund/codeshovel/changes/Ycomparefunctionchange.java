package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yfunction;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;

public abstract class Ycomparefunctionchange extends Ychange {

	private static final boolean INCLUDE_META_DATA = true;

	protected Yfunction newFunction;
	protected Yfunction oldFunction;

	private Double daysBetweenCommits;
	private List<RevCommit> commitsBetweenForRepo;
	private List<RevCommit> commitsBetweenForFile;

	public Ycomparefunctionchange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction.getCommitName());
		this.newFunction = newFunction;
		this.oldFunction = oldFunction;
	}

	@Override
	public String toString() {
		String baseTemplate = "%s(%s:%s:%s => %s:%s:%s)";
		String baseString = String.format(baseTemplate,
				getClass().getSimpleName(),
				oldFunction.getCommitNameShort(),
				oldFunction.getName(),
				oldFunction.getNameLineNumber(),
				oldFunction.getCommitNameShort(),
				oldFunction.getName(),
				oldFunction.getNameLineNumber()
		);

		if (INCLUDE_META_DATA) {
			String metadataTemplate = "TimeBetweenCommits(%s) - NumCommitsBetween(ForRepo:%s,ForFile:%s)";
			String metadataString = String.format(metadataTemplate,
					getDaysBetweenCommits(),
					getCommitsBetweenForRepo().size(),
					getCommitsBetweenForFile().size());
			baseString += "|" + metadataString;
		}

		return baseString;

	}

	public Yfunction getNewFunction() {
		return newFunction;
	}

	public Yfunction getOldFunction() {
		return oldFunction;
	}

	public double getDaysBetweenCommits() {
		if (this.daysBetweenCommits == null) {
			this.daysBetweenCommits = Utl.getDaysBetweenCommits(oldFunction.getCommit(), newFunction.getCommit());
		}
		return daysBetweenCommits;
	}

	public List<RevCommit> getCommitsBetweenForRepo() {
		if (this.commitsBetweenForRepo == null) {
			this.commitsBetweenForRepo = new ArrayList<>();
			try {
				this.commitsBetweenForRepo = repositoryService.getCommitsBetween(
						this.oldFunction.getCommit(),
						this.newFunction.getCommit(),
						null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return commitsBetweenForRepo;
	}

	public List<RevCommit> getCommitsBetweenForFile() {
		if (this.commitsBetweenForFile == null) {
			this.commitsBetweenForFile = new ArrayList<>();
			try {
				this.commitsBetweenForFile = repositoryService.getCommitsBetween(
						this.oldFunction.getCommit(),
						this.newFunction.getCommit(),
						this.newFunction.getSourceFilePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return commitsBetweenForFile;
	}

}
