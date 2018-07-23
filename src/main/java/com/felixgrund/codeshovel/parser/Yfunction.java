package com.felixgrund.codeshovel.parser;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.wrappers.RevCommit;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

public interface Yfunction {

	String getBody();
	String getName();
	List<Yparameter> getParameters();
	Yreturn getReturnStmt();
	Ymodifiers getModifiers();
	Yexceptions getExceptions();
	int getNameLineNumber();
	int getEndLineNumber();
	Object getRawFunction();

	String getCommitName();
	String getCommitNameShort();

	RevCommit getCommit();

	String getId();

	String getSourceFileContent();
	String getSourceFilePath();

	String getSourceFragment();

	Repository getRepository();

}
