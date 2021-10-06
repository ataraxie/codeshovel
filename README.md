# CodeShovel - Unearthing Method Histories

Take this shovel to dig in source code history for changes to specific methods and functions. The tool is currently implemented for software projects written in Java. CodeShovel is a tool for navigating method histories and is robust to the kinds of changes methods frequently undergo (e.g., being modified, renamed, or and moved between files and directories).

*This is research!* Primarily undertaken in the [Software Practices Lab](https://spl.cs.ubc.ca) at UBC in Vancouver, Canada we have developed this project to help practitioners to more efficiently check how their methods have changed and give researchers an easier way to track method evolution for academic studies. Please do not hesitate to get in touch if you have any questions!

### Publication

A conference paper describing how CodeShovel works and how it was evaluated as been accepted for publication at the International Conference on Software Engineering 2021. A free version of the paper can be found [here](https://www.cs.ubc.ca/~rtholmes/papers/icse_2021_grund.pdf). If you use CodeShovel in an academic paper, please cite:

> ***CodeShovel: Constructing Method-Level Source Code Histories.***
Felix Grund, Shaiful Alam Chowdhury, Nick Bradley, Braxton Hall, and Reid Holmes.
In Proceedings of the International Conference on Software Engineering (ICSE). 2021. 13 pages

```
@inproceedings{icse_2021_codeshovel,
  title = {{CodeShovel}: {C}onstructing Method-Level Source Code Histories},
  author = {Felix Grund and Shaiful Alam Chowdhury and Nick Bradley and Braxton Hall and Reid Holmes},
  booktitle = {Proceedings of the International Conference on Software Engineering (ICSE)},
  year = {2021}
}
```

## Pathways for Use

CodeShovel can be used in three ways:

* ***Web Service UI***: We have built a browser-based user interface that you can use to interactively navigate the history of a method of interest. We host a public copy of the [web interface](https://se.cs.ubc.ca/CodeShovel/index.html) if you just want to use CodeShovel without installing anything, but this repository also has [instructions for self-hosting](#web-service-ui) the web service on your own computer using Docker.

* ***Web Service REST:*** To programmatically investigate the history of a method, you can also call the CodeShovel web service using standard rest commands. You can direct these against our hosted version of the web service, or against your own self-hosted copy of the web service. The [REST interface instructions](#web-service-rest) are below.

* ***Command Line:*** Finally, if you would prefer to interact with CodeShovel on the command line directly without using the REST interface, you can call the CodeShovel `jar` directly. [Command line instructions](#command-line) are included below.

<a name="web-service-ui"></a>
## Web Service UI 

***Public UI:*** The web service UI enables easy interactive exploration of a method history. The quickest way to use this is through our hosted version available at [https://se.cs.ubc.ca/CodeShovel/index.html](https://se.cs.ubc.ca/CodeShovel/index.html). Through this interface you can explore histories of some sample methods (these are not cached: they are dynamically computed as the underlying repositories are updated), or by providing a link to a public repository of your choosing. **Note:** This is likely to be the least performant of all interface options as it runs on shared infrastructure with minimal resources, but is a viable way to check the results of the tool.

***Self-Hosted UI:*** You can also stand up a copy of the web interface on your own infrastructure. To do this, follow these steps:

1. Clone the repository: 
	* `git clone git@github.com:ataraxie/codeshovel.git`
2. Configure the environment:
	* Copy `.env.webservice.sample` to `.env`.
	* Update the log and cache paths in `.env`.
	* Specify a GitHub token; to generate one:
		* Visit: [https://github.com/settings/tokens](https://github.com/settings/tokens)
		* Tap 'Generate New Token'.
		* Grant the 'Repo' permission scope.
		* Copy the token generated to the `.env` file.
	* The default hostname does not need to be changed.

3. Start the web service:
	* `docker-compose build && docker-compose up`
	* Works with Docker 2.2+.
4. Access your server:
	* For the UI: open the web service in your browser (`https://localhost:8080`).
	* For the REST interface: see REST API instructions below.

<a name="web-service-rest"></a>
## Web Service REST 

As with the web service UI, you can either use our public web service or self-host your own (see the instructions [#web-service-ui](above)). If you are using the self-hosted web service, change `https://se.cs.ubc.ca/CodeShovel` to point to your own service.

Interacting with the CodeShovel web service is through the following REST endpoints. Examples are provided using `curl` syntax for ease of testing, just adapt the values as needed.

* `GET getHistory/`: Retrieves the history of a method
    * `gitUrl` (should end in `.git`)
    * `sha` (optional)
    * `filePath`
    * `startLine`
    * `methodName`

```
curl "https://se.cs.ubc.ca/CodeShovel/getHistory?gitUrl=${}&sha=${}&filePath=${}&startLine=${}&methodName=${}"
```

e.g.,

`curl "https://se.cs.ubc.ca/CodeShovel/getHistory?gitUrl=https://github.com/apache/commons-math.git&sha=$d71b8c93&filePath=/src/main/java/org/apache/commons/math4/dfp/DfpDec.java&methodName=round&startLine=164"`

Each entry in the history list conforms to the following schema:

```typescript
interface Change {
    type: string;
    commitMessage: string;
    commitDate: string;
    commitName: string; // SHA
    commitAuthor: string;
    commitDateOld?: string;
    commitNameOld?: string;
    commitAuthorOld?: string;
    daysBetweenCommits?: number;
    commitsBetweenForRepo?: number;
    commitsBetweenForFile?: number;
    diff: string;
    extendedDetails?: {[key: string]: any}
}
```

For convenience, we also provide endpoints for listing files and methods within the repository:

* `GET listFiles/`: Retrieves the list of files in a repo at a SHA
  * `gitUrl` should end in .git
  * `sha` (optional)

```
curl "https://se.cs.ubc.ca/CodeShovel/listFiles?gitUrl=${}&sha=${}"
```

e.g.,

`curl "https://se.cs.ubc.ca/CodeShovel/listFiles?gitUrl=https://github.com/apache/commons-math.git&sha=$d71b8c93"`

Every file in the repository is included in the returned list of strings. These can then be passed to `listMethods` to find the methods in any given file.

* `GET listMethods/`: Retrieves the list of methods for a file at a SHA
    * `gitUrl` should end in .git
    * `sha` (optional)
    * `filePath`

```
curl "https://se.cs.ubc.ca/CodeShovel/listMethods?gitUrl=${}&sha=${}&filePath=${}"
```

e.g., 

`curl "https://se.cs.ubc.ca/CodeShovel/listMethods?gitUrl=https://github.com/apache/commons-math.git&sha=$d71b8c93&filePath=/src/main/java/org/apache/commons/math4/dfp/DfpDec.java"`

Each entry in the returned list describes a method conforming to the following schema:

```typescript
interface Method {
    methodName: string; // Name of method
    longName: string; // Method prototype
    startLine: number;
    isStatic: boolean;
    isAbstract: boolean;
    visibility: "" | "public" | "private" | "protected";
}
```

<a name="command-line"></a>
## Command Line

In order to run from the command line CodeShovel for a local repository, you can clone the repo, build the tool, and then call it on the command line.

1. Clone the repo: 
  * `git clone git@github.com:ataraxie/codeshovel.git`
2. Switch to the appropriate branch: 
  * `cd codeshovel; git checkout research`
2. Build the code: 
  * `mvn -DskipTests=true package`
  * Works with mvn version 3.3+.
4. Call the code: 
  * `java -jar target/codeshovel-1.0.0-SNAPSHOT.jar OPTIONS` 
  * Works with Java version 8+.

`OPTIONS` are defined as follows:

```
 -filepath <arg>      Path to the file containing the method (required) 
 -methodname <arg>    Method name (required)
 -outfile <arg>       Output path (optional: defaults to current working directory)
 -reponame <arg>      Name of the repository (optional: defaults to the path name) 
 -repopath <arg>      Path to a local copy of the git repository (required)
 -startcommit <arg>   Hash of the commit to begin with backwards history traversal (optional: default is HEAD) 
 -startline <arg>     Start line of the method (required: differentiates between overloaded methods)

```

Minimal example (assumes [checkstyle](https://github.com/checkstyle/checkstyle) is checked out in `~/tmp/checkstyle/` and you are in `codeshovel/`):

```
java -jar target/codeshovel-0.3.1-SNAPSHOT.jar \
	-repopath ~/tmp/checkstyle \
	-filepath src/main/java/com/puppycrawl/tools/checkstyle/Checker.java \
	-methodname fireErrors \
	-startline 401 \ 
	-outfile results.json
```

<!--
Seems to duplicate content above in the REST API section.
## Output file format

If you are using the Web Service UI, the result will be rendered for you automatically. But if you are using the REST or Command Line interfaces results will be returned as JSON so you can process them according to you needs. Each run of CodeShovel will print result summaries to your console and will also produce a result file. Result files are in JSON and are structured as follows:

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
-->

## Code Shovel Development

While the vast majority of users will use the Web Service UI, Web Service REST, or Command Line interfaces, if you want to build CodeShovel yourself (for instance if you are doing development), you can follow the [Development instructions](doc/DEVELOPMENT.md).
