package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;
import org.eclipse.jgit.diff.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class Ycomparefunctionchange extends Ychange {

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
				e.printStackTrace();
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
				e.printStackTrace();
			}
		}
		return commitsBetweenForFile;
	}

	public String getDiffAsString() {
		if (this.diffString == null) {
			String sourceOldString = oldFunction.getSourceFragment();
			String sourceNewString = newFunction.getSourceFragment();
			RawText sourceOld = new RawText(oldFunction.getSourceFragment().getBytes());
			RawText sourceNew = new RawText(newFunction.getSourceFragment().getBytes());
			DiffAlgorithm diffAlgorithm = new HistogramDiff();
			RawTextComparator textComparator = RawTextComparator.DEFAULT;
			EditList editList = diffAlgorithm.diff(textComparator, sourceOld, sourceNew);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DiffFormatter formatter = new DiffFormatter(out);

			try {
				int numLinesOld = Utl.getLines(sourceOldString).size();
				int numLinesNew = Utl.getLines(sourceNewString).size();
				formatter.setContext(1000);
				formatter.format(editList, sourceOld, sourceNew);
				this.diffString = out.toString(StandardCharsets.UTF_8.name());
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
