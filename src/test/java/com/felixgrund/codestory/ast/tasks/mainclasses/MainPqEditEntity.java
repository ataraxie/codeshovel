package com.felixgrund.codestory.ast.tasks.mainclasses;

import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.tasks.AnalysisLevel1Task;

public class MainPqEditEntity {

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
		task.setFunctionName("editEntity");
		task.setFunctionStartLine(246);
		task.setStartCommitName("15c0c5ca8b14ad8ddc09e8c2c44c9265352e43cc");

		task.run();

		Yresult yresult = task.getYresult();
		for (Ycommit ycommit : yresult.keySet()) {
			System.out.println("\"" + ycommit.getCommit().getName() + "\" : \"" + yresult.get(ycommit) + "\",");
		}

	}


}
