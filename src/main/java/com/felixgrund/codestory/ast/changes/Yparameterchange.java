package com.felixgrund.codestory.ast.changes;

import com.felixgrund.codestory.ast.entities.Yparameter;

import java.lang.reflect.Parameter;
import java.util.List;

public class Yparameterchange extends Ychange {

	private List<Yparameter> oldParameters;
	private List<Yparameter> newParameters;

	public Yparameterchange(List<Yparameter> oldParameters, List<Yparameter> newParameters) {
		this.oldParameters = oldParameters;
		this.newParameters = newParameters;
	}

	public List<Yparameter> getOldParameters() {
		return oldParameters;
	}

	public List<Yparameter> getNewParameters() {
		return newParameters;
	}
}
