package com.felixgrund.codeshovel.execution;

import com.felixgrund.codeshovel.execution.ShovelExecution;
import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.services.impl.CachingRepositoryService;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.nio.file.Paths;
import java.util.Date;

/**
 * Intended to be executed from the Dockerfile.
 *
 * The program should be started in the repository directory. Requires the relative path to the file to analyze and the
 * commit to start running at.
 *
 * cd /path/to/repo
 * java codeshovel relative/path/to/file.java sd8i3ksd823r4sdfkls83r48
 */
public class DockerExecution {

    private static String TARGET_FILE_PATH;
    private static String REPO_PATH;
    private static String START_COMMIT;
    private static String REPO_NAME;

    public static void main(String[] args) throws Exception {
        TARGET_FILE_PATH = args[0];
        START_COMMIT = args[1];
        REPO_PATH = System.getProperty("user.dir") + "/.git";
        REPO_NAME = Paths.get(REPO_PATH).getParent().getFileName().toString();

        System.out.println("Starting CodeShovel with REPO_NAME=" + REPO_NAME + " REPO_PATH=" + REPO_PATH +
                " TARGET_FILE_PATH=" + TARGET_FILE_PATH + " START_COMMIT=" + START_COMMIT);

        long start = new Date().getTime();
        execute();
        long end = new Date().getTime();
        System.out.println("Time taken: " + (end - start) / 1000 + "sec");
    }

    private static void execute() throws Exception {
        String repositoryPath = REPO_PATH; //REPO_DIR + "/" + REPO + "/.git";
        Repository repository = Utl.createRepository(repositoryPath);
        Git git = new Git(repository);

        RepositoryService repositoryService = new CachingRepositoryService(git, repository, REPO_NAME, repositoryPath);

        Commit startCommit = repositoryService.findCommitByName(START_COMMIT);

        StartEnvironment env = new StartEnvironment(repositoryService);
        env.setFilePath(TARGET_FILE_PATH);
        env.setFileName(Utl.getFileName(TARGET_FILE_PATH));
        env.setStartCommitName(START_COMMIT);
        env.setStartCommit(startCommit);
        ShovelExecution.runSingle(env, env.getFilePath(), false);
    }

}
