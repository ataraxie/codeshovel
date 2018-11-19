package com.felixgrund.codeshovel.execution;

import com.felixgrund.codeshovel.services.RepositoryService;
import com.felixgrund.codeshovel.services.impl.CachingRepositoryService;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.wrappers.Commit;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import org.apache.commons.cli.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

/**
 * This class is the main entry point for command-line based CodeShovel executions.
 *
 * We are using Apache Commons CLI for facilitating command-line handling.
 *
 * @author Felix Grund
 */
public class MainCli {

	public static void main(String[] args) {
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption(newOption("reponame", true, "name of the repository", false));
		options.addOption(newOption("repopath", true, "path to the repository (on the local file system)", true));
		options.addOption(newOption("filepath", true, "path to the file containing the method", true));
		options.addOption(newOption("methodname", true, "name of the method", true));
		options.addOption(newOption("startline", true, "start line of the method", true));
		options.addOption(newOption("startcommit", true, "hash of the commit to begin with backwards history traversal", false));
		options.addOption(newOption("outfile", true, "path to the output file", false));

		try {
			CommandLine line = parser.parse(options, args);
			String repositoryPath = line.getOptionValue("repopath");
			String pathDelimiter = repositoryPath.contains("\\\\") ? "\\\\" : "/";
			String gitPathEnding = pathDelimiter + ".git";
			if (!repositoryPath.endsWith(gitPathEnding)) {
				repositoryPath += gitPathEnding;
			}
			String repositoryName = line.getOptionValue("reponame");
			if (repositoryName == null) {
				String[] split = repositoryPath.replace(gitPathEnding, "").split(pathDelimiter);
				repositoryName = split[split.length - 1];
			}
			String filePath = line.getOptionValue("filepath");
			String functionName = line.getOptionValue("methodname");
			int functionStartLine = Integer.parseInt(line.getOptionValue("startline"));
			String startCommitName = line.getOptionValue("startcommit");
			if (startCommitName == null) {
				startCommitName = "HEAD";
			}
			String outputFilePath = line.getOptionValue("outfile");
			if (outputFilePath == null) {
				outputFilePath = System.getProperty("user.dir") + "/" + repositoryName + "-" + functionName + "-" + functionStartLine + ".json";
			}

			Repository repository = Utl.createRepository(repositoryPath);
			Git git = new Git(repository);
			RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);
			Commit startCommit = repositoryService.findCommitByName(startCommitName);

			StartEnvironment startEnv = new StartEnvironment(repositoryService);
			startEnv.setRepositoryPath(repositoryPath);
			startEnv.setFilePath(filePath);
			startEnv.setFunctionName(functionName);
			startEnv.setFunctionStartLine(functionStartLine);
			startEnv.setStartCommitName(startCommitName);
			startEnv.setStartCommit(startCommit);
			startEnv.setFileName(Utl.getFileName(startEnv.getFilePath()));
			startEnv.setOutputFilePath(outputFilePath);

			ShovelExecution.runSingle(startEnv, startEnv.getFilePath(), true);
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar <codeshovel-jar-file>", options);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	private static Option newOption(String name, boolean hasArg, String desc, boolean required) {
		Option option = new Option(name, hasArg, desc);
		option.setRequired(required);
		return option;
	}

}
