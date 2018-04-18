package com.felixgrund.codestory.ast.changes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ymultichange extends Ychange {

	private List<Ychange> changes;

	public Ymultichange(List<Ychange> changes) {
		this.changes = changes;
	}

}
