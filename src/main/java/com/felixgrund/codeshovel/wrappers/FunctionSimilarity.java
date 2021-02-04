package com.felixgrund.codeshovel.wrappers;

import com.felixgrund.codeshovel.util.Thresholds;

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
        // TODO: Enhance simlarity (especially name similarity) by
        //  considering parameter similarity and return type similarity

        if (crossFile == true) {
            // Only consider body and name similarity
            this.overallSimilarity = (
                    bodySimilarity + nameSimilarity
            ) / 2; // make it a score 0..1
        } else {
            // Consider body, name, scope, and line similarity
            float max = Thresholds.NAME_SIM_MULT.val() +
                    Thresholds.SCOPE_SIM_MULT.val() +
                    Thresholds.BODY_SIM_MULT.val() +
                    Thresholds.LINE_SIM_MULT.val();

            this.overallSimilarity = (
                    nameSimilarity * Thresholds.NAME_SIM_MULT.val() +
                            scopeSimilarity * Thresholds.SCOPE_SIM_MULT.val() +
                            bodySimilarity * Thresholds.BODY_SIM_MULT.val() +
                            lineNumberSimilarity * Thresholds.LINE_SIM_MULT.val()
            ) / max; // make it a score 0..1
        }
    }

    public double getScopeSimilarity() {
        return scopeSimilarity;
    }

    public void setScopeSimilarity(double scopeSimilarity) {
        this.scopeSimilarity = scopeSimilarity;
    }

    public double getBodySimilarity() {
        return bodySimilarity;
    }

    public double getLineNumberSimilarity() {
        return lineNumberSimilarity;
    }

    public void setBodySimilarity(double bodySimilarity) {
        this.bodySimilarity = bodySimilarity;
    }

    public void setNameSimilarity(double nameSimilarity) {
        this.nameSimilarity = nameSimilarity;
    }

    public double getNameSimilarity() {
        return nameSimilarity;
    }

    public void setLineNumberSimilarity(double lineNumberSimilarity) {
        this.lineNumberSimilarity = lineNumberSimilarity;
    }

    public double getOverallSimilarity() {
        computeOverallSimilarity();
        return overallSimilarity;
    }

    @Override
    public String toString() {
        return "BodySimilarity: " + bodySimilarity + "; ScopeSimilarity: " + scopeSimilarity
                + "; NameSimilarity: " + nameSimilarity + "; LineNumberSimilarity: " + lineNumberSimilarity
                + " --- Overall: " + getOverallSimilarity();
    }
}
