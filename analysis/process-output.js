const fs = require('fs');
const path = require('path');


// /home/ncbradley/cs-output/diff_semantic_gitlog/checkstyle/119fd/src/it/java/com/google/checkstyle/test/base/AbstractIndentationTestSupport.java/isCommentConsistent___comment-String.json
const outputDir = "/home/ncbradley/cs-output/diff_semantic_gitlog";
const repo = "checkstyle";  // set to null to run against all repos
const commit = ""; // set to null to run against all commits

let baseDir = outputDir;

if (repo) {
    if (commit) {
        baseDir += "/" + repo + "/" + commit;
    }
    else {
        baseDir += "/" + repo
    }
}



// function walker(dir) {
//     const results = [];
//     return new Promise((resolve, reject) => {
//         fs.readdir(dir, (err, files) => {
//             if (err) {
//                 reject(err);
//             }

//             const pending = files.length;

//             if (!pending) {
//                 return resolve(results);
//             }

//             const promises = [];
//             for (let file of files) {
//                 file = path.resolve(dir, file);
//                 fs.stat(file, function(err, stats) {
//                     if (stats && stats.isDirectory()) {
//                         promises.push(walker(file));
//                     } else {
//                         results.push(file);
//                     }
//                 });
//             }
//             promises.then(r => resolve(results.concat(r))).catch(err => reject(err));
//         });
//     });
// }


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

filewalker(baseDir, (error, results) => {
    if (error) {
        console.log(error);
        throw error;
    }

    // do something with files
    // how many "extra" commits did we find?
    let totalOnlyCodeShovel = 0;
    let totalOnlyBaseLine = 0;
    let totalIntersection = 0;
    let totalCommits = 0;
    for (const file of results) {
        try {
            const semanticMethodDiff = JSON.parse(fs.readFileSync(file));
            const cs = new Set(semanticMethodDiff.codeshovelHistory);
            const bl = new Set(semanticMethodDiff.baselineHistory);

            totalCommits += union(cs, bl).size;
            totalOnlyCodeShovel += semanticMethodDiff.onlyInCodeshovel.length;
            totalOnlyBaseLine += semanticMethodDiff.onlyInBaseline.length;
            totalIntersection += intersection(cs, bl).size;
        } catch (err) {
            console.log(`ERROR processing ${file}. ${err}`);
        }
    }
    console.log(`Both: ${totalIntersection}, Baseline: ${totalOnlyBaseLine}, Codeshovel: ${totalOnlyCodeShovel}; Total: ${totalCommits}`);


    // const methodCommitCount = [];
    // for (const file of results) {
    //     try {
    //         const method = JSON.parse(fs.readFileSync(file));
    //         const numCommits = method.changeHistory.length;
    //         if (numCommits > 1) {
    //             methodCommitCount.push(numCommits);
    //         }
    //     } catch (err) {
    //         console.log(`ERROR processing ${file}.`);
    //     }
    // }
    // fs.writeFileSync("methodCommitCount.csv", methodCommitCount.join(",\n"));
    // console.log("Number methods analyzed:", methodCommitCount.length);
});

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