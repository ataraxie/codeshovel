package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.entities.Ycommit;

import java.util.LinkedHashMap;

public interface Interpreter {

	void interpret() throws Exception;
	Ychange getInterpretation();

}
