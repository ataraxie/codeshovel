package com.felixgrund.codestory.ast;

import com.felixgrund.codestory.ast.parser.JavaScriptParser;

import java.net.URL;

public class Main {

	public static void main(String[] args) {
		try {
			execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void execute() throws Exception {
		URL url = Main.class.getClassLoader().getResource("examples/pocketquery-admin.js");
		if (url == null) {
			throw new Exception("File not found");
		}
		JavaScriptParser parser = new JavaScriptParser("pocketquery-admin.js", url);
		parser.getAllFunctions();

	}

}
