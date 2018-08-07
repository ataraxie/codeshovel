package com.felixgrund.codeshovel.changes;

import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.apache.commons.lang3.StringUtils;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.List;

public class Ymultichange extends Ychange {

	private List<Ychange> changes;

	public Ymultichange(StartEnvironment startEnv, Commit commit, List<Ychange> changes) {
		super(startEnv, commit);
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

	@Override
	public String getTypeAsString() {
		List<String> substrings = new ArrayList<>();
		for (Ychange change : changes) {
			substrings.add(change.getTypeAsString());
		}
		return "Ymultichange(" +StringUtils.join(substrings, ",") + ")";
	}
}
