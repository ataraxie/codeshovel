var fs = require('fs');
var path = require('path');

var srcDir = process.env.SRC_DIR;
var dstDir = process.env.DST_DIR;
var repo = process.env.REPO_NAME;  // set to null to run against all repos

var repoDir = process.env.REPO_DIR;

var execSync = require('child_process').execSync;

if (repo === "all") {
	fs.readdir(srcDir + "/codeshovel", function(err, list) {
		list.forEach(function(item) {
			execute(item);
		});
	});
} else {
	execute(repo);
}

var logCommands = [];

function runGitLogCommand(cmd, workDir) {
	return execSync(cmd, { cwd: workDir });
}

function getLogCommand(commitName, beginLine, endLine, filePath) {
	var cmd = "git log --no-merges " + commitName + " -L " + beginLine + "," + endLine + ":" + filePath;
	cmd += " | grep 'commit\\s'";
	cmd += " | sed 's/commit//'";
	// git log --no-merges -L 91,93:src/main/java/org/apache/commons/io/input/XmlStreamReaderException.java  | grep 'commit\s' | sed 's/commit//'
	return cmd;
}

function ensureDirectoryExistence(filePath) {
	var dirname = path.dirname(filePath);
	if (fs.existsSync(dirname)) {
		return true;
	}
	ensureDirectoryExistence(dirname);
	fs.mkdirSync(dirname);
}

function execute(repo) {

	var thisRepoDir = repoDir + "/" + repo;

	console.log("execute(): " + repo);

	var dirCodeshovel = srcDir + "/codeshovel/" + repo;
	var dirDiff = srcDir + "/diff_semantic_gitlog/" + repo;

	/**
	 * Explores recursively a directory and returns all the filepaths and folderpaths in the callback.
	 *
	 * @see http://stackoverflow.com/a/5827895/4241030
	 * @param {String} dir
	 * @param {Function} done
	 */
	function filewalker(dir, done) {
		var results = [];

		fs.readdir(dir, function(err, list) {
			if (err) return done(err);

			var pending = list.length;

			if (!pending) return done(null, results);

			list.forEach(function(file){
				file = path.resolve(dir, file);

				fs.stat(file, function(err, stat){
					// If directory, execute a recursive call
					if (stat && stat.isDirectory()) {
						// Add directory to array [comment if you need to remove the directories from the array]
						// results.push(file);

						filewalker(file, function(err, res){
							results = results.concat(res);
							if (!--pending) done(null, results);
						});
					} else {
						results.push(file);

						if (!--pending) done(null, results);
					}
				});
			});
		});
	};

	var shovelResults = {};

	filewalker(dirCodeshovel, function(error, files) {
		if (error) {
			console.log(error);
			throw error;
		}

		files.forEach(function(file) {
			var relativePath = file.split("/outputserver/codeshovel/")[1];
			shovelResults[relativePath] = JSON.parse(fs.readFileSync(file));
		});

		collectSemanticDiffs();
	});

	function collectSemanticDiffs() {
		filewalker(dirDiff, function(error, files) {
			if (error) {
				console.log(error);
				throw error;
			}

			var count = 0;
			var numLength0 = 0;
			var filesTotal = files.length;
			console.log("Total files: " + filesTotal);
			files.forEach(function(file) {
				count += 1;
				console.log("Iterating file " + count + " / " + filesTotal);

				var diffObj = JSON.parse(fs.readFileSync(file));
				if (diffObj.baselineHistory.length === 0) {
					numLength0 += 1;
					var relativePath = file.split("/diff_semantic_gitlog/")[1];
					var newBaseline;
					var shovelObj = shovelResults[relativePath];
					var logCommand = getLogCommand(shovelObj.startCommitName, shovelObj.functionStartLine,
						shovelObj.functionEndLine, shovelObj.sourceFilePath);
					try {
						var result = runGitLogCommand(logCommand, thisRepoDir);
						if (result) {
							var resultString = result.toString();
							if (resultString) {
								resultString = resultString.trim();
								var newBaseline = resultString.split("\n");
								diffObj.updatedBaselineHistory = newBaseline;
								var json = JSON.stringify(diffObj);
								try {
									fs.writeFileSync(file, json);
									console.log("Result for repo " + repo + " saved: " + file);
								} catch (err) {
									console.error("Could not write updated output file");
									console.error(err);
								}
							}
						}
					} catch (err) {
						console.error(err);
						console.log(logCommand);
						console.log(relativePath);
					}

				}
			});
		});
	}

}



// {
//     "origin": "codeshovel",
//     "repositoryName": "guava",
//     "repositoryPath": "/home/ncbradley/codeshovel-repos/guava/.git",
//     "startCommitName": "34c1616279efe08089960aafd3df486cf8a04820",
//     "sourceFileName": "Absent.java",
//     "functionName": "get",
//     "functionId": "get",
//     "sourceFilePath": "android/guava/src/com/google/common/base/Absent.java",
//     "functionStartLine": 42,
//     "functionEndLine": 44,
//     "changeHistory": [
//       "9b94fb3965c6869b0ac47420958a4bbae0b2d54c"
//     ],
//     "changeHistoryShort": {
//       "9b94fb3965c6869b0ac47420958a4bbae0b2d54c": "Yintroduced"
//     },
//     "changeHistoryDetails": {
//       "9b94fb3965c6869b0ac47420958a4bbae0b2d54c": {
//         "type": "Yintroduced",
//         "commitMessage": "Begin exporting sources of Guava for Android.\n\npom.xml files to come.\n\n-------------\nCreated by MOE: https://github.com/google/moe\nMOE_MIGRATED_REVID\u003d153757913\n",
//         "commitDate": 1492741671000,
//         "commitName": "9b94fb3965c6869b0ac47420958a4bbae0b2d54c",
//         "commitAuthor": "zhenghua"
//       }
//     }
//   }
