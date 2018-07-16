package com.felixgrund.codestory.ast.tasks;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.entities.Yresult;
import com.felixgrund.codestory.ast.services.RepositoryService;
import com.felixgrund.codestory.ast.services.impl.CachingRepositoryService;
import com.felixgrund.codestory.ast.util.Utl;
import com.felixgrund.codestory.ast.wrappers.StartEnvironment;
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

	private static final String CODESTORY_REPO_DIR = System.getenv("codestory.repo.dir");

	// Specify file name (without file extension) if you want to run only a single test.
	// e.g. "checkstyle-Checker-fireErrors";
	private static final String RUN_ONLY_TEST = null;

	private static List<RunConfig> runConfigs = new ArrayList<>();

	@BeforeAll
	public static void loadStubs() throws IOException {
		ClassLoader classLoader = AnalysisTaskTest.class.getClassLoader();
		File directory = new File(classLoader.getResource("stubs/js").getFile());
		for (File file : directory.listFiles()) {
			String json = FileUtils.readFileToString(file, "utf-8");
			Gson gson = new Gson();
			RunConfig runConfig = gson.fromJson(json, RunConfig.class);
			runConfig.setConfigName(file.getName().replace(".json", ""));
			if (RUN_ONLY_TEST == null || runConfig.getConfigName().equals(RUN_ONLY_TEST)) {
				runConfigs.add(runConfig);
			}
		}
	}

	@TestFactory
	@DisplayName("Dynamic test stubs from JSON files")
	public Collection<DynamicTest> createDynamicTests() throws Exception {

		Collection<DynamicTest> dynamicTests = new ArrayList<>();
		for (RunConfig runConfig : runConfigs) {
			System.out.println("Running dynamic test for config :" + runConfig.getConfigName());

			String repositoryName = runConfig.getRepoName();
			String repositoryPath = CODESTORY_REPO_DIR + "/" + repositoryName + "/.git";
			String filePath = runConfig.getFilePath();
			String language = Utl.getFileExtensionWithoutDot(filePath);
			Repository repository = Utl.createRepository(repositoryPath);
			Git git = new Git(repository);

			RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);

			RevCommit startCommit = repositoryService.findCommitByName(runConfig.getStartCommitName());

			StartEnvironment env = new StartEnvironment(repositoryService);
			env.setFilePath(filePath);
			env.setStartCommitName(runConfig.getStartCommitName());
			env.setMethodName(runConfig.getMethodName());
			env.setStartLine(runConfig.getMethodStartLine());
			env.setFileExtension("." + language);
			env.setStartCommit(startCommit);

			AnalysisTask task = new AnalysisTask(env);
			task.setFilePath(runConfig.getFilePath());
			task.setMethodName(runConfig.getMethodName());
			task.setMethodStartLine(runConfig.getMethodStartLine());

			task.build();
			task.run();

			Yresult yresult = task.getYresult();

			DynamicTest test = createDynamicTest(runConfig, yresult);
			dynamicTests.add(test);
		}

		return dynamicTests;
	}

	private DynamicTest createDynamicTest(RunConfig runConfig, Yresult yresult) {
		String message = String.format("%s - expecting %s changes", runConfig.getConfigName(), runConfig.getExpectedResult().size());
		return DynamicTest.dynamicTest(
				message,
				() -> {
					assertTrue(compareResults(runConfig.getExpectedResult(), yresult), "results should be the same");
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
			System.err.println("Only in expected: " + StringUtils.join(onlyInExpected, ","));
			System.err.println("Only in actual: " + StringUtils.join(onlyInActual, ","));
			return false;
		}
		for (Ycommit ycommit : actualResult.keySet()) {
			Ychange ychange = actualResult.get(ycommit);
			String commitName = ycommit.getCommit().getName();
			String expectedChange = expectedResult.get(commitName);
			if (expectedChange == null) {
				System.err.println(String.format("Expected result does not contain commit with name %s", commitName));
				return false;
			}
			String actualChangeType = ychange.getClass().getSimpleName();
			if (!expectedChange.equals(actualChangeType)) {
				System.err.println(String.format("Type of change was not expected. Expected: %s, actual: %s",
						expectedChange, actualChangeType));
				return false;
			}
		}

		return true;
	}

}