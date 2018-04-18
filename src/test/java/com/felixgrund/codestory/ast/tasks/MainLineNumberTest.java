package com.felixgrund.codestory.ast.tasks;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainLineNumberTest {

	private static final String SEARCH_METHOD = "getAcceptableTokens";
	private static final int SEARCH_LINE_NUMBER = 255;

	public static void main(String[] args) throws Exception {
		String fileContent = getFileContent("sources/WhitespaceAroundCheck.java");
		CompilationUnit compilationUnit = JavaParser.parse(fileContent);
		compilationUnit.accept(new VoidVisitorAdapter<Void>() {
			@Override
			public void visit(MethodDeclaration method, Void arg) {
				super.visit(method, arg);
				if (SEARCH_METHOD.equals(method.getNameAsString())) {
					System.out.println("Found method! Line number: " + method.getBegin().get().line);
				}
			}
		}, null);
	}

	private static String getFileContent(String path) throws IOException {
		ClassLoader classLoader = MainLineNumberTest.class.getClassLoader();
		File file = new File(classLoader.getResource(path).getFile());
		return FileUtils.readFileToString(file, "utf-8");
	}

}
