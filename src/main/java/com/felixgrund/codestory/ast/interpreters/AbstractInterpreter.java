package com.felixgrund.codestory.ast.interpreters;

import com.felixgrund.codestory.ast.changes.Ychange;
import com.felixgrund.codestory.ast.entities.Ycommit;
import com.felixgrund.codestory.ast.parser.Yfunction;
import com.felixgrund.codestory.ast.parser.Yparser;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;

import java.util.List;

public abstract class AbstractInterpreter {

	protected abstract Ychange interpret() throws Exception;



}
