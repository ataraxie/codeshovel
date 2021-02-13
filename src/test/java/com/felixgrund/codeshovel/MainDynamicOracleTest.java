package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.entities.Yresult;
import com.felixgrund.codeshovel.execution.ShovelExecution;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.services.impl.CachingRepositoryService;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.GlobalEnv;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is the main class for comparing the CodeShovel performance
 * against pre-computed oracle files. This is useful for both
 * evaluating CodeShovel in an academic setting, but also to ensure
 * that any development changes have not decreased CodeShovel's
 * effectiveness.
 * <p>
 * Instead of modifying the code to change what the suite is
 * testing, it uses environment variables. They are case sensitive:
 * <p>
 * Required:
 * REPO_DIR: The directory containing checked out repos for analysis.
 * * For the Java oracle these can be acquired with bin/clone-java-repositories.sh
 * LANG: The language for the oracle being used. e.g., java
 * * This should correspond to the resource dir your oracles are stored in.
 * * Oracles are usually below src/test/resources/oracles
 * * So, for Java oracles: src/test/resources/oracles/java  <-- java is LANG
 * <p>
 * Optional:
 * ENV_NAMES: The prefix for oracles you want tested. e.g., checkstyle
 * * Can also take a list: e.g., checkstyle,jgit,ju
 * * Where 'ju' would run both junit4 and junit5.
 * SKIP_NAMES: Just like ENV_NAMES but for oracles you want skipped.
 * * These override ENV_NAMES.
 * <p>
 * Legacy:
 * WRITE_SIMILARITIES
 * WRITE_RESULTS
 */

public class MainDynamicOracleTest {

    private static final Logger log = LoggerFactory.getLogger(MainDynamicOracleTest.class);
    private static final Gson GSON = new Gson();

    // These three environment variables should be set before the test
    // suite is run. They help configure the test environment without
    // having to recompile the code.

    // The folder containing the repositories the oracle is evaluating
    private static final String REPO_DIR = GlobalEnv.REPO_DIR;

    // The location of the oracle files (src/resources/oracles/<LANG>)
    private static final String ORACLE_DIR = "oracles/" + GlobalEnv.LANG;

    public List<StartEnvironment> selectEnvironments() throws IOException {
        System.out.println("Selecting Environments");

        System.out.println("selectEnvironments - ORACLE_DIR: " + ORACLE_DIR);
        System.out.println("selectEnvironments - REPO_DIR: " + REPO_DIR);
        System.out.println("selectEnvironments - ENV_NAMES ([] runs all oracles): " + GlobalEnv.ENV_NAMES);
        System.out.println("selectEnvironments - SKIP_NAMES ([] does not skip): " + GlobalEnv.SKIP_NAMES);

        ClassLoader classLoader = MainDynamicOracleTest.class.getClassLoader();

        if (REPO_DIR == null) {
            System.err.println("REPO_DIR environment variable must be specified");
            System.exit(1);
        }

        File directory = null;
        try {
            directory = new File(classLoader.getResource(ORACLE_DIR).getFile());
        } catch (Exception e) {
            System.err.println("MainDynamicOracle - ORACLE_DIR is not present: " + ORACLE_DIR);
            System.exit(1);
        }

        ArrayList<StartEnvironment> selectedEnvironments = new ArrayList<>();
        int skipCount = 0;

        List<File> files = Arrays.asList(directory.listFiles());
        Collections.sort(files);

        for (File file : files) {
            String envName = file.getName().replace(".json", "");

            boolean shouldSkip = false;
            // skip anything explicitly excluded
            if (GlobalEnv.SKIP_NAMES.size() > 0) {
                // only consider skips if there are some
                for (String excludedEnv : GlobalEnv.SKIP_NAMES) {
                    if (envName.startsWith(excludedEnv.trim())) {
                        System.out.println("Skipping env due to SKIP_NAMES env var: " + envName + "; skipEnv: " + excludedEnv);
                        shouldSkip = true;
                    }
                }
            } else {
                // no skips, keep going
            }

            boolean shouldInclude = false;
            if (shouldSkip == false) {
                // if includes specified, only consider those
                if (GlobalEnv.ENV_NAMES.size() > 0) {
                    for (String includedEnv : GlobalEnv.ENV_NAMES) {
                        if (envName.startsWith(includedEnv.trim())) {
                            shouldInclude = true;
                        }
                    }
                } else {
                    System.out.println("Including all envs due to ENV_NAME being empty");
                    shouldInclude = true;
                }
            }

            if (shouldInclude == false) {
                // System.out.println("Skipping env due to ENV_NAME not containing: " + envName);
                skipCount++;
            } else if (shouldSkip == true) {
                // System.out.println("Skipping env due to SKIP_NAMES containing: " + envName);
                skipCount++;
            } else {
                System.out.println("Including environment: " + envName);
                String json = FileUtils.readFileToString(file, "utf-8");
                StartEnvironment startEnv = GSON.fromJson(json, StartEnvironment.class);
                startEnv.setEnvName(envName);

                selectedEnvironments.add(startEnv);
            }
        }

        System.out.println("Environments Selected; # included: " +
                selectedEnvironments.size() + "; # skipped: " + skipCount);

        return selectedEnvironments;
    }

    @TestFactory
    @Execution(ExecutionMode.CONCURRENT) // NOTE: tests might only run concurrently using `mvn test`
    @DisplayName("Dynamic test oracles from JSON files")
    public Collection<DynamicTest> createDynamicTests() throws Exception {

        List<StartEnvironment> selectedEnvironments = this.selectEnvironments();

        List<DynamicTest> dynamicTests = new ArrayList<>();
        for (StartEnvironment env : selectedEnvironments) {
            System.out.println("TestFactory - Creating suite for: " + env.getEnvName());
            dynamicTests.add(createDynamicTest(env));
            System.out.println("TestFactory - Suite created for: " + env.getEnvName());
        }

        // Sort the tests so we work through them consistently
        // Specifically, need an alph order, not lexicographic
        // (e.g., Java sorts Z before a)
        Collections.sort(dynamicTests, (DynamicTest t1, DynamicTest t2) ->
                t1.getDisplayName().toLowerCase(Locale.ROOT).
                        compareTo(t2.getDisplayName().toLowerCase(Locale.ROOT)));

        return dynamicTests;
    }

    private DynamicTest createDynamicTest(StartEnvironment startEnv) throws IOException {
        DynamicTest test = null;
        System.out.println("Running dynamic test for config: " + startEnv.getEnvName());

        try {
            test = doCreateDynamicTest(startEnv);
        } catch (Exception e) {
            log.error("Could run Shovel execution for Env: {{}}. Skipping.", startEnv.getEnvName(), e);
        }

        return test;
    }

    private DynamicTest doCreateDynamicTest(StartEnvironment startEnv) {
        String message = startEnv.getEnvName();
        System.out.println("Creating test: " + message);

        return DynamicTest.dynamicTest(
                message,
                () -> {
                    Yresult yresult = null;
                    try {
                        StringBuilder actualResultBuilder = new StringBuilder();
                        StringBuilder expectedResultBuilder = new StringBuilder();
                        yresult = runEnvironment(startEnv, actualResultBuilder, expectedResultBuilder);

                        System.out.println("Performing test comparison: " + message);
                        System.out.println("Expected: \n" + expectedResultBuilder.toString());
                        System.out.println("Actual: \n" + actualResultBuilder.toString());
                        boolean results = compareResults(startEnv.getExpectedResult(), yresult); // prints useful data
                        assertEquals(expectedResultBuilder.toString(), actualResultBuilder.toString(), "stringified result should be the same");
                        assertTrue(results, "results should be the same");

                        System.out.println("Test comparison complete: " + message);
                        System.out.println("Test execution complete: " + message);
                    } catch (Exception e) {
                        log.error("Could run Shovel execution for Env: {{}}. Skipping.", startEnv.getEnvName(), e);
                        // clear large objects
                        startEnv.setRepositoryService(null);
                        yresult = null;
                        throw e;
                    }
                    // clear large objects
                    startEnv.setRepositoryService(null);
                    yresult = null;
                }
        );
    }

    public boolean performComparison(StartEnvironment startEnv) throws Exception {
        Yresult yresult = null;
        try {
            String message = startEnv.getEnvName();

            StringBuilder actualResultBuilder = new StringBuilder();
            StringBuilder expectedResultBuilder = new StringBuilder();
            yresult = runEnvironment(startEnv, actualResultBuilder, expectedResultBuilder);

            System.out.println("Performing test comparison: " + message);
            if (expectedResultBuilder.toString().equals(actualResultBuilder.toString())) {
                // good
            } else {
                System.err.println(message + " test failed; stringified result should be the same");
                return false;
            }

            if (compareResults(startEnv.getExpectedResult(), yresult)) {
                // good
            } else {
                System.err.println(message + " test failed; results should be the same");
                return false;
            }

            System.out.println("Test succeeded: " + message);
        } catch (Exception e) {
            log.error("Could run Shovel execution for Env: {{}}. Skipping.", startEnv.getEnvName(), e);
            // clear large objects
            startEnv.setRepositoryService(null);
            yresult = null;
            throw e;
        }
        // clear large objects
        startEnv.setRepositoryService(null);
        yresult = null;
        return true;
    }

    public Yresult runEnvironment(StartEnvironment startEnv, StringBuilder actualResultBuilder, StringBuilder expectedResultBuilder) throws IOException, Exception {
        String message = startEnv.getEnvName();

        System.out.println("Executing test: " + message);
        System.err.println("Execution thread: " + Thread.currentThread().getName()); // for tracking concurrent test execution
        String repositoryName = startEnv.getRepositoryName();
        String repositoryPath = REPO_DIR + "/" + repositoryName + "/.git";

        try {
            // this isn't needed here, but is just a sanity check to make sure the repositoryPath actually exists
            File f = new File(repositoryPath);
            if (!f.exists()) {
                throw new FileNotFoundException(repositoryPath);
            }
        } catch (Exception e) {
            System.err.println("MainDynamicOracle - Oracle repository is not present: " + repositoryPath);
            System.exit(1);
        }

        String filePath = startEnv.getFilePath();

        Repository repository = Utl.createRepository(repositoryPath);
        Git git = new Git(repository);
        RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);
        Commit startCommit = repositoryService.findCommitByName(startEnv.getStartCommitName());

        startEnv.setRepositoryService(repositoryService);
        startEnv.setRepositoryPath(repositoryPath);
        startEnv.setStartCommit(startCommit);
        startEnv.setFileName(Utl.getFileName(filePath));

        // compute changes
        Yresult yresult = ShovelExecution.runSingle(startEnv, startEnv.getFilePath(), true);

        for (String commitName : yresult.keySet()) {
            actualResultBuilder.append("\n").append(commitName).append(":").append(yresult.get(commitName).getTypeAsString());
        }

        Map<String, String> expectedResult = startEnv.getExpectedResult();
        for (String commitName : expectedResult.keySet()) {
            expectedResultBuilder.append("\n").append(commitName).append(":").append(expectedResult.get(commitName));
        }

        return yresult;
    }

    private static boolean compareResults(Map<String, String> expectedResult, Yresult actualResult) {

        if (expectedResult.size() != actualResult.size()) {
            System.out.println(String.format("\nNumber of expected results: %s, actual: %s",
                    expectedResult.size(), actualResult.size()));

            Set<String> expectedKeys = expectedResult.keySet();
            Set<String> actualKeys = new HashSet<>();
            for (String commitName : actualResult.keySet()) {
                actualKeys.add(commitName);
            }
            Set<String> onlyInExpected = new HashSet<>(expectedKeys);
            onlyInExpected.removeAll(actualKeys);
            Set<String> onlyInActual = new HashSet<>(actualKeys);
            onlyInActual.removeAll(expectedKeys);
            if (onlyInExpected.size() > 0) {
                System.out.println("\nOnly in expected (missing in actual): " + onlyInExpected.size() + "\n" + StringUtils.join(onlyInExpected, "\n"));
            }
            if (onlyInActual.size() > 0) {
                System.out.println("\nOnly in actual (not in expected): " + onlyInActual.size() + "\n" + StringUtils.join(onlyInActual, "\n"));
            }

            return false;
        } else {
            System.out.println("\nNumber of expected results: " + expectedResult.size() + " (all found)");
        }

        for (String commitName : actualResult.keySet()) {
            Ychange ychange = actualResult.get(commitName);
            String expectedChangeType = expectedResult.get(commitName);
            if (expectedChangeType == null) {
                System.out.println(String.format("Expected result does not contain commit with name %s", commitName));
                return false;
            }
            String actualChangeType = ychange.getTypeAsString();
            if (!expectedChangeType.equals(actualChangeType)) {
                System.out.println(String.format("Type of change was not expected for commit %s. Expected: %s, actual: %s",
                        commitName, expectedChangeType, actualChangeType));
                return false;
            }
        }
        return true;
    }

}
