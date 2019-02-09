package com.felixgrund.codeshovel.tasks;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.changes.Ycomparefunctionchange;
import com.felixgrund.codeshovel.changes.Ymultichange;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.entities.Yresult;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.util.Utl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecursiveAnalysisTask {

	private StartEnvironment startEnv;
	private AnalysisTask startTask;

	private Yresult recursiveResult;
	private int numAnalyzedCommits;
	private long timeTaken;
	private boolean printOutput = true;

	public RecursiveAnalysisTask(StartEnvironment startEnv, AnalysisTask startTask) {
		this.startEnv = startEnv;
		this.startTask = startTask;
		this.numAnalyzedCommits = 0;
	}

	public void run() throws Exception {
		long startTime = new Date().getTime();
		AnalysisTask task = this.startTask;
		runAndPrintOptionally(task);
		this.recursiveResult = this.startTask.getYresult();
		this.numAnalyzedCommits += this.startTask.getNumCommitsTotal();

		while (task.getLastMajorChange() != null) {
			Ychange majorChange = task.getLastMajorChange();
			List<Ychange> changesToConsider = new ArrayList<>();
			if (majorChange instanceof Ycomparefunctionchange) {
				changesToConsider.add(majorChange);
			} else if (majorChange instanceof Ymultichange) {
				Ymultichange multiChange = (Ymultichange) majorChange;
				changesToConsider.add(multiChange.getChanges().get(0));
			}

			for (Ychange ychange : changesToConsider) {
				if (ychange instanceof Ycomparefunctionchange) {
					Ycomparefunctionchange metaChange = (Ycomparefunctionchange) ychange;
					Yfunction oldFunction = metaChange.getOldFunction();
					task = new AnalysisTask(startEnv, oldFunction);
					runAndPrintOptionally(task);
					this.recursiveResult.putAll(task.getYresult());
					this.numAnalyzedCommits += task.getNumCommitsTotal();
				}
			}
		}

		this.timeTaken = new Date().getTime() - startTime;
	}

	private void runAndPrintOptionally(AnalysisTask task) throws Exception {
		task.build();
		if (this.printOutput) {
			Utl.printAnalysisRun(task);
		}
		task.run();
		if (this.printOutput) {
			Utl.printMethodHistory(task);
		}
	}

	public void setPrintOutput(boolean printOutput) {
		this.printOutput = printOutput;
	}

	public Yresult getResult() {
		return recursiveResult;
	}

	public int getNumAnalyzedCommits() {
		return numAnalyzedCommits;
	}

	public long getTimeTaken() {
		return timeTaken;
	}
}
