package com.felixgrund.codeshovel.wrappers;

import com.felixgrund.codeshovel.util.Utl;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;

public class Commit {

	private String authorName;
	private String authorEmail;
	private String commitMessage;
	private String name;
	private Date commitDate;
	private long commitTime;

	public Commit(RevCommit revCommit) {
		setName(revCommit); // this must be called first!
		setAuthorName(revCommit);
		setAuthorEmail(revCommit);
		setCommitMessage(revCommit);
		setCommitDate(revCommit);
	}

	public ObjectId getId() {
		return ObjectId.fromString(this.name);
	}

	public long getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(RevCommit revCommit) {
		this.commitTime = revCommit.getCommitTime();
	}

	public void setCommitDate(RevCommit revCommit) {
		this.commitDate = new Date((long) 1000 * revCommit.getCommitTime());
	}

	public String setAuthorName(RevCommit revCommit) {
		this.authorName = "";
		try {
			PersonIdent ident = revCommit.getAuthorIdent();
			this.authorName = ident.getName();
		} catch (Exception e) {
			System.out.println("Could not parseMethods author for commit " + getCommitNameShort());
		}
		return authorName;
	}

	public String setAuthorEmail(RevCommit revCommit) {
		this.authorEmail = "";
		try {
			PersonIdent ident = revCommit.getAuthorIdent();
			this.authorEmail = ident.getEmailAddress();
		} catch (Exception e) {
			System.out.println("Could not parseMethods author for commit " + getCommitNameShort());
		}
		return authorEmail;
	}

	public void setCommitMessage(RevCommit revCommit) {
		try {
			this.commitMessage = revCommit.getFullMessage();
		} catch (NullPointerException e) {
			// It's bad style to catch a NullPointerException but I haven't found out why exactly jGit's getFullMessage
			// would throw a NullPointer. We need to ignore this because we don't want the whole Shovel run for the
			// method to fail just because there's no commit message on a commit.
			this.commitMessage = "[could not be extracted]";
		}

	}

	public void setName(RevCommit revCommit) {
		this.name = revCommit.getName();
	}

	public String getCommitDateAsString(Commit revCommit) {
		return Utl.DATE_FORMAT.format(this.commitDate);
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public String getCommitMessage() {
		return commitMessage;
	}

	public String getName() {
		return name;
	}

	public Date getCommitDate() {
		return commitDate;
	}

	public String getCommitNameShort() {
		return this.name.substring(0, 6);
	}
}
