package com.felixgrund.codestory.ast.tasks.mainclasses;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.tasks.AnalysisLevel1Task;

public class MainPqOnDynamicParametersSubmit {

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");

	public static void main(String[] args) {
		try {
			execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void execute() throws Exception {

		AnalysisLevel1Task task = new AnalysisLevel1Task();
		task.setRepository(CODESTORY_REPO_DIR + "/pocketquery/.git");
		task.setBranchName("master");
		task.setFilePath("src/main/resources/pocketquery/js/pocketquery-dynamicload.js");
		task.setFileName("pocketquery-dynamicload.js");
		task.setFunctionName("onDynamicParametersSubmit");
		task.setFunctionStartLine(106);
		task.setStartCommitName("e0ac6f3aaa8342ba6a60394c7dde2dd04c627d24");

		task.run();

		Yresult yresult = task.getYresult();
		for (Ycommit ycommit : yresult.keySet()) {
			System.out.println("\"" + ycommit.getCommit().getName() + "\" : \"" + yresult.get(ycommit) + "\",");
		}

	}


}
