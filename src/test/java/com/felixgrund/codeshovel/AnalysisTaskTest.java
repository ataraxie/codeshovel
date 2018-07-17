package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.changes.Ychange;
import com.felixgrund.codeshovel.entities.Ycommit;
import com.felixgrund.codeshovel.entities.Yresult;
import com.felixgrund.codeshovel.execution.ShovelExecution;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.services.impl.CachingRepositoryService;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalysisTaskTest {

	private static final String CODESTORY_REPO_DIR = System.getenv("codeshovel.repo.dir");
	private static final String STUBS_DIR = "stubs/js";

	private static final Gson GSON = new Gson();

	// Specify file name (without file extension) if you want to run only a single test.
	// e.g. "checkstyle-Checker-fireErrors";
	private static final String RUN_ONLY_TEST = "jquery-ajax-ajax-2";

	private static List<StartEnvironment> startEnvs = new ArrayList<>();

	@BeforeAll
	public static void loadStubs() throws IOException {
		ClassLoader classLoader = AnalysisTaskTest.class.getClassLoader();
		File directory = new File(classLoader.getResource(STUBS_DIR).getFile());
		for (File file : directory.listFiles()) {
			String json = FileUtils.readFileToString(file, "utf-8");
			StartEnvironment startEnv = GSON.fromJson(json, StartEnvironment.class);
			startEnv.setEnvName(file.getName().replace(".json", ""));
			if (RUN_ONLY_TEST == null || startEnv.getEnvName().equals(RUN_ONLY_TEST)) {
				startEnvs.add(startEnv);
			}
		}
	}

	@TestFactory
	@DisplayName("Dynamic test stubs from JSON files")
	public Collection<DynamicTest> createDynamicTests() throws Exception {

		Collection<DynamicTest> dynamicTests = new ArrayList<>();
		for (StartEnvironment startEnv : startEnvs) {
			System.out.println("Running dynamic test for config :" + startEnv.getEnvName());

			String repositoryName = startEnv.getRepositoryName();
			String repositoryPath = CODESTORY_REPO_DIR + "/" + repositoryName + "/.git";
			String filePath = startEnv.getFilePath();
			Repository repository = Utl.createRepository(repositoryPath);
			Git git = new Git(repository);

			RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);

			RevCommit startCommit = repositoryService.findCommitByName(startEnv.getStartCommitName());

			startEnv.setRepositoryService(repositoryService);
			startEnv.setRepositoryPath(repositoryPath);
			startEnv.setStartCommit(startCommit);
			startEnv.setFileName(Utl.getFileName(filePath));

			Yresult yresult = ShovelExecution.runSingle(startEnv);

			DynamicTest test = createDynamicTest(startEnv, yresult);
			dynamicTests.add(test);
		}

		return dynamicTests;
	}

	private DynamicTest createDynamicTest(StartEnvironment startEnv, Yresult yresult) {
		String message = String.format("%s - expecting %s changes", startEnv.getEnvName(), startEnv.getExpectedResult().size());
		return DynamicTest.dynamicTest(
				message,
				() -> {
					assertTrue(compareResults(startEnv.getExpectedResult(), yresult), "results should be the same");
				}
		);
	}

	private static boolean compareResults(LinkedHashMap<String, String> expectedResult, Yresult actualResult) {
		boolean ret = true;
		if (expectedResult.size() != actualResult.size()) {
			System.err.println(String.format("Result size did not match. Expected: %s, actual: %s",
					expectedResult.size(), actualResult.size()));

			Set<String> expectedKeys = expectedResult.keySet();
			Set<String> actualKeys = new HashSet<>();
			for (Ycommit ycommit : actualResult.keySet()) {
				actualKeys.add(ycommit.getCommit().getName());
			}
			Set<String> onlyInExpected = new HashSet<>(expectedKeys);
			onlyInExpected.removeAll(actualKeys);
			Set<String> onlyInActual = new HashSet<>(actualKeys);
			onlyInActual.removeAll(expectedKeys);
			System.err.println("\nOnly in expected:\n" + StringUtils.join(onlyInExpected, "\n"));
			System.err.println("\nOnly in actual:\n" + StringUtils.join(onlyInActual, "\n"));
			return false;
		}
		for (Ycommit ycommit : actualResult.keySet()) {
			Ychange ychange = actualResult.get(ycommit);
			String commitName = ycommit.getCommit().getName();
			String expectedChangeType = expectedResult.get(commitName);
			if (expectedChangeType == null) {
				System.err.println(String.format("Expected result does not contain commit with name %s", commitName));
				return false;
			}
			String actualChangeType = ychange.getTypeAsString();
			if (!expectedChangeType.equals(actualChangeType)) {
				System.err.println(String.format("Type of change was not expected. Expected: %s, actual: %s",
						expectedChangeType, actualChangeType));
				return false;
			}
		}

		return true;
	}

}