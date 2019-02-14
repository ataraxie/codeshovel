if (!String.prototype.startsWith) {
	String.prototype.startsWith = function(searchString, position) {
		position = position || 0;
		return this.indexOf(searchString, position) === position;
	};
}

var fs = require('fs');
var path = require('path');


var srcDir = process.env.SRC_DIR;
var dstDir = process.env.DST_DIR;
var commitTimesFilePath = process.env.COMMIT_TIMES_PATH;
var repo = process.env.REPO_NAME;  // set to null to run against all repos

var commitTimes = JSON.parse(fs.readFileSync(commitTimesFilePath));

if (repo === "all") {
	fs.readdir(srcDir + "/codeshovel", function(err, list) {
		list.forEach(function(item) {
			execute(item);
		});
	});
} else {
	execute(repo);
}

function sortResult(fullResult) {
	var newResult = {};
	Object.keys(fullResult).forEach(function(key) {
		var value = fullResult[key];
		if (typeof value !== 'object') {
			newResult[key] = value;
		}
	});
	["changeStats", "totalHistoryCount", "totalHistoryCountBase", "statsMethodsOneChange",
		"methodSizeStatsLeftNumChangesRightNumLinesNumMethods" ].forEach(function(key) {
		newResult[key] = fullResult[key];
	});
	return newResult;
}

function execute(repo) {

	console.log("execute(): " + repo);

	var dirCodeshovel = srcDir + "/codeshovel/" + repo;

	var median = function(values) {
		values.sort(function(a, b) {
			return a - b;
		});
		var lowMiddle = Math.floor((values.length - 1) / 2);
		var highMiddle = Math.ceil((values.length - 1) / 2);
		var median = (values[lowMiddle] + values[highMiddle]) / 2;
		return median;
	};

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

	var fullResult = { repo: repo };
	var commitTimesRepo = commitTimes[repo];

	var totalHistoryCount = {};

	var getDuration = function(commitArr) {
		var newestCommit = commitArr[0];
		var oldestCommit = commitArr[commitArr.length - 1];
		var timestampNewest = commitTimesRepo[newestCommit];
		var timestampOldest = commitTimesRepo[oldestCommit];
		return timestampNewest - timestampOldest;
	};

	filewalker(dirCodeshovel, function(error, results) {
		if (error) {
			console.log(error);
			throw error;
		}

		// do something with files
		// how many "extra" commits did we find?
		var changeStats = {};
		var methodSizeStats = {};
		var statsMethodsOneChange = {
			setters: 0,
			getters: 0,
			tests: 0
		};
		var countSmallMethodsForOneChange = 0;
		var countSmallMethodsForMoreThanOneChange = 0;

		var sumLineLengthOneChange = 0;
		var sumLineLengthMoreThanOneChange = 0;
		var totalCommits = 0;

		var numMethodsWithCrossFileChanges = 0;
		var totalMethods = results.length;
		var shovelResultsArr = [];

		var numShovelHistoryGt1 = 0;
		var methodLifetime = 0;

		var totalTimeTaken = 0;
		var totalCommitsSeen = 0;

		var numCommitsSeenArr = [];
		var timeTakenArr = [];

		fullResult.totalMethods = totalMethods;
		fullResult.failedMethods = 0;
		fullResult.totalHistoryEquals1 = 0;
		fullResult.totalHistory2To5 = 0;
		fullResult.totalHistory6To10 = 0;
		fullResult.totalHistoryGt10 = 0;

		console.log("Iterating codeshovel results for repo: " + repo);

		results.forEach(function(file) {
			try {
				var shovelObj = JSON.parse(fs.readFileSync(file));
				var methodName = shovelObj.functionName;

				var numMethodCommits = shovelObj.changeHistory.length;
				if (!methodSizeStats[numMethodCommits]) {
					methodSizeStats[numMethodCommits] = {};
				}

				var numMethodLines = 1 + (shovelObj.functionEndLine - shovelObj.functionStartLine);
				if (!methodSizeStats[numMethodCommits][numMethodLines]) {
					methodSizeStats[numMethodCommits][numMethodLines] = 1;
				} else {
					methodSizeStats[numMethodCommits][numMethodLines] += 1;
				}

				shovelResultsArr.push(numMethodCommits);
				totalCommitsSeen += shovelObj.numCommitsSeen;
				totalTimeTaken += shovelObj.timeTaken;
				totalCommits += numMethodCommits;
				timeTakenArr.push(shovelObj.timeTaken);
				numCommitsSeenArr.push(shovelObj.numCommitsSeen);

				if (numMethodCommits === 1) {
					fullResult.totalHistoryEquals1 += 1;
				}
				else if (numMethodCommits <= 5) fullResult.totalHistory2To5 += 1;
				else if (numMethodCommits <= 10) fullResult.totalHistory6To10 += 1;
				else if (numMethodCommits > 10) fullResult.totalHistoryGt10 += 1;

				if (!totalHistoryCount[numMethodCommits]) {
					totalHistoryCount[numMethodCommits] = 1;
				} else {
					totalHistoryCount[numMethodCommits] += 1;
				}

				if (numMethodLines <= 3) {
					if (numMethodCommits <= 1) {
						countSmallMethodsForOneChange += 1;
					} else {
						countSmallMethodsForMoreThanOneChange += 1;
					}
				}

				if (numMethodCommits > 1) {
					numShovelHistoryGt1 += 1;
					methodLifetime += getDuration(shovelObj.changeHistory);

					var oldestCommitName = shovelObj.changeHistory[shovelObj.changeHistory.length-1];
					var oldestCommitChangeType = shovelObj.changeHistoryShort[oldestCommitName];

					if (oldestCommitChangeType !== "Yintroduced") {
						fullResult.failedMethods += 1;
					}
				}

				if (numMethodCommits === 1) {
					sumLineLengthOneChange += numMethodLines;
					if (numMethodLines <= 3) {
						if (methodName.startsWith("get") || methodName.startsWith("is")) {
							statsMethodsOneChange.getters += 1;
						} else if (methodName.startsWith("set")) {
							statsMethodsOneChange.setters += 1;
						}
					}
					if (methodName.startsWith("test")) {
						statsMethodsOneChange.tests += 1;
					}
				} else {
					sumLineLengthMoreThanOneChange += numMethodLines;
				}

				var methodChangeTypes = {};
				for (var commitName in shovelObj.changeHistoryShort) {
					var changeType = shovelObj.changeHistoryShort[commitName];
					addStats(changeStats, changeType);
					addStats(methodChangeTypes, changeType);
				}

				var changeTypesForMethod = Object.keys(methodChangeTypes);
				if (changeTypesForMethod.indexOf("Yfilerename") >= 0 || changeTypesForMethod.indexOf("Ymovefromfile") >= 0) {
					numMethodsWithCrossFileChanges += 1;
				}
			} catch (err) {
				console.log("ERROR processing file: " + file + "  --  " + err);
			}
		});

		fullResult.totalChanges = totalCommits;
		fullResult.avgMethodLifetimeInDays = (methodLifetime / numShovelHistoryGt1) / 86400000;
		fullResult.avgSize = totalCommits / fullResult.totalMethods;
		fullResult.medianSize = median(shovelResultsArr);
		fullResult.totalHistoryCount = totalHistoryCount;

		fullResult.totalTimeTaken = totalTimeTaken;
		fullResult.totalCommitsSeen = totalCommitsSeen;
		fullResult.avgTimeTaken = totalTimeTaken / totalMethods;
		fullResult.avgTotalCommitsSeen = totalCommitsSeen / totalMethods;
		fullResult.medianTimeTaken = median(timeTakenArr);
		fullResult.medianNumCommitsSeen = median(numCommitsSeenArr);

		fullResult.changeStats = changeStats;
		fullResult.countSmallMethodsForOneChange = countSmallMethodsForOneChange;
		fullResult.countSmallMethodsForMoreThanOneChange = countSmallMethodsForMoreThanOneChange;
		fullResult.numMethodsWithCrossFileChanges = numMethodsWithCrossFileChanges;
		fullResult.fragmentMethodsWithCrossFileChanges = numMethodsWithCrossFileChanges / totalMethods;

		var numMethodsOneChange = fullResult.totalHistoryCount["1"];
		var numMethodsMoreThanOneChange = fullResult.totalMethods - numMethodsOneChange;
		fullResult.avgMethodSizeOneChange = sumLineLengthOneChange / numMethodsOneChange;
		fullResult.avgMethodsMoreThanOneChange = sumLineLengthMoreThanOneChange / numMethodsMoreThanOneChange;

		fullResult.statsMethodsOneChange = statsMethodsOneChange;
		fullResult.methodSizeStatsLeftNumChangesRightNumLinesNumMethods = methodSizeStats;

		fullResult = sortResult(fullResult);

		var jsonResult = JSON.stringify(fullResult, null, 2);
		var filePath = dstDir + "/stats-" + repo + ".json";
		fs.writeFile(filePath, jsonResult, function(err) {
			if (err) {
				console.log(err);
			}

			console.log("Result for repo " + repo + " saved: " + filePath);
		});
	});

	// filewalker(dirDiff, function(error, results) {
		// if (error) {
		// 	console.log(error);
		// 	throw error;
		// }
		//
		// // do something with files
		// // how many "extra" commits did we find?
		// var totalShovel = 0;
		// var numShovelHistoryGt1 = 0;
		// var totalCommitDuration = 0;
		// var methodDetails = {};
		// var shovelResultsArr = [];
		// var totalMethods = results.length;
		//
		// fullResult.totalMethods = totalMethods;
		// fullResult.totalHistoryEquals1 = 0;
		// fullResult.totalHistory2To5 = 0;
		// fullResult.totalHistory6To10 = 0;
		// fullResult.totalHistoryGt10 = 0;
		//
		// console.log("Iterating semantic diff files for repo: " + repo);

		// results.forEach(function(file) {
		// 	try {
		// 		var resultObj = JSON.parse(fs.readFileSync(file));
		// 		var methodId = file.split("/codeshovel/")[1].replace(".json", "");
		// 		var singleMethodResult = {};
		//
		// 		var numHistoryCommits = resultObj.codeshovelHistory.length;
		// 		shovelResultsArr.push(numHistoryCommits);
		// 		totalShovel += numHistoryCommits;
		// 		singleMethodResult.sizeShovel = numHistoryCommits;
		// 		if (numHistoryCommits === 1) {
		// 			fullResult.totalHistoryEquals1 += 1;
		// 		}
		// 		else if (numHistoryCommits <= 5) fullResult.totalHistory2To5 += 1;
		// 		else if (numHistoryCommits <= 10) fullResult.totalHistory6To10 += 1;
		// 		else if (numHistoryCommits > 10) fullResult.totalHistoryGt10 += 1;
		//
		// 		if (!totalHistoryCount[numHistoryCommits]) {
		// 			totalHistoryCount[numHistoryCommits] = 1;
		// 		} else {
		// 			totalHistoryCount[numHistoryCommits] += 1;
		// 		}
		//
		// 		methodDetails[methodId] = singleMethodResult;
		//
		// 		if (numHistoryCommits > 1) {
		// 			numShovelHistoryGt1 += 1;
		// 			totalCommitDuration += getDuration(resultObj.codeshovelHistory);
		// 		}
		// 		if (numBase > 1) {
		// 			numBaselineHistoryGt1 += 1;
		// 			totalCommitDurationBase += getDuration(resultObj.baselineHistory);
		// 		}
		//
		// 		if (numBase > 1 && numHistoryCommits > 1) {
		// 			var baselineInShovelRange = getBaselineInShovelRange(resultObj, commitTimesRepo);
		// 			if (baselineInShovelRange.length > 1) {
		// 				numBaselineHistoryGt1InShovelRange += 1;
		// 				totalCommitDurationBaseInShovelRange += getDuration(baselineInShovelRange);
		// 				totalCommitDurationShovelBaseInShov += getDuration(resultObj.codeshovelHistory);
		// 			}
		// 		}
		//
		// 	} catch (err) {
		// 		console.log("ERROR processing file: " + file + " -- " + err);
		// 	}
		// });

		// fullResult.totalChanges = totalShovel;
		// fullResult.avgMethodLifetimeInDays = (totalCommitDuration / numShovelHistoryGt1) / 86400000;
		// fullResult.avgSize = totalShovel / fullResult.totalMethods;
		// fullResult.medianSize = median(shovelResultsArr);
		// fullResult.totalHistoryCount = totalHistoryCount;
		//
		// try {
		// 	collectShovel();
		// } catch (err) {
		// 	console.err("Could not collect shovel stats");
		// }

	// });

	function addStats(statsObj, changeType) {
		var doAdd = function(changeType) {
			if (!statsObj[changeType]) {
				statsObj[changeType] = 1;
			} else {
				statsObj[changeType] += 1;
			}
		};

		if (changeType.startsWith("Ymultichange")) {
			var subchangesString = changeType.split("Ymultichange(")[1].replace(")", "");
			var subchangesArr = subchangesString.split(",");
			subchangesArr.forEach(function(subchange) {
				doAdd(subchange);
			});
		} else {
			doAdd(changeType);
		}

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
