package com.felixgrund.codeshovel.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * This enum captures all of the threshold values
 * used by the CodeShovel change tracking algorithm.
 * <p>
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

    BODY_SIM_CROSS_FILE(0.75f),
    BODY_SIM_WITHIN_FILE(0.5f),

    MOST_SIM_FUNCTION(0.82f), // WAS 0.82f
    MOST_SIM_FUNCTION_MAX(0.95f), // WAS 0.95f

    LONG_METHOD_CHAR_THRESHOLD(20), // WAS 60

    //
    // FunctionSimilarity multipliers
    //
    BODY_SIM_MULT(1.25f), // WAS: 1.4f
    SCOPE_SIM_MULT(0.75f), // WAS: 0.8f
    LINE_SIM_MULT(0.75f), // WAS: 0.6f
    NAME_SIM_MULT(1.25f); // WAS: 1.2f

    private float base = 0;
    private float value = 0;

    private Thresholds(float value) {
        this.base = value; // only the constructor (default) value can set this
        this.value = value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    private void reset() {
        this.value = this.base;
    }

    public static void resetAll() {
        for (Thresholds t : Thresholds.values()) {
            t.reset();
        }
    }

    public float val() {
        return value;
    }

    public float base() {
        return base;
    }

//    public static String toJSON() {
//        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//        String json = GSON.toJson(Thresholds);
//        System.out.println("Thresholds JSON: "+json);
//        return json;
//    }

    public static String toDiffJSON() {
        ArrayList<JsonObject> arr = new ArrayList<>();

        for (Thresholds t : Thresholds.values()) {
            if (t.value != t.base) {
                String s = "{name: " + t.name() + ", val: " + t.value + "}";
                JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                arr.add(jsonObject);
            }
        }

        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        String json = GSON.toJson(arr);

        return json;
    }
}