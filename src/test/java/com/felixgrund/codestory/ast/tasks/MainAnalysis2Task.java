package com.felixgrund.codestory.ast.tasks;

public class MainAnalysis2Task {

	public static void main(String[] args) throws Exception {
		AnalysisLevel1Task level1task = MainAnalysis1Task.execute();
		AnalysisLevel2Task level2Task = new AnalysisLevel2Task(level1task);
			level2Task.run();
	}

}
