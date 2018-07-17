package com.felixgrund.codeshovel.wrappers;

// TODO: parameter similarity, return type similarity
public class FunctionSimilarity {

	protected boolean crossFile;

	protected double bodySimilarity = 0;
	protected double lineNumberSimilarity = 0;
	protected double scopeSimilarity = 0;
	protected double overallSimilarity = 0;
	protected double nameSimilarity = 0;

	public FunctionSimilarity(boolean crossFile) {
		this.crossFile = crossFile;
	}

	public void computeOverallSimilarity() {
		// TODO: this is just BETA BETA!
		if (crossFile) {
			this.overallSimilarity = (bodySimilarity + nameSimilarity) / 2;
		} else {
			this.overallSimilarity = (bodySimilarity + scopeSimilarity + lineNumberSimilarity + nameSimilarity) / 4;
		}
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

	public void setNameSimilarity(double nameSimilarity) {
		this.nameSimilarity = nameSimilarity;
		computeOverallSimilarity();
	}

	public double getNameSimilarity() {
		return nameSimilarity;
	}

	public void setLineNumberSimilarity(double lineNumberSimilarity) {
		this.lineNumberSimilarity = lineNumberSimilarity;
	}

	public double getOverallSimilarity() {
		return overallSimilarity;
	}

	@Override
	public String toString() {
		return "BodySimilarity: " + bodySimilarity + "; ScopeSimilarity: " + scopeSimilarity
				+ "; NameSimilarity: " + nameSimilarity + "; LineNumberSimilarity: " + lineNumberSimilarity
				+ " --- Overall: " + getOverallSimilarity();
	}

}
