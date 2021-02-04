package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Ycomparefunctionchange extends Ychange {

	private static final Logger log = LoggerFactory.getLogger(Ycomparefunctionchange.class);

	private static final boolean INCLUDE_META_DATA = true;

	protected Yfunction newFunction;
	protected Yfunction oldFunction;

	protected Commit oldCommit;

	protected String diffString;

	private Double daysBetweenCommits;
	private List<Commit> commitsBetweenForRepo;
	private List<Commit> commitsBetweenForFile;

	public Ycomparefunctionchange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction.getCommit());
		this.oldCommit = oldFunction.getCommit();
		this.newFunction = newFunction;
		this.oldFunction = oldFunction;
	}

	@Override
	public String toString() {
		String baseTemplate = "%s(%s:%s:%s:%s => %s:%s:%s:%s)";
		String baseString = String.format(baseTemplate,
				getClass().getSimpleName(),
				oldFunction.getCommitNameShort(),
				oldFunction.getName(),
				oldFunction.getNameLineNumber(),
				oldFunction.getSourceFilePath(),
				newFunction.getCommitNameShort(),
				newFunction.getName(),
				newFunction.getNameLineNumber(),
				newFunction.getSourceFilePath()
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

	@Override
	public JsonObject toJsonObject() {
		JsonObject obj = super.toJsonObject();
		obj.addProperty("commitDateOld", Ychange.DATE_FORMATTER.format(oldCommit.getCommitDate()));
		obj.addProperty("commitNameOld", oldCommit.getName());
		obj.addProperty("commitAuthorOld", oldCommit.getAuthorName());
		obj.addProperty("daysBetweenCommits", getDaysBetweenCommits());
		obj.addProperty("commitsBetweenForRepo", getCommitsBetweenForRepo().size());
		obj.addProperty("commitsBetweenForFile", getCommitsBetweenForFile().size());
		obj.addProperty("actualSource", newFunction.getSourceFragment());
		obj.addProperty("path", newFunction.getSourceFilePath());
		obj.addProperty("functionStartLine", newFunction.getNameLineNumber());
		obj.addProperty("functionName", newFunction.getName());
		obj.addProperty("diff", getDiffAsString());
		obj.add("extendedDetails", getExtendedDetailsJsonObject());
		return obj;
	}

	public JsonObject getExtendedDetailsJsonObject() {
		return new JsonObject();
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

	public List<Commit> getCommitsBetweenForRepo() {
		if (this.commitsBetweenForRepo == null) {
			this.commitsBetweenForRepo = new ArrayList<>();
			try {
				this.commitsBetweenForRepo = repositoryService.getCommitsBetween(
						this.oldFunction.getCommit(),
						this.newFunction.getCommit(),
						null);
			} catch (Exception e) {
				log.warn("Failed to generate diff string: " + e.getMessage());
			}
		}
		return commitsBetweenForRepo;
	}

	public List<Commit> getCommitsBetweenForFile() {
		if (this.commitsBetweenForFile == null) {
			this.commitsBetweenForFile = new ArrayList<>();
			try {
				this.commitsBetweenForFile = repositoryService.getCommitsBetween(
						this.oldFunction.getCommit(),
						this.newFunction.getCommit(),
						this.newFunction.getSourceFilePath());
			} catch (Exception e) {
				// Note: this is a relatively common error and seems to be handled correctly by callers
				System.err.println("Ycomparefunctionchange::getCommitsBetweenForFile() - failed to get commits between files");
				if (this.oldFunction != null){
					System.out.println("oldFunc commit: "+this.oldFunction.getCommit().getCommitNameShort());
					System.out.println("oldFunc name: "+this.oldFunction.getName()+"; path: "+this.oldFunction.getSourceFilePath());
				} else {
					System.out.println("oldFunc: null");
				}

				if (this.newFunction!= null){
					System.out.println("newFunc commit: "+this.newFunction.getCommit().getCommitNameShort());
					System.out.println("newFunc name: "+this.newFunction.getName()+"; path: "+this.newFunction.getSourceFilePath());
				} else {
					System.out.println("newFunc: null");
				}

				// e.printStackTrace();
			}
		}
		return commitsBetweenForFile;
	}

	private String getDiffAsString() {
		if (this.diffString == null) {
			String sourceOldString = oldFunction.getSourceFragment();
			String sourceNewString = newFunction.getSourceFragment();
			try {
				this.diffString = this.getDiffAsString(sourceOldString, sourceNewString);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return diffString;
	}

	public Commit getOldCommit() {
		return oldCommit;
	}
}
