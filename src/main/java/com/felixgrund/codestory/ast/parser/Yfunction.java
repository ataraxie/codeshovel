package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.entities.Yparameter;
import com.felixgrund.codestory.ast.entities.Yreturn;

import java.util.List;

public interface Yfunction {

	String getBody();
	String getName();
	List<Yparameter> getParameters();
	Yreturn getReturnStmt();
	int getNameLineNumber();
	int getEndLineNumber();

}
