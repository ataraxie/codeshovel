package com.felixgrund.codestory.ast.tasks.mainclasses;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.tasks.AnalysisLevel1Task;

public class MainPqEntityToForm {

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
		task.setFilePath("src/main/resources/pocketquery/js/pocketquery-admin.js");
		task.setFileName("pocketquery-admin.js");
		task.setFunctionName("entityToForm");
		task.setFunctionStartLine(213);
		task.setStartCommitName("2835f9f6ec7dd37ada593f162027408e0f350dd7");

		task.run();

		Yresult yresult = task.getYresult();
		for (Ycommit ycommit : yresult.keySet()) {
			System.out.println("\"" + ycommit.getCommit().getName() + "\" : \"" + yresult.get(ycommit) + "\",");
		}

	}


}
