package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.entities.Yparameter;
import com.felixgrund.codestory.ast.parser.impl.JavaParser;
import com.felixgrund.codestory.ast.util.MethodVisitor;
import com.felixgrund.codestory.ast.util.Utl;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MiningTest {

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
	private static final String FILE_PATH = "src/main/java/com/puppycrawl/tools/checkstyle/utils/CommonUtils.java";
	private static final String START_COMMIT = "119fd4fb33bef9f5c66fc950396669af842c21a3";
	private static final String BRANCH_NAME = "master";

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + "checkstyle" + "/.git";
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(repositoryPath))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		Git git = new Git(repository);
		RevCommit startCommit = Utl.findCommitByName(repository, START_COMMIT);
		String startFileContent = Utl.findFileContent(repository, startCommit, FILE_PATH);

		CompilationUnit cu = com.github.javaparser.JavaParser.parse(startFileContent);
		List<MethodDeclaration> methods = findAllMethods(cu, new MethodVisitor() {
			@Override
			public boolean methodMatches(MethodDeclaration method) {
				return !method.isAbstract();
			}
		});

		for (MethodDeclaration method : methods) {
			String name = method.getName().toString();
			int lineNumber = JavaParser.getMethodStartLine(method);
			System.out.println("=== BEGIN METHOD: " + name + ":" + lineNumber);
			AnalysisTask task = new AnalysisTask();
			task.setRepository(repositoryPath);
			task.setFilePath(FILE_PATH);
			task.setFunctionName(name);
			task.setFunctionStartLine(lineNumber);
			task.setStartCommitName(START_COMMIT);

			task.runRecursively();
			System.out.println("=== END METHOD: " + name + ":" + lineNumber);
		}
	}

	private static List<MethodDeclaration> findAllMethods(CompilationUnit cu, MethodVisitor visitor) {
		cu.accept(visitor, null);
		return visitor.getMatchedNodes();
	}

}
