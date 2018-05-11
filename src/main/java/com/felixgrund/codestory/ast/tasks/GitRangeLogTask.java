package com.felixgrund.codestory.ast.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GitRangeLogTask {

	private AnalysisTask startTask;

	public GitRangeLogTask(AnalysisTask startTask) {
		this.startTask = startTask;
	}

	public void run() throws Exception {
		Runtime r = Runtime.getRuntime();
		r.exec("cd /Users/felix/dev/codestory/repos/jquery");
		Process p = r.exec("git log --no-merges -L 90,114:src/ajax.js | grep 'commit\\s' | sed 's/commit//'");
		p.waitFor();
		BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = "";

		while ((line = b.readLine()) != null) {
			System.out.println(line);
		}

		b.close();
	}

	public List<String> getResult() {
		return new ArrayList<>();
	}

}
