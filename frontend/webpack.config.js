module.exports = function(cmdArgs) {

	let envName = "default";

	const webpack = require("webpack");
	const ExtractTextPlugin = require("extract-text-webpack-plugin");
	const HtmlWebpackPlugin = require("html-webpack-plugin");
	const UglifyJsPlugin = require("uglifyjs-webpack-plugin");
	const CopyWebpackPlugin = require("copy-webpack-plugin");
	const CleanWebpackPlugin = require("clean-webpack-plugin");
	const Dotenv = require('dotenv');
	const DotenvWebpack = require('dotenv-webpack');
	const VersionFile = require('webpack-version-file');

    const path = require("path");
    const fs = require("fs");

	const SRC_DIR = path.resolve(__dirname, "src");
	const DIST_DIR = path.resolve(__dirname, "dist");

	const ENV_PATH = "env/" + envName + ".env";
	console.log("Using env file: " + ENV_PATH);
	const ENV = Dotenv.config({path: ENV_PATH}).parsed;

	const exampleJsonPath = path.resolve(__dirname, "data/example-codeshovel-output.json");
	const exampleJson = fs.readFileSync(exampleJsonPath, "utf8");

	function src(relativePath) {
		return SRC_DIR + "/" + relativePath;
	}

	// Formatted date for our build versioning file
	function getDate() {
		function pad(s) { return (s < 10) ? '0' + s : s; }
		let d = new Date();
		return [d.getFullYear(), pad(d.getMonth()+1), pad(d.getDate()), ].join('/');
	}

    // Webpack plugins to use for the build. These differ according to our target modes (dev|bootstrap|prod)
    function createPlugins() {
        const plugins = [
	        new CleanWebpackPlugin(DIST_DIR+"/**/*"), // cleans the /dist directory for each build
            new ExtractTextPlugin({ // responsible for the compiled css file
                filename: "bundle.css",
                allChunks: true
            }),
            new HtmlWebpackPlugin({ // creates the index.html file using the index.scaffold.hbs template
                title: "CodeShovel",
                favicon: src("img/favicon.ico"),
                template: src("templates/index.scaffold.hbs"),
                bodyClasses: '',
                bodyAttributes: '',
                inject: true
            }),
	        new DotenvWebpack({path: ENV_PATH}), // makes the env available in our JS sources
	        new VersionFile({ // creates the BUILD_INFO.txt version file for each build using the given template
		        output: './dist/BUILD_INFO.txt',
		        package: './package.json',
		        template: './version.template.ejs',
		        data: {
			        date: getDate(),
			        environment: envName
		        }
	        }),
	        new webpack.DefinePlugin({
		        XMPP_HOST: JSON.stringify("http://localhost"),
		        EXAMPLE_JSON: JSON.stringify(exampleJson)
	        })
        ];

        return plugins;
    }

    function createSources() {

        const sources = [
            src("scss/index.scss"),
            src("js/index.js"),
        ];

        return sources;
    }

    return {
        entry: createSources(),
        output: {
            filename: "bundle.js",
            path: path.resolve(__dirname, DIST_DIR)
        },
        module: {
            rules: [
                {
                    test: /\.js$/,
                    exclude: /node_modules/,
                    loader: "eslint-loader",
                    options: {

                    }
                },
                { // css / sass / scss loader for webpack
                    test: /\.(css|sass|scss)$/,
                    exclude: "/node_modules",
                    use: ExtractTextPlugin.extract({
                        use: [{ loader: "css-loader", options: { minimize: true }}, "sass-loader"],
                    })
                },
                {
                    test: /\.hbs/,
                    use: [
                        {
                            loader: "handlebars-loader",
                        }
                    ]
                },
                {
                    test: /\.jpe?g$|\.gif$|\.png|\.ico/i,
                    use: {
                        loader: "file-loader",
                        options: {
                            name: "[name].[ext]"
                        }
                    }
                }
            ]
        },
        plugins: createPlugins(),
        devtool: "source-map",
        devServer: {
            port: 3000,
            inline: false,
            progress: true,
            contentBase: DIST_DIR,
            outputPath: DIST_DIR
        }
    };
};