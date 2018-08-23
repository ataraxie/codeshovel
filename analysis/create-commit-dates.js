var fs = require('fs');
var path = require('path');

var repoDir = process.env.REPO_DIR;

var execSync = require('child_process').execSync;

var fullResult = {};

fs.readdir(repoDir, function(err, list) {
	list.forEach(function(dirName) {
		var fullPath = repoDir + "/" + dirName;
		if (fs.lstatSync(fullPath).isDirectory()) {
			execute(dirName);
		}
	});
});

function runGitLogCommand(cmd, workDir) {
	return execSync(cmd, { cwd: workDir });
}

function getLogCommand(commitName, beginLine, endLine, filePath) {
	var cmd = "git log | grep 'commit\\s' | sed 's/commit//'";
	cmd += "";
	cmd += "";
	// git log --no-merges -L 91,93:src/main/java/org/apache/commons/io/input/XmlStreamReaderException.java  | grep 'commit\s' | sed 's/commit//'
	return cmd;
}

function execute(repo) {
	fullResult[repo] = {};

	var res = fullResult[repo];
	var cmd = "git log --date=raw";
	var out = execSync(cmd, { cwd: repoDir + "/" + repo });

	console.log(out.toString());
}