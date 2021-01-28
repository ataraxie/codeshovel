package com.felixgrund.codeshovel.util;

public enum Thresholds {
    // AbstractParser thresholds
    BODY_SIM_THRESHOLD(0.9f),
    SCOPE_SIM_THRESHOLD(1),
    LINE_NUM_THRESHOLD(10),

    BODY_SIM_CROSS_FILE(0.8f),
    BODY_SIM_WITHIN_FILE(0.5f),

    MOST_SIM_FUNCTION(0.82f),
    MOST_SIM_FUNCTION_MAX(0.95f),

    SHORT_METHOD_THRESHOLD(3),
    LONG_METHOD_THRESHOLD(60),

    // FunctionSimilarity multipliers
    BODY_SIM_MULT(1.4f),
    SCOPE_SIM_MULT(0.8f),
    LINE_SIM_MULT( 0.6f),
    NAME_SIM_MULT( 1.2f);

    private float value = 0;

    private Thresholds(float value) {
        this.value = value;
    }

    public float val() {
        return value;
    }
}