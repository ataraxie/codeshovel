package com.felixgrund.codestory.ast.changes;

public class Yreturntypechange extends Ychange {

	private String oldReturnType;
	private String newReturnType;

	public Yreturntypechange(String oldReturnType, String newReturnType) {
		this.oldReturnType = oldReturnType;
		this.newReturnType = newReturnType;
	}

	public String getOldReturnType() {
		return oldReturnType;
	}

	public String getNewReturnType() {
		return newReturnType;
	}
}
