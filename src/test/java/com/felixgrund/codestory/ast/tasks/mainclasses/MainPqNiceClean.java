package com.felixgrund.codestory.ast.tasks.mainclasses;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.interpreters.Interpreter;
import com.felixgrund.codestory.ast.tasks.AnalysisLevel1Task;

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

		AnalysisLevel1Task task = new AnalysisLevel1Task();
		task.setRepository("/Users/felix/dev/projects_scandio/pocketquery/.git");
		task.setBranchName("master");
		task.setFilePath("src/main/resources/pocketquery/js/pocketquery-admin.js");
		task.setFileName("pocketquery-admin.js");
		task.setFunctionName("niceClean");
		task.setFunctionStartLine(70);
		task.setStartCommitName("1c39e6ff5622755e179b07f6dfc47eb043ef4a54");

		task.run();

		for (Ycommit ycommit : task.getYhistory()) {
			Interpreter interpreter = new Interpreter(ycommit);
			interpreter.interpret();

			if (!interpreter.getFindings().isEmpty()) {
				System.out.println("\n"+ ycommit);
				System.out.println(interpreter.getFindings());
			}

		}

	}


}
