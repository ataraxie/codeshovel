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
var repo = process.env.REPO_NAME;  // set to null to run against all repos


if (repo === "all") {
	fs.readdir(srcDir + "/codeshovel", function(err, list) {
		list.forEach(function(item) {
			execute(item);
		});
	});
} else {
	execute(repo);
}

function execute(repo) {

	console.log("execute(): " + repo);

	var dirCodeshovel = srcDir + "/codeshovel/" + repo;

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
	}


	filewalker(dirCodeshovel, function(error, results) {
		var resultArr = [];
		results.forEach(function(file) {
			try {
				var shovelObj = JSON.parse(fs.readFileSync(file));
				var methodKey = file.replace(/Users\/fgrund\/Documents\/CODESHOVEL\/output\/codeshovel\//g, '');
				var numCommitsSeen = shovelObj.numCommitsSeen;
				var timeTaken = shovelObj.timeTaken;
				resultArr.push(numCommitsSeen+'|||'+timeTaken+'|||'+methodKey);
			} catch (err) {
				console.log("ERROR processing file: " + file + "  --  " + err);
			}
		});

		var resultString = resultArr.join("\n");

		// var jsonResult = JSON.stringify(fullResult, null, 2);
		var filePath = dstDir + "/times-" + repo + ".csv";
		fs.writeFile(filePath, resultString, function(err) {
			if (err) {
				console.log(err);
			}

			console.log("Result for repo " + repo + " saved: " + filePath);
		});
	});
}
