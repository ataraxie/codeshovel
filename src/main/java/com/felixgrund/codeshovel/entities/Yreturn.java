package com.felixgrund.codeshovel.entities;

public class Yreturn {

	private String type;

	public static final Yreturn NONE = new Yreturn("");

	public Yreturn(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if (obj instanceof Yreturn) {
			Yreturn otherReturn = (Yreturn) obj;
			ret = this.type.equals(otherReturn.getType());
		}
		return ret;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
