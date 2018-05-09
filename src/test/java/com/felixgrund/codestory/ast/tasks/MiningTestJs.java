package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.parser.impl.JsParser;
import com.felixgrund.codestory.ast.util.Utl;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import jdk.nashorn.internal.ir.FunctionNode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.util.List;


public class MiningTestJs {

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");
	private static final String REPO = "jquery";
	private static final String FILE_PATH = "src/ajax.js";
	private static final String FILENAME = "ajax.js";
	private static final String START_COMMIT = "46ea7a3f0e8893a420e4c3321dc3aca40d96f754";

	public static void main(String[] args) throws Exception {
		String repositoryPath = CODESTORY_REPO_DIR + "/" + REPO + "/.git";
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(repositoryPath))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		Git git = new Git(repository);
		RevCommit startCommit = Utl.findCommitByName(repository, START_COMMIT);
		String startFileContent = Utl.findFileContent(repository, startCommit, FILE_PATH);

		JsParser parser = new JsParser(FILENAME, startFileContent);
		FunctionNode rootNode = (FunctionNode) parser.parse();
		List<FunctionNode> methods = findAllMethods(rootNode, new JsParser.FunctionNodeVisitor() {
			@Override
			public boolean nodeMatches(FunctionNode functionNode) {
				return !":program".equals(functionNode.getName());
			}
		});

		for (FunctionNode method : methods) {
			String name = method.getName();
			int lineNumber = method.getLineNumber();
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

	private static List<FunctionNode> findAllMethods(FunctionNode startNode, JsParser.FunctionNodeVisitor visitor) {
		startNode.accept(visitor);
		return visitor.getMatchedNodes();
	}

}
