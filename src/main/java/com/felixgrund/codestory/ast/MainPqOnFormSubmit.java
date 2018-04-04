package com.felixgrund.codestory.ast;

import com.felixgrund.codestory.ast.entities.YCommit;
import com.felixgrund.codestory.ast.interpreters.Interpreter;
import com.felixgrund.codestory.ast.tasks.YAnalysisTask;

public class MainPqOnFormSubmit {

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
		task.setFunctionName("onFormSubmit");
		task.setFunctionStartLine(438);
		task.setStartCommitName("0540bb23561ef9921f55a83bd8bf7cc91d471bf3");

		task.run();

		for (YCommit YCommit : task.getResult()) {
			Interpreter interpreter = new Interpreter(YCommit);
			interpreter.interpret();

			if (!interpreter.getFindings().isEmpty()) {
				System.out.println("\n"+ YCommit);
				System.out.println(interpreter.getFindings());
			}

		}

	}


}
