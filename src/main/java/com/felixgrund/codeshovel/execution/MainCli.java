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

	/*
	Entry point for the command line tool. If options are supplied incorrectly, a help message like this will
	be shown (printing it here because it shows the required arguments):

	usage: java -jar <codeshovel-jar-file>
		 -filepath <arg>      (required) path to the file containing the method
		 -methodname <arg>    (required) name of the method
		 -outfile <arg>       path to the output file. Default: current working directory
		 -reponame <arg>      name of the repository. Default: last part from repopath (before /.git)
		 -repopath <arg>      (required) path to the repository (on the local file system)
		 -startcommit <arg>   hash of the commit to begin with backwards history traversal. Default: HEAD
		 -startline <arg>     (required) start line of the method
	 */
	public static void main(String[] args) {
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption(newOption("reponame", true, "name of the repository. Default: last part from repopath (before /.git)", false));
		options.addOption(newOption("repopath", true, "(required) path to the repository (on the local file system)", true));
		options.addOption(newOption("filepath", true, "(required) path to the file containing the method", true));
		options.addOption(newOption("methodname", true, "(required) name of the method", true));
		options.addOption(newOption("startline", true, "(required) start line of the method", true));
		options.addOption(newOption("startcommit", true, "hash of the commit to begin with backwards history traversal. Default: HEAD", false));
		options.addOption(newOption("outfile", true, "path to the output file. Default: current working directory", false));

		try {
			CommandLine line = parser.parse(options, args);
			String repositoryPath = line.getOptionValue("repopath");
			// Unix vs. Windows. Probably there is a better way to do this.
			String pathDelimiter = repositoryPath.contains("\\\\") ? "\\\\" : "/";
			// Repo paths need to reference the .git directory. We add it to the path if it's not provided.
			String gitPathEnding = pathDelimiter + ".git";
			if (!repositoryPath.endsWith(gitPathEnding)) {
				repositoryPath += gitPathEnding;
			}
			// If no repo name parameter was provided we extract if from the repo path.
			String repositoryName = line.getOptionValue("reponame");
			if (repositoryName == null) {
				String[] split = repositoryPath.replace(gitPathEnding, "").split(pathDelimiter);
				repositoryName = split[split.length - 1];
			}
			String filePath = line.getOptionValue("filepath");
			String functionName = line.getOptionValue("methodname");
			int functionStartLine = Integer.parseInt(line.getOptionValue("startline"));
			// If no start commit hash was provided we use HEAD.
			String startCommitName = line.getOptionValue("startcommit");
			if (startCommitName == null) {
				startCommitName = "HEAD";
			}
			// If no output file path was provided the output file will be saved in the current directory.
			String outputFilePath = line.getOptionValue("outfile");
			if (outputFilePath == null) {
				outputFilePath = System.getProperty("user.dir") + "/" + repositoryName + "-" + functionName + "-" + functionStartLine + ".json";
			}

			// Below is the start of a CodeShovel execution as we know it.
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
