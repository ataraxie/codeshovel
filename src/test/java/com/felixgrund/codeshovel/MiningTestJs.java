package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.wrappers.GlobalEnv;

public class MiningTestJs extends MiningTest {

	private static final boolean METHOD_MODE = false;

	static {
		TARGET_FILE_EXTENSION = ".js";
		TARGET_FILE_PATH = "src/css.js";
		CODESTORY_REPO_DIR = GlobalEnv.REPO_DIR;
		REPO = "jquery";
		START_COMMIT = "45f085882597016e521436f01a8459daf3e4000e";

		// Specify a method name if you only want to consider methods with that name.
		// If you want to run it on all methods (which will be most cases for mining), use `null`.
		TARGET_METHOD = null;

		// Specify a method line number if you want to use only methods with name TARGET_METHOD *and* TARGET_METHOD_STARTLINE.
		// If the start line doesn't matter (which will be most cases for mining), use `0`.
		TARGET_METHOD_STARTLINE = 0;
	}

	public static void main(String[] args) throws Exception {
		execMining();
	}

}
