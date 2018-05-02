package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Ycommit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ymultichange extends Ychange {

	private List<Ychange> changes;

	public Ymultichange(Ycommit commit, List<Ychange> changes) {
		super(commit);
		this.changes = changes;
	}

}
