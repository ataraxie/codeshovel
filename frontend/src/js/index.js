const $ = jQuery = require("jquery");
global.$ = global.jQuery = $;

const Diff2Html = require("diff2html").Diff2Html;
require("../../node_modules/diff2html/dist/diff2html-ui");

let SHOVEL = JSON.parse(EXAMPLE_JSON);
global.SHOVEL = SHOVEL;

let Handlebars = require('handlebars/runtime');

const shovelTemplates = {
	commitTable: require('../templates/commit-table.hbs')
};

let templates = {};
templates["side-by-side-file-diff"] = `
    <div id="{{fileHtmlId}}" class="d2h-file-wrapper" data-lang="{{file.language}}">
        <div class="d2h-file-header">
          {{{filePath}}}
        </div>
        <div class="d2h-files-diff">
            <div class="d2h-file-side-diff">
                <div class="d2h-code-wrapper">
                    <table class="d2h-diff-table">
                        <tbody class="d2h-diff-tbody">
                        {{{diffs.left}}}
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="d2h-file-side-diff">
                <div class="d2h-code-wrapper">
                    <table class="d2h-diff-table">
                        <tbody class="d2h-diff-tbody">
                        {{{diffs.right}}}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
`;


templates["generic-file-path"] = `
    <span class="d2h-file-name-wrapper">
        <span class="d2h-icon-wrapper">{{>fileIcon}}</span>
        <span class="d2h-file-name">{{fileDiffName}}</span>
        {{>fileTag}}
    </span>
`;

let history = SHOVEL.changeHistory;
let changes = [];
let columns = ["type", "commitMessage", "commitDate", "commitName", "commitAuthor", "commitDateOld", "commitNameOld", "commitAuthorOld", "daysBetweenCommits", "commitsBetweenForRepo", "commitsBetweenForFile"];

for (let commitName of history) {
	let change = SHOVEL.changeHistoryDetails[commitName];
	changes.push(change);
}

console.log(changes);

console.log(shovelTemplates.commitTable({
	columns: columns,
	changes: changes
}));

let ui = new Diff2HtmlUI({diff: "--- a/src/cmd/trace/goroutines.go\n" +
	"+++ b/src/cmd/trace/goroutines.go\n" +
	"@@ -1,8 +1,8 @@\n" +
	"     public static Pattern createPattern(String pattern, int flags) {\n" +
	"         try {\n" +
	"             return Pattern.compile(pattern, flags);\n" +
	"         }\n" +
	"         catch (final PatternSyntaxException ex) {\n" +
	"-            throw new ConversionException(\n" +
	"+            throw new IllegalArgumentException(\n" +
	"                 \"Failed to initialise regular expression \" + pattern, ex);\n" +
	"         }\n" +
	"}"});

ui.draw("#row-diff", {
	inputFormat: "diff",
	outputFormat: "side-by-side",
	showFiles: false,
	matching: "lines",
	rawTemplates: {
		"side-by-side-file-diff": templates["side-by-side-file-diff"],
		"generic-file-path": templates["generic-file-path"]
	}
});