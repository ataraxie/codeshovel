package com.felixgrund.codestory.ast.wrappers;

public class FunctionSimilarity {

	protected double bodySimilarity = 0;
	protected double lineNumberSimilarity = 0;
	protected double scopeSimilarity = 0;
	protected double overallSimilarity = 0;

	public void computeOverallSimilarity() {
		this.overallSimilarity = (bodySimilarity + scopeSimilarity + lineNumberSimilarity) / 3;
	}

	public double getScopeSimilarity() {
		return scopeSimilarity;
	}

	public void setScopeSimilarity(double scopeSimilarity) {
		this.scopeSimilarity = scopeSimilarity;
		computeOverallSimilarity();
	}

	public double getBodySimilarity() {
		return bodySimilarity;
	}

	public double getLineNumberSimilarity() {
		return lineNumberSimilarity;
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

	@Override
	public String toString() {
		return "BodySimilarity: " + bodySimilarity + "; ScopeSimilarity: " + scopeSimilarity +
				"; LineNumberSimilarity: " + lineNumberSimilarity + " --- Overall: " + getOverallSimilarity();
	}

}
