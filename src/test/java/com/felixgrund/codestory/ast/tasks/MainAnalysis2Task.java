package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;

public class MainAnalysis2Task {

	public static void main(String[] args) throws Exception {
		AnalysisLevel1Task level1task = MainAnalysis1Task.execute();
		AnalysisLevel2Task level2Task = new AnalysisLevel2Task(level1task);
		System.out.println("\n\n========================== \nRunning Level 2 Analysis\n==========================");
		level2Task.run();

		if (MainAnalysis1Task.PRINT_RESULT) {
			printResult(level1task, level2Task);
		}

	}

	private static void printResult(AnalysisLevel1Task level1Task, AnalysisLevel2Task level2Task) {
		Yresult yresult = level2Task.getYresult();
		for (Ycommit ycommit : yresult.keySet()) {
			Ychange ychange = yresult.get(ycommit);
			Ychange level1Change = level1Task.getYresult().get(ycommit);
			Ychange level2Change = yresult.get(ycommit);
			System.out.println(String.format("%s: change %s was in fact a %s", ycommit.getCommit().getName(), level1Change, level2Change));
		}
	}

}
