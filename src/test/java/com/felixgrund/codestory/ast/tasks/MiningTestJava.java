package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.execution.MiningExecution;
import com.felixgrund.codestory.ast.parser.impl.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;


public class MiningTestJava {

	private static final boolean METHOD_MODE = true;

	private static final String TARGET_FILE_EXTENSION = ".java";
//	private static final String TARGET_FILE_PATH = "src/main/java/com/puppycrawl/tools/checkstyle/utils/CommonUtils.java";
	private static final String TARGET_FILE_PATH = "src/main/java/de/scandio/confluence/plugins/pocketquery/managers/impl/BandanaInternalDatabaseManager.java";
	private static final String TARGET_METHOD = "getAllDatabases";
//	private static final int TARGET_METHOD_STARTLINE = 288;
	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
//	private static final String REPO = "checkstyle";
	private static final String REPO = "pocketquery";
//	private static final String START_COMMIT = "119fd4fb33bef9f5c66fc950396669af842c21a3";
	private static final String START_COMMIT = "79c0ad833fc3b46a3b5248a3e7c826b6d0b513e1"; // master 2018-07-05

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		MiningExecution execution = new MiningExecution(TARGET_FILE_EXTENSION);
		execution.setRepositoryName(REPO);
		execution.setRepositoryPath(repositoryPath);
		execution.setStartCommitName(START_COMMIT);

		execution.setOnlyFilePath(TARGET_FILE_PATH);

		if (METHOD_MODE) {
			execution.setOnlyMethodName(TARGET_METHOD);
//			execution.setOnlyStartLine(TARGET_METHOD_STARTLINE);
		}

		execution.execute();
	}


}
