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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainDynamicStubTest {

	private static Logger log = LoggerFactory.getLogger(MainDynamicStubTest.class);

	private static final String CODESTORY_REPO_DIR = GlobalEnv.REPO_DIR;
	private static final String STUBS_DIR = "stubs/" + GlobalEnv.LANG;

	private static final Gson GSON = new Gson();

	private static final String RUN_ONLY_TEST = GlobalEnv.ENV_NAME;


	@TestFactory
	@DisplayName("Dynamic test stubs from JSON files")
	public Collection<DynamicTest> createDynamicTests() throws Exception {

		ClassLoader classLoader = MainDynamicStubTest.class.getClassLoader();
		File directory = new File(classLoader.getResource(STUBS_DIR).getFile());

		Collection<DynamicTest> dynamicTests = new ArrayList<>();
		int index = 0;
		int numTestsRun = 0;

		List<File> files = Arrays.asList(directory.listFiles());
		Collections.sort(files);

		fileLoop: for (File file : files) {
			String envName = file.getName().replace(".json", "");
			for (String skipEnv : GlobalEnv.SKIP_ENVS) {
				if (envName.startsWith(skipEnv)) {
					System.out.println("Skipping env due to SKIP_ENVS env var: " + envName);
					continue fileLoop;
				}
			}

			String json = FileUtils.readFileToString(file, "utf-8");
			StartEnvironment startEnv = GSON.fromJson(json, StartEnvironment.class);
			startEnv.setEnvName(envName);
			boolean stubWhitelisted = envName.startsWith(RUN_ONLY_TEST);
			boolean isBlacklist = RUN_ONLY_TEST.startsWith("!");
			boolean stubBlacklisted = isBlacklist && envName.startsWith(RUN_ONLY_TEST.substring(1));
			if (RUN_ONLY_TEST == null || stubWhitelisted || (isBlacklist && !stubBlacklisted)) {
				index++;
				if (GlobalEnv.BEGIN_INDEX >= 0 && GlobalEnv.MAX_RUNS >= 0) {
					if (index < GlobalEnv.BEGIN_INDEX) {
						System.out.println("index < GlobalEnv.BEGIN_INDEX; skip.");
						continue;
					}
					if (numTestsRun >= GlobalEnv.MAX_RUNS) {
						System.out.println("numTestsRun < GlobalEnv.MAX_RUNS; skip.");
						continue;
					}
				}

				numTestsRun++;
				dynamicTests.add(createDynamicTest(startEnv));
			}
		}

		return dynamicTests;
	}

	private DynamicTest createDynamicTest(StartEnvironment startEnv) throws IOException {
		DynamicTest test = null;
		System.out.println("Running dynamic test for config :" + startEnv.getEnvName());

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

		try {
			Yresult yresult = ShovelExecution.runSingle(startEnv, startEnv.getFilePath(), true);
			test = doCreateDynamicTest(startEnv, yresult);
		} catch (Exception e) {
			log.error("Could run Shovel execution for Env: {{}}. Skipping.", startEnv.getEnvName(), e);
		}

		return test;
	}


	private DynamicTest doCreateDynamicTest(StartEnvironment startEnv, Yresult yresult) {
		Map<String, String> expectedResult = startEnv.getExpectedResult();
		String message = String.format("%s - expecting %s changes", startEnv.getEnvName(), expectedResult.size());

		StringBuilder actualResultBuilder = new StringBuilder();
		for (String commitName : yresult.keySet()) {
			actualResultBuilder.append("\n").append(commitName).append(":").append(yresult.get(commitName).getTypeAsString());
		}

		StringBuilder expectedResultBuilder = new StringBuilder();
		for (String commitName : expectedResult.keySet()) {
			expectedResultBuilder.append("\n").append(commitName).append(":").append(expectedResult.get(commitName));
		}

		return DynamicTest.dynamicTest(
				message,
				() -> {
					assertEquals(expectedResultBuilder.toString(), actualResultBuilder.toString(), "stringified result should be the same");
					assertTrue(compareResults(expectedResult, yresult), "results should be the same");
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