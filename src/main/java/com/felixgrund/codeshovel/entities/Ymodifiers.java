package com.felixgrund.codeshovel.entities;

import java.util.ArrayList;
import java.util.List;

public class Ymodifiers {

	public static final Ymodifiers NONE = new Ymodifiers(new ArrayList<>());

	private List<String> modifiers;

	public Ymodifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Ymodifiers && this.modifiers.equals(((Ymodifiers) obj).getModifiers());
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	@Override
	public String toString() {
		return this.modifiers.toString();
	}
}
