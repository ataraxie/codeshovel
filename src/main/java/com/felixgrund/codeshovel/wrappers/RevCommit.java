package com.felixgrund.codeshovel.wrappers;

import com.felixgrund.codeshovel.util.Utl;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;

import java.util.Date;

public class RevCommit {

	private String authorName;
	private String authorEmail;
	private String commitMessage;
	private String name;
	private String nameShort;
	private Date commitDate;
	private long commitTime;
	private ObjectId id;

	public RevCommit(org.eclipse.jgit.revwalk.RevCommit revCommit) {
		setName(revCommit); // this must be called first!
		setAuthorName(revCommit);
		setAuthorEmail(revCommit);
		setCommitMessage(revCommit);
		setCommitDate(revCommit);
		setId(revCommit);
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(org.eclipse.jgit.revwalk.RevCommit revCommit) {
		this.id = revCommit.getId();
	}

	public long getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(org.eclipse.jgit.revwalk.RevCommit revCommit) {
		this.commitTime = revCommit.getCommitTime();
	}

	public void setCommitDate(org.eclipse.jgit.revwalk.RevCommit revCommit) {
		this.commitDate = new Date((long) 1000 * revCommit.getCommitTime());
	}

	public String setAuthorName(org.eclipse.jgit.revwalk.RevCommit revCommit) {
		this.authorName = "";
		try {
			PersonIdent ident = revCommit.getAuthorIdent();
			this.authorName = ident.getName();
		} catch (Exception e) {
			System.err.println("Could not parse author for commit " + getCommitNameShort());
		}
		return authorName;
	}

	public String setAuthorEmail(org.eclipse.jgit.revwalk.RevCommit revCommit) {
		this.authorEmail = "";
		try {
			PersonIdent ident = revCommit.getAuthorIdent();
			this.authorEmail = ident.getEmailAddress();
		} catch (Exception e) {
			System.err.println("Could not parse author for commit " + getCommitNameShort());
		}
		return authorEmail;
	}

	public void setCommitMessage(org.eclipse.jgit.revwalk.RevCommit revCommit) {
		this.commitMessage = revCommit.getFullMessage();
	}

	public void setName(org.eclipse.jgit.revwalk.RevCommit revCommit) {
		this.name = revCommit.getName();
	}

	public String getCommitDateAsString(RevCommit revCommit) {
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
