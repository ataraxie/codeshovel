package com.felixgrund.codestory.ast.entities;

import com.felixgrund.codestory.ast.changes.Ychange;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Yhistory extends ArrayList<Ycommit> {

	private Ycommit firstOccurrence;

	@Override
	public boolean add(Ycommit ycommit) {
		if (ycommit.isFunctionFound()) {
			if (this.firstOccurrence != null) {
				this.firstOccurrence.setFirstFunctionOccurrence(false);
			}
			ycommit.setFirstFunctionOccurrence(true);
			this.firstOccurrence = ycommit;
		}
		return super.add(ycommit);
	}

	public Ycommit getFirstFunctionOccurrence() {
		return firstOccurrence;
	}
}
