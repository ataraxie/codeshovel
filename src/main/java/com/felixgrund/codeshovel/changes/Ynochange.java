package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.eclipse.jgit.revwalk.RevCommit;

public class Ynochange extends Ychange {

	public Ynochange(StartEnvironment startEnv, RevCommit commit) {
		super(startEnv, commit);
	}

}
