## Code Shovel Development

#### Note: most users will not need to consider any of the below details and should refer to [README.md](README.md).

While the vast majority of users will use the Web Service UI, Web Service REST, or Command Line interfaces, if you want to build CodeShovel yourself (for instance if you are doing development), you can follow the instructions below.

### Prerequisites

* Java 8 or higher must be installed (`java -version` must reveal 1.8 or higher). You can find Java on the [Oracle page](https://www.oracle.com/technetwork/java/javase/downloads/index.html).
* If you want to build CodeShovel yourself: Maven 3.5.x or higher


### Building CodeShovel from the terminal

In the CodeShovel project directory, run `mvn install` and `mvn -DskipTests=true package`. The result will be a `.jar` file in the `target` directory
that can be run as described previously. 

### Setting up CodeShovel in the IDE

Open the CodeShovel repo as Java project. Different run/debug configurations are possible:
* `com.felixgrund.codeshovel.execution.MainCli`
  * Use this if you want to run CodeShovel the same way as running the `.jar` file from the command line
  * Don't forget to configure the arguments in the configuration
  * Type of the run/debug configuration should be `Application`
* `com.felixgrund.codeshovel.MainSingleOracle`
  * Run CodeShovel from a single oracle file (see below)
  * Type of the run/debug configuration should be `Application`
* `com.felixgrund.codeshovel.MainDynamicOracleTest`
  * Run CodeShovel with many test cases from multiple oracle files (see below)
  * Type of the run/debug configuration should be `JUnit`
 
### Running CodeShovel with oracle files

Oracle files can be validated in the IDE by running `MainDynamicOracleTest` or on the terminal using the parallelized test suite (this will be the fastest way) using `mvn test`.

For development, it makes most to run CodeShovel from so-called *oracle files*. These are located at 
`src/test/resources/oracles/LANG` where `LANG` refers to the programming language.

An oracle file is a JSON file and contains data describing one method for which to run CodeShovel.
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

There is an extra key `expectedResults` that is used for a unit test run from the oracle file (with the `MainDynamicOracle` class).
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

You can check the existing oracle files in the `src/test/resources/oracles/<LANG>` directories for clarification.

When running CodeShovel from oracle files, there are no program arguments required, 
but a few environment variables are required or optional:
* `REPO_DIR`: local repository path - path to a local directory that contains the repository.
  * (e.g. `/Users/myhome/dev/codeshovel-repos` where the repo dir `checkstyle` is inside this directory)
  * If you are working with the Java oracle, you can run `bin/clone-java-repositories.sh` to clone the Java oracle projects.
* `LANG`: programming language of the target method. Currently only java oracle files are in the repository.
  * These files should be in the `src/test/resources/oracles/<LANG>` folder.
* `ENV_NAMES` (optional): environment name - basically a name for the oracle/s to be used.
  * Must refer to the file name of the oracle without the `.json` ending
  * e.g. if you have a oracle file `checkstyle-Checker-fireErrors.json`, the env name should be `checkstyle-Checker-fireErrors`
  * You can address multiple oracle files by providing only the beginning of the file name
    * e.g. `checkstyle-` will match all oracle files starting with `checkstyle-`
* `SKIP_NAMES` (optional): If you want to skip certain oracle files among the matched oracles you can specify a comma-separated list of env names.
* `BEGIN_INDEX` (optional): If you only want to start at a specific index among all the matched oracle files (starts with 1, not 0!).
* `MAX_RUNS` (optional): If you only want to run X oracle files of the matched env names.
* `DISABLE_ALL_OUTPUTS` (optional): Set this to true if you don't want any files to be written. If this is set to true, all the `WRITE_*` flags below will be ignored!
* `WRITE_SEMANTIC_DIFFS` (optional): Set this to true if you want comparisons to `git-log` be performed and results written to files.
* `WRITE_RESULTS` (optional): Set this to true if you want result files  to be written (the ones described in section *The CodeShovel result file*).
* `WRITE_SIMILARITIES` (optional): Set this to true if similarity comparisons for methods should be logged to files. This should only be set for debugging cases.


### Contributing

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

For each method that is used for testing, a oracle should be created in `test/resources/oracles/LANGUAGE`. The oracle file names
should be in the form `REPONAME-MODULENAME-METHODNAME.json` where `MODULENAME` is the name of the source file or class. If you
create oracles for multiple methods with the same name, please add `-LINENUMBER` (line number where the method name appears) 
to the file name.

One way of creating these oracle files is as follows:
- Create the JSON file with all fields filled except `expectedResult`. Then, run CodeShovel with the oracle as described above
in section `Running CodeShovel with oracle files`
- Go through the resulting output and check if it is correct
  - IF it is correct, paste the result as `expectedResult` (as valid JSON) and make sure it passes when run as unit test 
  - ELSE, improve your implementation and repeat until it is correct
