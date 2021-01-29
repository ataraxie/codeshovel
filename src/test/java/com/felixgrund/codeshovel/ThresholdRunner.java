package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.util.Thresholds;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;

import java.util.List;

public class ThresholdRunner {
    public ThresholdRunner() {
        System.out.println("ThresholdRunner::init");
    }

    public void doIt() throws Exception {
        System.out.println("ThresholdRunner::doIt() - start");

        String result = "";
        int count = 0;

        Thresholds.resetAll();
        Thresholds.MOST_SIM_FUNCTION.setValue(0.8f);
        Thresholds.MOST_SIM_FUNCTION_MAX.setValue(0.9f);
        result = run();
        count++;
        System.out.println("CONFIG RESULT"+count+" complete; config: ");
        System.out.println(Thresholds.toDiffJSON());
        System.out.println("CONFIG RESULT "+count+": "+result);

        Thresholds.resetAll();
        Thresholds.MOST_SIM_FUNCTION.setValue(0.7f);
        Thresholds.MOST_SIM_FUNCTION_MAX.setValue(0.8f);
        result = run();
        count++;
        System.out.println("CONFIG RESULT"+count+" complete; config: ");
        System.out.println(Thresholds.toDiffJSON());
        System.out.println("CONFIG RESULT "+count+": "+result);

        System.out.println("ThresholdRunner::doIt() - done");
    }
    public String run() throws Exception {
        System.out.println("ThresholdRunner::run() - start");

        MainDynamicStubTest runner = new MainDynamicStubTest();
        List<StartEnvironment> envs = runner.selectEnvironments();

        int successCount = 0;
        int failCount = 0;
        for (StartEnvironment env : envs){
            boolean success = runner.performComparison(env);
            if (success){
                System.out.println("Succeeded: "+env.getEnvName());
                successCount++;
            } else {
                System.err.println("Failed: "+env.getEnvName());
                failCount++;
            }
        }
        String msg = "# success: "+successCount+"; # fail: "+failCount;
        System.out.println("ThresholdRunner::run() - done; "+msg);
        return msg;
    }

    public static void main(String[] args) {
        ThresholdRunner tr = new ThresholdRunner();
        try{
            tr.doIt();
        }catch(Exception e){
            System.err.println("ThresholdRunner - ERROR: "+e.getMessage());
        }

    }
}