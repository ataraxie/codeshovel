package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.execution.MiningExecution;
import com.felixgrund.codestory.ast.parser.impl.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;


public class MiningTestJava {

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
	private static final String REPO = "checkstyle";
	private static final String FILENAME = "CommonUtils.java";
	private static final String FILE_PATH = "src/main/java/com/puppycrawl/tools/checkstyle/utils/CommonUtils.java";
	private static final String START_COMMIT = "119fd4fb33bef9f5c66fc950396669af842c21a3";

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		MiningExecution execution = new MiningExecution();
		execution.setRepositoryName(REPO);
		execution.setRepositoryPath(repositoryPath);
		execution.setFileName(FILENAME);
		execution.setFilePath(FILE_PATH);
		execution.setStartCommit(START_COMMIT);
//		execution.setFunctionName("inspectPrefiltersOrTransports");

		execution.execute();
	}

	private static List<MethodDeclaration> findAllMethods(CompilationUnit cu, JavaParser.MethodVisitor visitor) {
		cu.accept(visitor, null);
		return visitor.getMatchedNodes();
	}

}
