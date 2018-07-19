package com.felixgrund.codeshovel.wrappers;

import com.felixgrund.codeshovel.util.Utl;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;

public class CommitWrap {

	private RevCommit revCommit;

	public CommitWrap(RevCommit revCommit) {
		this.revCommit = revCommit;
	}

	public Date getCommitDate() {
		return Utl.getCommitDate(revCommit);
	}

	public String getAuthorName() {
		String authorName = "";
		try {
			PersonIdent ident = revCommit.getAuthorIdent();
			authorName = ident.getName();
		} catch (Exception e) {
			System.err.println("Could not parse author for commit " + Utl.getCommitNameShort(revCommit));
		}
		return authorName;
	}

	public String getAuthorEmail() {
		String authorEmail = "";
		try {
			PersonIdent ident = revCommit.getAuthorIdent();
			authorEmail = ident.getEmailAddress();
		} catch (Exception e) {
			System.err.println("Could not parse author for commit " + Utl.getCommitNameShort(revCommit));
		}
		return authorEmail;
	}

	public String getCommitMessage() {
		return revCommit.getFullMessage();
	}

	public String getCommitName() {
		return revCommit.getName();
	}

	public String getCommitDateAsString() {
		return Utl.DATE_FORMAT.format(getCommitDate());
	}

}
