package com.felixgrund.codeshovel.entities;

import java.util.ArrayList;
import java.util.List;

public class Yexceptions {

	public static final Yexceptions NONE = new Yexceptions(new ArrayList<>());

	private List<String> exceptions;

	public Yexceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Yexceptions && this.exceptions.equals(((Yexceptions) obj).getExceptions());
	}

	public List<String> getExceptions() {
		return exceptions;
	}

	@Override
	public String toString() {
		return this.exceptions.toString();
	}
}
