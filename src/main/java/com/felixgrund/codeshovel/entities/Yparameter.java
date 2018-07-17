package com.felixgrund.codeshovel.entities;

public class Yparameter {

	public static final String TYPE_NONE = "";

	private String name;
	private String type;

	public Yparameter(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if (obj instanceof Yparameter) {
			Yparameter otherParameter = (Yparameter) obj;
			ret = this.name.equals(otherParameter.getName()) && this.type.equals(otherParameter.getType());
		}
		return ret;
	}
}
