# CodeShovel - Unearthing Method Histories

Take this shovel to dig in source code history for changes to specific methods and functions. Currently implemented for Java. More languages to follow.

CodeShovel is a tool for navigating dedicated method histories, across all kinds of changes that it saw throughout its life span.

*This is research!* At the Software Practices Lab at UBC/Vancouver, we are currently working on a research paper that will describe properly how it works and why it makes lots of sense.

## Getting Started

### Prerequisites

* Java 8 or higher must be installed (`java -version` must reveal 1.8 or higher). You can find Java on the [Oracle page](https://www.oracle.com/technetwork/java/javase/downloads/index.html).
* If you want to build CodeShovel yourself: Maven 3.5.x or higher

### Running the tool from command line

In order to run from the command line `CodeShovel` for a local repository, you can simply download the most recent
version from the [Releases page](https://github.com/ataraxie/codeshovel/releases) and run it as follows:
```
java -jar codeshovel-XXX.jar OPTIONS
```

`OPTIONS` are defined as follows:

```
 -filepath <arg>      (required) path to the file containing the method
 -methodname <arg>    (required) name of the method
 -outfile <arg>       path to the output file. Default: current working directory
 -reponame <arg>      name of the repository. Default: last part from repopath (before /.git)
 -repopath <arg>      (required) path to the repository (on the local file system)
 -startcommit <arg>   hash of the commit to begin with backwards history traversal. Default: HEAD
 -startline <arg>     (required) start line of the method

```

Minimal examples:

```
java -jar codeshovel-X.X.X.jar \
	-repopath ~/checkstyle \
	-filepath src/main/java/com/puppycrawl/tools/checkstyle/Checker.java \
	-methodname fireErrors \
	-startline 384
```

### The CodeShovel result file

Each run of CodeShovel will print result summaries to your console and will also produce a result file. Result files
are in JSON and are structured as follows:

```
{
  // Origin of the request. In our case this will always be "codeshovel"
  "origin": "codeshovel",
  // Name of the repository
  "repositoryName": "checkstyle",
  // Full path to the repository
  "repositoryPath": "~/dev/codeshovel/repos/checkstyle/.git",
  // Start commit hash
  "startCommitName": "119fd4fb33bef9f5c66fc950396669af842c21a3",
  // File name of the source file
  "sourceFileName": "Checker.java",
  // Name of the method/function in play
  "functionName": "fireErrors",
  // ID for the method/function in play
  "functionId": "fireErrors___fileName-String__errors-SortedSet__LocalizedMessage__",
  // Path of the source file containing the method in play
  "sourceFilePath": "src/main/java/com/puppycrawl/tools/checkstyle/Checker.java",
  // Start line of the method/function
  "functionStartLine": 384,
  // End line of the method/function
  "functionEndLine": 399,
  // List of commit hashes that changed the function/method
  "changeHistory": [ ... ],
  // Short description with the type of change for each commit that changed the function/method
  "changeHistoryShort": { },
  // Detailed report for each commit that changed the method
  "changeHistoryDetails": { }
}
```

The `changeHistoryDetails` array contains an object for each commit that changed the method in this format:

```
{
  // The keys in the object are the hashes of the commits that changed the method/function
  "COMMIT_HASH": {
    // Type of the change
    "type": "Ybodychange",
    // Commit message
    "commitMessage": "Issue #3254: UT to verify all property types and values in XDocs",
    // Commit date
    "commitDate": 1515029424000,
    // Commit hash
    "commitName": "327c0bc843612486ab4ded32a2f01038e1271fd0", 
    // Commit author name
    "commitAuthor": "rnveach", 
    // Commit date of the parent/previous commit
    "commitDateOld": 1514928265000, 
    // Commit name of the parent/previous commit
    "commitNameOld": "dabb75d43c7e02317565dde4c5e60f380d3b16b8", 
    // Author of the parent/previous commit
    "commitAuthorOld": "Roman Ivanov", 
    // Number of days between this commit and the parent/previous commit
    "daysBetweenCommits": 1.17, 
    // How many commits happened in the whole repo between these two?
    "commitsBetweenForRepo": 4, 
    // How many commits happened in the whole file between these two?
    "commitsBetweenForFile": 1, 
    // Full diff of the method:
    "diff": "@@ -1,16 +1,16 @@[GIT DIFF CODE],
    // In some cases, there are more details for the change 
    // (e.g. source file and target file for a method move operation
    "extendedDetails": {}
  }
}
```

### Building CodeShovel from the terminal

In the CodeShovel project directory, run `mvn install` and `mvn package`. The result will be a `.jar` file in the `target` directory
that can be run as described previously.

## Manual for developers

### Setting up CodeShovel in the IDE

Open the CodeShovel repo as Java project. Different run/debug configurations are possible:
* `com.felixgrund.codeshovel.execution.MainCli`
  * Use this if you want to run CodeShovel the same way as running the `.jar` file from the command line
  * Don't forget to configure the arguments in the configuration
  * Type of the run/debug configuration should be `Application`
* `com.felixgrund.codeshovel.MainSingleStub`
  * Run CodeShovel from a single stub file (see below)
  * Type of the run/debug configuration should be `Application`
* `com.felixgrund.codeshovel.MainDynamicStubTest`
  * Run CodeShovel with many test cases from multiple stub files (see below)
  * Type of the run/debug configuration should be `JUnit`
 
### Running CodeShovel with stub files

For development, it makes most to run CodeShovel from so-called *stub files*. These are located at 
`src/test/resources/stubs/LANG` where `LANG` refers to the programming language.

A stub file is a JSON file and contains data describing one method for which to run CodeShovel.
The structure is as follows:

```
{
  "repositoryName": "checkstyle",
  "filePath": "src/main/java/com/puppycrawl/tools/checkstyle/Checker.java", 
  "functionName": "fireErrors",
  "functionStartLine": 384,
  "startCommitName": "119fd4fb33bef9f5c66fc950396669af842c21a3",
  "expectedResult": { ... }
}
```

The keys mostly match the arguments for the command-line runtime semantically. Note that `startCommitName` should have
the actual commit hash rather than `HEAD`.

There is an extra key `expectedResults` that is used for a unit test run from the stub file (with the `MainDynamicStubTest` class).
It must contain the *exact* same object as is expected in the `changeHistoryShort` object in a result file. 

Example:

```
{
  (...),
  "expectedResult": {
    "b8ca6a585b824e91b3b8c72dd5cc53c0eb0ab0f1": "Ymultichange(Yparameterchange,Ybodychange)",
    "f1efb27670a93690577f1bae17fc9dcbd88a795d": "Yfilerename",
    "1d614c3a7ecf8a3ede4df8a50da46e71792d0025": "Yparameterchange",
    "e00c478dd61d9d883e41b500b780ab217582c2e7": "Ybodychange",
    "0e3fe5643667a53079dbd114e5b1e9aa91fde083": "Yintroduced"
  }
}
```

You can check the existing stub files in the `src/test/resources/stubs/LANG` directories for clarification.

When running CodeShovel from stub files, there are no program arguments required, 
but a few environment variables are required or optional:
* `REPO_DIR`: local repository path - path to a local directory that contains the repository.
  * (e.g. `/Users/myhome/dev/codeshovel-repos` where the repo dir `checkstyle` is inside this directory)
* `LANG`: programming language of the target method. Currently either `java` or `js`.
* `ENV_NAME` (optional): environment name - basically a name for the stub/s to be used.
  * Must refer to the file name of the stub without the `.json` ending
  * e.g. if you have a stub file `checkstyle-Checker-fireErrors.json`, the env name should be `checkstyle-Checker-fireErrors`
  * You can address multiple stub files by providing only the beginning of the file name
    * e.g. `checkstyle-` will match all stub files starting with `checkstyle-`
* `BEGIN_INDEX` (optional): If you only want to start at a specific index among all the matched stub files (starts with 1, not 0!).
* `MAX_RUNS` (optional): If you only want to run X stub files of the matched env names.
* `SKIP_ENVS` (optional): If you want to skip certain stub files among the matched stubs you can specify a comma-separated list of env names.
* `DISABLE_ALL_OUTPUTS` (optional): Set this to true if you don't want any files to be written. If this is set to true, all the `WRITE_*` flags below will be ignored!
* `WRITE_SEMANTIC_DIFFS` (optional): Set this to true if you want comparisons to `git-log` be performed and results written to files.
* `WRITE_RESULTS` (optional): Set this to true if you want result files  to be written (the ones described in section *The CodeShovel result file*).
* `WRITE_SIMILARITIES` (optional): Set this to true if similarity comparisons for methods should be logged to files. This should only be set for debugging cases.

## Contributing

We are using the master-develop-topic Git workflow in CodeShovel. If you want to contribute, please do as follows:

* Create an issue on Github
* Create a branch from `develop` with name `ISSUE_ID-what-is-this-branch-doing`
* Create a pull request onto `develop`
* Assign the project owner as reviewer


### Developing a language-specific version

All implementation logic that is specific to programming languages is implementaed in `parser/impl`. There are only two
classes that need to be created for one version:
- a `*Function` class that implements `Yfunction` and extends `AbstractFunction<E>`, where `E` is the class representing
one method node in the language-specific AST class in use
- a `*Parser` class that implements `Yparser` and extends `AbstractParser`

Lots of functionality is implemented in the `Abstract*` classes that is intended to work across different programming
languages. However, this is not guaranteed and anytime a new implementation is written the implementation of the methods
in `Yfunction` and `Yparser` in the respective `Abstract*` classes needs to be checked in regard to the new language.
As soon as methods need to perform differently, the versions in the `Abstract*` classes must be overriden. Please mark
these methods with the `@override` annotation in your implementing classes.

Constructors in the implementation classes must call the super constructor of the abstract class. Refer to the 
already implemented language-specific classes for examples.


### Testing a language-specific version

For each method that is used for testing, a stub should be created in `test/resources/stubs/LANGUAGE`. The stub file names
should be in the form `REPONAME-MODULENAME-METHODNAME.json` where `MODULENAME` is the name of the source file or class. If you
create stubs for multiple methods with the same name, please add `-LINENUMBER` (line number where the method name appears) 
to the file name.

One way of creating these stub files is as follows:
- Create the JSON file with all fields filled except `expectedResult`. Then, run CodeShovel with the stub as described above
in section `Running CodeShovel with stub files`
- Go through the resulting output and check if it is correct
  - IF it is correct, paste the result as `expectedResult` (as valid JSON) and make sure it passes when run as unit test 
  - ELSE, improve your implementation and repeat until it is correct
