package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.interpreters.InterpreterLevel1;
import com.felixgrund.codestory.ast.interpreters.InterpreterLevel2;

import java.io.IOException;

public class AnalysisLevel2Task {

	private AnalysisLevel1Task level1Task;
	private Yresult yresult;

	public AnalysisLevel2Task(AnalysisLevel1Task level1Task) {
		this.level1Task = level1Task;
	}

	public void run() throws Exception {
		this.yresult = new Yresult();
		Yresult level1Result = this.level1Task.getYresult();
		for (Ycommit ycommit : level1Result.keySet()) {
			Ychange ychange = level1Result.get(ycommit);
			if (ychange instanceof Yintroduced || ychange instanceof Yremoved || ychange instanceof Yadded) {
				InterpreterLevel2 interpreter = new InterpreterLevel2(ycommit, ychange);
				interpreter.interpret();
				this.yresult.put(ycommit, interpreter.getInterpretation());
			}
		}
	}

	public Yresult getYresult() {
		return yresult;
	}
}
