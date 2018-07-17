package com.felixgrund.codeshovel.wrappers;

import com.felixgrund.codeshovel.util.Utl;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;

public class CommitWrap {

	private RevCommit revCommit;
	private Date commitDate;
	private String authorName;
	private String authorEmail;

	public CommitWrap(RevCommit revCommit) {
		this.revCommit = revCommit;
		this.commitDate = Utl.getCommitDate(revCommit);
		try {
			PersonIdent ident = revCommit.getAuthorIdent();
			this.authorName = ident.getName();
			this.authorEmail = ident.getEmailAddress();
		} catch (Exception e) {
			this.authorName = "";
			this.authorEmail = "";
			System.err.println("Could not parse author for commit " + Utl.getCommitNameShort(revCommit));
		}
	}

	public Date getCommitDate() {
		return commitDate;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public RevCommit getRevCommit() {
		return revCommit;
	}
}
