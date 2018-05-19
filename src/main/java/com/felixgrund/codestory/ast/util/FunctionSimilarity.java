package com.felixgrund.codestory.ast.util;

public class FunctionSimilarity {

	private double scopeSimilarity = 0;
	private double bodySimilarity = 0;
	private double lineNumberSimilarity = 0;
	private double overallSimilarity = 0;

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
		computeOverallSimilarity();
	}

	public void setBodySimilarity(double bodySimilarity) {
		this.bodySimilarity = bodySimilarity;
		computeOverallSimilarity();
	}

	public void setLineNumberSimilarity(double lineNumberSimilarity) {
		this.lineNumberSimilarity = lineNumberSimilarity;
		computeOverallSimilarity();
	}

	public double getOverallSimilarity() {
		return overallSimilarity;
	}

	private void computeOverallSimilarity() {
		this.overallSimilarity = (bodySimilarity + scopeSimilarity + lineNumberSimilarity) / 3;
	}

	@Override
	public String toString() {
		return "BodySimilarity: " + bodySimilarity + "; ScopeSimilarity: " + scopeSimilarity +
				"; LineNumberSimilarity: " + lineNumberSimilarity + " --- Overall: " + getOverallSimilarity();
	}
}
