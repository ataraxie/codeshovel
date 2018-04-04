package com.felixgrund.codestory.ast;

import com.felixgrund.codestory.ast.entities.YCommit;
import com.felixgrund.codestory.ast.interpreters.Interpreter;
import com.felixgrund.codestory.ast.tasks.YAnalysisTask;

public class MainPqNiceClean {

	private static final String PROJECT_DIR = System.getProperty("user.dir");

	public static void main(String[] args) {
		try {
			execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void execute() throws Exception {

		YAnalysisTask task = new YAnalysisTask();
		task.setRepository("/Users/felix/dev/projects_scandio/pocketquery/.git");
		task.setBranchName("master");
		task.setFilePath("src/main/resources/pocketquery/js/pocketquery-admin.js");
		task.setFileName("pocketquery-admin.js");
		task.setFunctionName("niceClean");
		task.setFunctionStartLine(70);
		task.setStartCommitName("1c39e6ff5622755e179b07f6dfc47eb043ef4a54");

		task.run();

		for (YCommit yCommit : task.getResult()) {
			Interpreter interpreter = new Interpreter(yCommit);
			interpreter.interpret();

			if (!interpreter.getFindings().isEmpty()) {
				System.out.println("\n"+ yCommit);
				System.out.println(interpreter.getFindings());
			}

		}

	}


}
