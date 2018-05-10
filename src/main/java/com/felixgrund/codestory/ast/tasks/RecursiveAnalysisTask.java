package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.changes.Ymetachange;
import com.felixgrund.codestory.ast.changes.Ymultichange;
import com.felixgrund.codestory.ast.entities.Yfunction;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.util.Utl;

import java.util.ArrayList;
import java.util.List;

public class RecursiveAnalysisTask {

	private AnalysisTask startTask;

	private Yresult recursiveResult;
	private boolean printOutput = true;

	public RecursiveAnalysisTask(AnalysisTask startTask) {
		this.startTask = startTask;
	}

	public void run() throws Exception {
		AnalysisTask task = this.startTask;
		runAndPrintOptionally(task);
		this.recursiveResult = this.startTask.getYresult();

		while (task.getLastMajorChange() != null) {
			Ychange majorChange = task.getLastMajorChange();
			List<Ychange> changesToConsider = new ArrayList<>();
			if (majorChange instanceof Ymetachange) {
				changesToConsider.add(majorChange);
			} else if (majorChange instanceof Ymultichange) {
				Ymultichange multiChange = (Ymultichange) majorChange;
				changesToConsider.addAll(multiChange.getChanges());
			}

			for (Ychange ychange : changesToConsider) {
				if (ychange instanceof Ymetachange) {
					Ymetachange metaChange = (Ymetachange) ychange;
					Yfunction compareFunction = metaChange.getCompareFunction();
					task = new AnalysisTask();
					task.setRepository(this.startTask.getRepository());
					task.setFilePath(this.startTask.getFilePath());
					task.setFileHistory(this.startTask.getFileHistory());
					task.setStartCommitName(metaChange.getCompareCommit().getName());
					task.setFunctionName(compareFunction.getName());
					task.setFunctionStartLine(compareFunction.getNameLineNumber());

					runAndPrintOptionally(task);
					this.recursiveResult.putAll(task.getYresult());
				}
			}
		}

	}

	private void runAndPrintOptionally(AnalysisTask task) throws Exception {
		if (this.printOutput) {
			Utl.printAnalysisRun(task.getStartCommitName(), task.getFunctionName(), task.getFunctionStartLine());
		}
		task.run();
		if (this.printOutput) {
			Utl.printMethodHistory(task.getYresult());
		}
	}

	public void setPrintOutput(boolean printOutput) {
		this.printOutput = printOutput;
	}

	public Yresult getRecursiveResult() {
		return recursiveResult;
	}
}
