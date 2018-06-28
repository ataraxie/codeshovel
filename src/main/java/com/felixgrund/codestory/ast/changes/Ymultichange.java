package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ymultichange extends Ychange {

	private List<Ychange> changes;

	public Ymultichange(String commitName, List<Ychange> changes) {
		super(commitName);
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
		return "Ymultichange("+StringUtils.join(substrings, ",")+")";
	}
}
