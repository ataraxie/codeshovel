package com.felixgrund.codeshovel;

import com.felixgrund.codeshovel.wrappers.GlobalEnv;

public class MiningTestJava extends MiningTest {

// Sample run
	static {
//		TARGET_FILE_EXTENSION = ".java";
//		CODESTORY_REPO_DIR = GlobalEnv.REPO_DIR;

//		TARGET_FILE_PATH = "flink-core/src/main/java/org/apache/flink/api/java/typeutils/runtime/PojoSerializer.java";
//		TARGET_FILE_PATH = "src/main/java/com/puppycrawl/tools/checkstyle/TreeWalker.java";


		REPO = "checkstyle";
//		REPO = "flink";
//		START_COMMIT = "9e936a5f8198b0059e9b5fba33163c2bbe3efbdd";
//		START_COMMIT = "119fd4fb33bef9f5c66fc950396669af842c21a3";


		// Specify a method name if you only want to consider methods with that name.
		// If you want to run it on all methods (which will be most cases for mining), use `null`.

//		TARGET_METHOD = "processFiltered";
//		TARGET_METHOD = "deserialize";

		// Specify a method line number if you want to use only methods with name TARGET_METHOD *and* TARGET_METHOD_STARTLINE.
		// If the start line doesn't matter (which will be most cases for mining), use `0`.
//		TARGET_METHOD_STARTLINE = 369;
	}


	static {
		TARGET_FILE_EXTENSION = ".java";
		TARGET_FILE_PATH = GlobalEnv.TARGET_FILE_PATH;
		CODESTORY_REPO_DIR = GlobalEnv.REPO_DIR;
		REPO = GlobalEnv.REPO;
		START_COMMIT = GlobalEnv.START_COMMIT;

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
