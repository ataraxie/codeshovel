package com.felixgrund.codestory.ast;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.interpreters.Interpreter;
import com.felixgrund.codestory.ast.tasks.AnalysisLevel1Task;

public class Main {

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
		task.setRepository("/Users/felix/dev/projects/jquery/.git");
		task.setBranchName("master");
		task.setFilePath("src/data.js");
		task.setFileName("data.js");
		task.setFunctionName("data");
		task.setFunctionStartLine(97);
		task.setStartCommitName("294a3698811d6aaeabc67d2a77a5ef5fac94165a");

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
