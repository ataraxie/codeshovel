package com.felixgrund.codeshovel.wrappers;

import com.felixgrund.codeshovel.util.Thresholds;

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
            // if cross-file, only consider body and name similarity
            this.overallSimilarity = (
                    bodySimilarity + nameSimilarity
            ) / 2;
        } else {
            // if within file, consider body, name, scope, and line similarity
            this.overallSimilarity = (
                    nameSimilarity * Thresholds.NAME_SIM_MULT.val() +
                            scopeSimilarity * Thresholds.SCOPE_SIM_MULT.val() +
                            bodySimilarity * Thresholds.BODY_SIM_MULT.val()+
                            lineNumberSimilarity * Thresholds.LINE_SIM_MULT.val()
            ) / 4;; // just make it a score 0..1
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
        computeOverallSimilarity();
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
