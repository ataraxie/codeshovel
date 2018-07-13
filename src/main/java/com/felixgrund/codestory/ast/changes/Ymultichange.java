package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.wrappers.Environment;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Ymultichange extends Ychange {

	private List<Ychange> changes;

	public Ymultichange(Environment startEnv, String commitName, List<Ychange> changes) {
		super(startEnv, commitName);
		this.changes = changes;
	}

	public List<Ychange> getChanges() {
		return changes;
	}

	@Override
	public String toString() {
		List<String> substrings = new ArrayList<>();
		for (Ychange change : changes) {
			substrings.add(change.toString());
		}
		return "Ymultichange(\n- "+StringUtils.join(substrings, "\n- ")+"\n)";
	}
}
