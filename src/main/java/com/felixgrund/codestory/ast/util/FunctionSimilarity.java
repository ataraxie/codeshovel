package com.felixgrund.codestory.ast.util;

public class FunctionSimilarity {

	private double scopeSimilarity;
	private double bodySimilarity;
	private double lineNumberSimilarity;

	public double getScopeSimilarity() {
		return scopeSimilarity;
	}

	public double getBodySimilarity() {
		return bodySimilarity;
	}

	public double getLineNumberSimilarity() {
		return lineNumberSimilarity;
	}

	public void setScopeSimilarity(double scopeSimilarity) {
		this.scopeSimilarity = scopeSimilarity;
	}

	public void setBodySimilarity(double bodySimilarity) {
		this.bodySimilarity = bodySimilarity;
	}

	public void setLineNumberSimilarity(double lineNumberSimilarity) {
		this.lineNumberSimilarity = lineNumberSimilarity;
	}

	public double getOverallSimilarity() {
		return (bodySimilarity + scopeSimilarity + lineNumberSimilarity) / 3;
	}

	@Override
	public String toString() {
		return "BodySimilarity: " + bodySimilarity + "; ScopeSimilarity: " + scopeSimilarity +
				"; LineNumberSimilarity: " + lineNumberSimilarity + " --- Overall: " + getOverallSimilarity();
	}
}
