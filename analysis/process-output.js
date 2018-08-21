const fs = require('fs');
const path = require('path');


// /home/ncbradley/cs-output/diff_semantic_gitlog/checkstyle/119fd/src/it/java/com/google/checkstyle/test/base/AbstractIndentationTestSupport.java/isCommentConsistent___comment-String.json
const outputDir = "/Users/felix/CODESTORY/codeshovel/outputserver";
const repo = "commons-io";  // set to null to run against all repos
const commit = "559de"; // set to null to run against all commits

const dirCodeshovel = outputDir + "/codeshovel/" + repo;
const dirDiff = outputDir + "/diff_semantic_gitlog/" + repo;

https://jonlabelle.com/snippets/view/javascript/calculate-mean-median-mode-and-range-in-javascript
function median(values) {
    debugger;
    values.sort((a, b) => a - b);
	let lowMiddle = Math.floor((values.length - 1) / 2);
	let highMiddle = Math.ceil((values.length - 1) / 2);
	let median = (values[lowMiddle] + values[highMiddle]) / 2;
	return median;
}

/**
 * Explores recursively a directory and returns all the filepaths and folderpaths in the callback.
 * 
 * @see http://stackoverflow.com/a/5827895/4241030
 * @param {String} dir 
 * @param {Function} done 
 */
function filewalker(dir, done) {
    let results = [];

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

let fullResult = {};

filewalker(dirDiff, (error, results) => {
    if (error) {
        console.log(error);
        throw error;
    }

	// do something with files
    // how many "extra" commits did we find?
    let totalShovel = 0;
    let totalBase = 0;
    let totalOnlyShovel = 0;
    let totalOnlyBase = 0;
    let methodDetails = {};
    let shovelResultsArr = [];
    let baseResultsArr = [];

	fullResult.totalMethods = results.length;
    fullResult.totalHistoryEquals1 = 0;
    fullResult.totalHistory2To5 = 0;
	fullResult.totalHistory6To10 = 0;
	fullResult.totalHistoryGt10 = 0;
	fullResult.totalHistoryCount = {};

    for (const file of results) {
        try {
            const diffObj = JSON.parse(fs.readFileSync(file));
	        const methodId = file.split("/diff_semantic_gitlog/")[1].replace(".json", "");
	        let singleMethodResult = {};

	        let numShovel = diffObj.codeshovelHistory.length;
	        shovelResultsArr.push(numShovel);
	        totalShovel += numShovel;
	        singleMethodResult.sizeShovel = numShovel;
	        if (numShovel === 1) fullResult.totalHistoryEquals1 += 1;
	        else if (numShovel <= 5) fullResult.totalHistory2To5 += 1;
	        else if (numShovel <= 10) fullResult.totalHistory6To10 += 1;
	        else if (numShovel > 10) fullResult.totalHistoryGt10 += 1;

	        if (!fullResult.totalHistoryCount[numShovel]) {
		        fullResult.totalHistoryCount[numShovel] = 1;
            } else {
		        fullResult.totalHistoryCount[numShovel] += 1;
            }

	        let numBase = diffObj.baselineHistory.length;
	        totalBase += numBase;
	        singleMethodResult.sizeBase = numBase;
	        baseResultsArr.push(numBase);

            let numOnlyShovel = diffObj.onlyInCodeshovel.length;
            totalOnlyShovel += numOnlyShovel;
	        singleMethodResult.sizeOnlyShovel = numOnlyShovel;

            let numOnlyBase = diffObj.onlyInBaseline.length;
            totalOnlyBase += numBase;
	        singleMethodResult.sizeOnlyBase = numOnlyBase;

	        methodDetails[methodId] = singleMethodResult;


        } catch (err) {
            console.log(`ERROR processing ${file}. ${err}`);
        }
    }

    fullResult.avgSizeShovel = totalShovel / fullResult.totalMethods;
    fullResult.avgSizeBase = totalBase / fullResult.totalMethods;
    fullResult.medianSizeShovel = median(shovelResultsArr);
    fullResult.medianSizeBase = median(baseResultsArr);
    // fullResult.methodDetails = methodDetails;

    try {
	    collectShovel();
    } catch (err) {
        console.err("Could not collect shovel stats");
    }

});

function addStats(statsObj, changeType) {
    let doAdd = function(changeType) {
	    if (!statsObj[changeType]) {
		    statsObj[changeType] = 1;
	    } else {
		    statsObj[changeType] += 1;
	    }
    };

    if (changeType.startsWith("Ymultichange")) {
        let subchangesString = changeType.split("Ymultichange(")[1].replace(")", "");
        let subchangesArr = subchangesString.split(",");
        for (let subchange of subchangesArr) {
            doAdd(subchange);
        }
    } else {
        doAdd(changeType);
    }

}

function collectShovel() {
	filewalker(dirCodeshovel, (error, results) => {
		if (error) {
			console.log(error);
			throw error;
		}

		// do something with files
		// how many "extra" commits did we find?
		let changeStats = {};

		for (const file of results) {
			try {
				const shovelObj = JSON.parse(fs.readFileSync(file));
				const methodId = file.split(outputDir + "/codeshovel/")[1].replace(".json", "");
				for (let commitName in shovelObj.changeHistoryShort) {
					let changeType = shovelObj.changeHistoryShort[commitName];
                    addStats(changeStats, changeType);
				}


			} catch (err) {
				console.log(`ERROR processing ${file}. ${err}`);
			}
		}

		fullResult.changeStats = changeStats;

        console.log(fullResult);
	});
}





//http://2ality.com/2015/01/es6-set-operations.html
function union(a, b) {
    return new Set([...a, ...b]);
}

function intersection(a, b) {
    return new Set([...a].filter(x => b.has(x)));
}

function diff(a, b) {
    return new Set([...a].filter(x => !b.has(x)));
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




/**
    One question: how many commits are "before" versus "ever"

 
 */