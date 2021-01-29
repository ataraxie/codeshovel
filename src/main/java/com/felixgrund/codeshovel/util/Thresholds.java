package com.felixgrund.codeshovel.util;

/**
 * This enum captures all of the threshold values
 * used by the CodeShovel change tracking algorithm.
 *
 * It is important to note that there is no one
 * _right_ set of thresholds. Many different sets of
 * thresholds can work. The set that is here has been
 * robust for the cases we have examined.
 */
public enum Thresholds {
    //
    // AbstractParser thresholds
    //
    BODY_SIM_THRESHOLD(0.9f),
    SCOPE_SIM_THRESHOLD(1),

    //  X       199/200     // NEW (REMOVED CODE)
    //  10      199/200     // ORIGINAL
    //  20      199/200
    //  5       199/200
    // LINE_NUM_THRESHOLD(5), // WAS 10

    BODY_SIM_CROSS_FILE(0.8f),
    BODY_SIM_WITHIN_FILE(0.5f),

    //  0.82    0.95    199/200     // ORIGINAL
    //  0.80    0.95    195/200
    //  0.85    0.95    190/200
    //  0.82    0.90    199/200
    //  0.80    0.90    curr        REDO TBD
    MOST_SIM_FUNCTION(0.82f), // WAS 0.82f
    MOST_SIM_FUNCTION_MAX(0.95f), // WAS 0.95f

    // X    20      199/200 // NEW (remove SHORT_METHOD_LINE_THRESHOLD)
    // 3    60      199/200 // ORIGINAL
    // 4    40      199/200
    // X    40      199/200 // remove SHORT_METHOD_LINE_THRESHOLD
    // WORSE
    // 5    50      198/200
    // X    60      198/200 // remove SHORT_METHOD_LINE_THRESHOLD
    // X    50      198/200 // remove SHORT_METHOD_LINE_THRESHOLD
    // 3    X       196/200 // Remove LONG_METHOD_THRESHOLD
    // 5    X       192/200 // Remove LONG_METHOD_THRESHOLD
    // SHORT_METHOD_LINE_THRESHOLD(3), // WAS 3
    LONG_METHOD_CHAR_THRESHOLD(20), // WAS 60

    //
    // FunctionSimilarity multipliers
    //

    // 1.25,    0.75,   0.75,   1.25:       199/200 // NEW
    // 1.4,     0.8,    0.6,    1.2:        199/200 // ORIGINAL
    // 1.2,     0.8,    0.8,    1.2:        198/200
    // WORSE
    // 1.1,     0.9,    0.9,    1.1:        196/200
    // 1.5,     0.5,    0.5,    1.5:        195/200
    // 1,       1,      1,      1:          195/200
    // 1.0,     0.5,    0.5,    1.0:        167/200
    BODY_SIM_MULT(1.25f), // WAS: 1.4f
    SCOPE_SIM_MULT(0.75f), // WAS: 0.8f
    LINE_SIM_MULT( 0.75f), // WAS: 0.6f
    NAME_SIM_MULT( 1.25f); // WAS: 1.2f

    private float value = 0;

    private Thresholds(float value) {
        this.value = value;
    }

    public float val() {
        return value;
    }
}