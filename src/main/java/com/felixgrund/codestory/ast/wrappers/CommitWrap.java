package com.felixgrund.codestory.ast.wrappers;

import com.felixgrund.codestory.ast.util.Utl;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;

public class CommitWrap {

	private Date commitDate;
	private String authorName;
	private String authorEmail;

	public CommitWrap(RevCommit revCommit) {
		this.commitDate = Utl.getCommitDate(revCommit);
		PersonIdent ident = revCommit.getAuthorIdent();
		this.authorName = ident.getName();
		this.authorEmail = ident.getEmailAddress();
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
}
