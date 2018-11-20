# CodeShovel - Unearthing Method Histories

Take this shovel to dig in source code history for changes to specific methods and functions. Currently implemented for Java and JavaScript.

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

Minimal example:
```
java -jar codeshovel-0.2.0.jar -repopath ~/checkstyle -filepath src/main/java/com/puppycrawl/tools/checkstyle/Checker.java -methodname fireErrors -startline 384
```

### The CodeShovel result file

Each run of CodeShovel will print result summaries to your console and will also produce a result file. Result files
are in JSON and are structured as follows:
```
{
  "origin": "codeshovel", // Origin of the request. In our case this will always be "codeshovel".
  "repositoryName": "checkstyle", // Name of the repository
  "repositoryPath": "~/dev/codeshovel/repos/checkstyle/.git", // Full path to the repository
  "startCommitName": "119fd4fb33bef9f5c66fc950396669af842c21a3", // Start commit hash
  "sourceFileName": "Checker.java", // File name of the source file
  "functionName": "fireErrors", // Name of the method/function in play
  "functionId": "fireErrors___fileName-String__errors-SortedSet__LocalizedMessage__", // ID for the method/function in play
  "sourceFilePath": "src/main/java/com/puppycrawl/tools/checkstyle/Checker.java", // Path of the source file containing the method in play
  "functionStartLine": 384, // Start line of the method/function
  "functionEndLine": 399, // End line of the method/function
  "changeHistory": [ ... ], // List of commit hashes that changed the function/method
  "changeHistoryShort": { }, // Short description with the type of change for each commit that changed the function/method
  "changeHistoryDetails": { } // Detailed report for each commit that changed the method
}
```

The `changeHistoryDetails` object is structured as follows:
```
{
  "COMMIT_HASH": { // The keys in the object are the hashes of the commits that changed the method/function
    "type": "Ybodychange", // Type of the change
    "commitMessage": "Issue #3254: UT to verify all property types and values in XDocs", // Commit message
    "commitDate": 1515029424000, // Commit date
    "commitName": "327c0bc843612486ab4ded32a2f01038e1271fd0", // Commit hash
    "commitAuthor": "rnveach", // Commit author name
    "commitDateOld": 1514928265000, // Commit date of the parent/previous commit
    "commitNameOld": "dabb75d43c7e02317565dde4c5e60f380d3b16b8", // Commit name of the parent/previous commit
    "commitAuthorOld": "Roman Ivanov", // Author of the parent/previous commit
    "daysBetweenCommits": 1.17, // Number of days between this commit and the parent/previous commit
    "commitsBetweenForRepo": 4, // How many commits happened in the whole repo between these two?
    "commitsBetweenForFile": 1, // How many commits happened in the whole file between these two?
    // Full diff of the method:
    "diff": "@@ -1,16 +1,16 @@\n     public void fireErrors(String fileName, SortedSet\u003cLocalizedMessage\u003e errors) {\n         final String stripped \u003d CommonUtils.relativizeAndNormalizePath(basedir, fileName);\n         boolean hasNonFilteredViolations \u003d false;\n         for (final LocalizedMessage element : errors) {\n             final AuditEvent event \u003d new AuditEvent(this, stripped, element);\n             if (filters.accept(event)) {\n                 hasNonFilteredViolations \u003d true;\n                 for (final AuditListener listener : listeners) {\n                     listener.addError(event);\n                 }\n             }\n         }\n-        if (hasNonFilteredViolations \u0026\u0026 cache !\u003d null) {\n-            cache.remove(fileName);\n+        if (hasNonFilteredViolations \u0026\u0026 cacheFile !\u003d null) {\n+            cacheFile.remove(fileName);\n         }\n     }\n\\ No newline at end of file\n",
    "extendedDetails": {} // In some cases, there are more details for the change (e.g. source file and target file for a method move operation
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
* `ENV_NAME`: environment name - basically a name for the stub/s to be used.
  * Must refer to the file name of the stub without the `.json` ending
  * e.g. if you have a stub file `checkstyle-Checker-fireErrors.json`, the env name should be `checkstyle-Checker-fireErrors`
  * You can address multiple stub files by providing only the beginning of the file name
    * e.g. `checkstyle-` will match all stub files starting with `checkstyle-`
* `LANG`: programming language of the target method. Currently either `java` or `js`.
* `BEGIN_INDEX` (optional): If you only want to start at a specific index among all the matched stub files (starts with 1, not 0!).
* `MAX_RUNS` (optional): If you only want to run X stub files of the matched env names.
* `SKIP_ENVS` (optional): If you want to skip certain stub files among the matched stubs you can specify a comma-separated list of env names.
* `DISABLE_ALL_OUTPUTS` (optional): Set this to true if you don't want any files to be written. If this is set to true, all the `WRITE_*` flags below will be ignored!
* `WRITE_SEMANTIC_DIFFS` (optional): Set this to true if you want comparisons to `git-log` be performed and results written to files.
* `WRITE_RESULTS` (optional): Set this to true if you want result files  to be written (the ones described in section *The CodeShovel result file*).
* `WRITE_SIMILARITIES` (optional): Set this to true if similarity comparisons for methods should be logged to files. This should only be set for debugging cases.

## Contributing

tbd.



