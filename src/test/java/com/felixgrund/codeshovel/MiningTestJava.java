package com.felixgrund.codeshovel;

public class MiningTestJava extends MiningTest {

	static {
		TARGET_FILE_EXTENSION = ".java";
		TARGET_FILE_PATH = "src/main/java/com/puppycrawl/tools/checkstyle/utils";
		CODESTORY_REPO_DIR = System.getenv("codeshovel.repo.dir");
		REPO = "checkstyle";
		START_COMMIT = "119fd4fb33bef9f5c66fc950396669af842c21a3";

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
