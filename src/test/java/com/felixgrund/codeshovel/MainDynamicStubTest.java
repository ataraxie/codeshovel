package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.entities.Yresult;
import com.felixgrund.codeshovel.execution.ShovelExecution;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.services.impl.CachingRepositoryService;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.GlobalEnv;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

// import static com.carrotsearch.sizeof.RamUsageEstimator.sizeOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is the main class for comparing the CodeShovel performance
 * against a pre-computed oracle file. This is useful for both
 * evaluating CodeShovel in an academic setting, but also to ensure
 * that any development changes have not decreased CodeShovel's
 * effectiveness.
 */
public class MainDynamicStubTest {

    private static final Logger log = LoggerFactory.getLogger(MainDynamicStubTest.class);
    private static final Gson GSON = new Gson();

    // These three environment variables should be set before the test
    // suite is run. They help configure the test environment without
    // having to recompile the code.

    // The folder containing the repositories the oracle is evaluating
    private static final String CODESTORY_REPO_DIR = GlobalEnv.REPO_DIR;

    // The location of the oracle files (src/resources/stubs.<LANG>)
    private static final String STUBS_DIR = "stubs/" + GlobalEnv.LANG;

    // Useful for running only one oracle (e.g., for debugging)
    // If blank, all oracles are executed
    // private static final String RUN_ONLY_TEST = GlobalEnv.ENV_NAME;

    @TestFactory
    @DisplayName("Dynamic test stubs from JSON files")
    public Collection<DynamicTest> createDynamicTests() throws Exception {

        System.out.println("Test Factory - starting");
        System.out.println("Test Factory - STUBS_DIR: " + STUBS_DIR);
        System.out.println("Test Factory - REPO_DIR: " + CODESTORY_REPO_DIR);
        System.out.println("Test Factory - ENV_NAMES ([] runs all stubs): " + GlobalEnv.ENV_NAMES);
        System.out.println("Test Factory - SKIP_ENVS ([] does not skip): " + GlobalEnv.SKIP_ENVS);

        ClassLoader classLoader = MainDynamicStubTest.class.getClassLoader();
        File directory = new File(classLoader.getResource(STUBS_DIR).getFile());

        ArrayList<DynamicTest> dynamicTests = new ArrayList<>();
//        int index = 0;
//        int numTestsRun = 0;
        int skipCount = 0;

        List<File> files = Arrays.asList(directory.listFiles());
        Collections.sort(files);

        for (File file : files) {
            String envName = file.getName().replace(".json", "");

            boolean shouldSkip = false;
            // skip anything explicitly excluded
            if (GlobalEnv.SKIP_ENVS.size() > 0) {
                // only consider skips if there are some
                for (String excludedEnv : GlobalEnv.SKIP_ENVS) {
                    if (envName.startsWith(excludedEnv.trim())) {
                        System.out.println("Skipping env due to SKIP_ENVS env var: " + envName + "; skipEnv: " + excludedEnv);
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
                System.out.println("Including env: " + envName);
                String json = FileUtils.readFileToString(file, "utf-8");
                StartEnvironment startEnv = GSON.fromJson(json, StartEnvironment.class);
                startEnv.setEnvName(envName);

                //                index++;
                //                if (GlobalEnv.BEGIN_INDEX >= 0 && GlobalEnv.MAX_RUNS >= 0) {
                //                    if (index < GlobalEnv.BEGIN_INDEX) {
                //                        System.out.println("index < GlobalEnv.BEGIN_INDEX; skip.");
                //                        continue;
                //                    }
                //                    if (numTestsRun >= GlobalEnv.MAX_RUNS) {
                //                        System.out.println("numTestsRun < GlobalEnv.MAX_RUNS; skip.");
                //                        continue;
                //                    }
                //                }
                //
                //                numTestsRun++;

                System.out.println("TestFactory - Creating suite for: " + startEnv.getEnvName());
                dynamicTests.add(createDynamicTest(startEnv));
                System.out.println("TestFactory - Suite created for: " + startEnv.getEnvName());
            }
        }
        System.out.println("TestFactory - All tests created; included #: " + dynamicTests.size() + "; skipped #: " + skipCount);

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

        String repositoryName = startEnv.getRepositoryName();
        String repositoryPath = CODESTORY_REPO_DIR + "/" + repositoryName + "/.git";
        String filePath = startEnv.getFilePath();

        try {
            test = doCreateDynamicTest(startEnv);
        } catch (Exception e) {
            log.error("Could run Shovel execution for Env: {{}}. Skipping.", startEnv.getEnvName(), e);
        }

        return test;
    }

    private DynamicTest doCreateDynamicTest(StartEnvironment startEnv) {
        Map<String, String> expectedResult = startEnv.getExpectedResult();
        String message = String.format("%s - expecting %s changes", startEnv.getEnvName(), expectedResult.size());

        System.out.println("Creating test: " + message);

        return DynamicTest.dynamicTest(
                message,
                () -> {
                    Yresult yresult = null;
                    try {
                        System.out.println("Executing test: " + message);
                        String repositoryName = startEnv.getRepositoryName();
                        String repositoryPath = CODESTORY_REPO_DIR + "/" + repositoryName + "/.git";
                        String filePath = startEnv.getFilePath();

                        Repository repository = Utl.createRepository(repositoryPath);
                        Git git = new Git(repository);
                        RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);
                        Commit startCommit = repositoryService.findCommitByName(startEnv.getStartCommitName());

                        startEnv.setRepositoryService(repositoryService);
                        startEnv.setRepositoryPath(repositoryPath);
                        startEnv.setStartCommit(startCommit);
                        startEnv.setFileName(Utl.getFileName(filePath));

                        yresult = ShovelExecution.runSingle(startEnv, startEnv.getFilePath(), true);

                        StringBuilder actualResultBuilder = new StringBuilder();
                        for (String commitName : yresult.keySet()) {
                            actualResultBuilder.append("\n").append(commitName).append(":").append(yresult.get(commitName).getTypeAsString());
                        }

                        StringBuilder expectedResultBuilder = new StringBuilder();
                        for (String commitName : expectedResult.keySet()) {
                            expectedResultBuilder.append("\n").append(commitName).append(":").append(expectedResult.get(commitName));
                        }

                        System.out.println("Performing test comparison: " + message);
                        assertEquals(expectedResultBuilder.toString(), actualResultBuilder.toString(), "stringified result should be the same");
                        assertTrue(compareResults(expectedResult, yresult), "results should be the same");
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

    private static boolean compareResults(Map<String, String> expectedResult, Yresult actualResult) {
        if (expectedResult.size() != actualResult.size()) {
            System.out.println(String.format("Result size did not match. Expected: %s, actual: %s",
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
            System.out.println("\nOnly in expected:\n" + StringUtils.join(onlyInExpected, "\n"));
            System.out.println("\nOnly in actual:\n" + StringUtils.join(onlyInActual, "\n"));
            return false;
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
