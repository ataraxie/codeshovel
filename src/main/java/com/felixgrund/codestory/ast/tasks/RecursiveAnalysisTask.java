package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.wrappers.Environment;
import com.felixgrund.codestory.ast.util.Utl;

import java.util.ArrayList;
import java.util.List;

public class RecursiveAnalysisTask {

	private Environment startEnv;
	private AnalysisTask startTask;

	private Yresult recursiveResult;
	private boolean printOutput = true;

	public RecursiveAnalysisTask(Environment startEnv, AnalysisTask startTask) {
		this.startEnv = startEnv;
		this.startTask = startTask;
	}

	public void run() throws Exception {
		AnalysisTask task = this.startTask;
		runAndPrintOptionally(task);
		this.recursiveResult = this.startTask.getYresult();

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
				}
			}
		}

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
}
