package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.entities.Yreturn;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.util.Utl;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ReferenceType;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import com.felixgrund.codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaFunction extends AbstractFunction implements Yfunction {

	private String name;
	private String type;
	private Ymodifiers modifiers;
	private Yexceptions exceptions;
	private List<Yparameter> parameters;
	private String body;
	private int beginLine;
	private int endLine;
	private String parentName;

	public JavaFunction(MethodDeclaration method, Commit commit, String sourceFilePath, String sourceFileContent) {
		super(commit, sourceFilePath, sourceFileContent);
		this.name = method.getNameAsString();
		this.type = method.getTypeAsString();
		List<String> modifiers = new ArrayList<>();
		for (Modifier modifier : method.getModifiers()) {
			modifiers.add(modifier.asString());
		}
		this.modifiers = new Ymodifiers(modifiers);
		List<String> exceptions = new ArrayList<>();
		for (ReferenceType type : method.getThrownExceptions()) {
			exceptions.add(type.asString());
		}
		this.exceptions = new Yexceptions(exceptions);

		if (method.getBody().isPresent()) {
			this.body = method.getBody().get().toString();
		}

		List<Yparameter> parametersList = new ArrayList<>();
		List<Parameter> parameterElements = method.getParameters();
		for (Parameter parameterElement : parameterElements) {
			Yparameter parameter = new Yparameter(parameterElement.getNameAsString(), parameterElement.getTypeAsString());
			Map<String, String> metadata = createParameterMetadataMap(parameterElement);
			parameter.setMetadata(metadata);
			parametersList.add(parameter);
		}
		this.parameters = parametersList;

		if (method.getName().getBegin().isPresent()) {
			this.beginLine = method.getName().getBegin().get().line;
		}

		if (method.getEnd().isPresent()) {
			this.endLine = method.getEnd().get().line;
		}

		if (method.getParentNode().isPresent()) {
			Node node = method.getParentNode().get();
			if (node instanceof NodeWithName) {
				this.parentName = ((NodeWithName) method.getParentNode().get()).getNameAsString();
			} else if (node instanceof NodeWithSimpleName) {
				this.parentName = ((NodeWithSimpleName) method.getParentNode().get()).getNameAsString();
			}
		}

	}

	@Override
	public String getId() {
		String ident = this.getName();
		String idParameterString = this.getIdParameterString();
		if (StringUtils.isNotBlank(idParameterString)) {
			ident += "___" + idParameterString;
		}
		return Utl.sanitizeFunctionId(ident);
	}

	@Override
	public String getFunctionPath() {
		return null;
	}

	@Override
	public String getParentName() {
		return this.parentName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Yreturn getReturnStmt() {
		return new Yreturn(this.type);
	}

	@Override
	public Ymodifiers getModifiers() {
		return this.modifiers;
	}

	@Override
	public Yexceptions getExceptions() {
		return this.exceptions;
	}

	@Override
	public String getBody() {
		return this.body;
	}


	@Override
	public List<Yparameter> getParameters() {
		return this.parameters;
	}

	private Map<String,String> createParameterMetadataMap(Parameter parameterElement) {
		Map<String, String> metadata = new HashMap<>();
		String modifiersString = createParameterModifiersString(parameterElement);
		if (modifiersString != null) {
			metadata.put("modifiers", modifiersString);
		}
		String annotationsString = createParameterAnnotationsString(parameterElement);
		if (annotationsString != null) {
			metadata.put("annotations", annotationsString);
		}
		return metadata;
	}

	private String createParameterModifiersString(Parameter parameterElement) {
		String ret = null;
		List<String> modifiers = new ArrayList<String>();
		for (Modifier modifier : parameterElement.getModifiers()) {
			modifiers.add(modifier.asString());
		}
		if (modifiers.size() > 0) {
			ret = StringUtils.join(modifiers, "-");
		}
		return ret;
	}

	private String createParameterAnnotationsString(Parameter parameterElement) {
		String ret = null;
		List<String> annotations = new ArrayList<String>();
		for (Node node : parameterElement.getAnnotations()) {
			annotations.add(node.toString());
		}
		if (annotations.size() > 0) {
			ret = StringUtils.join(annotations, "-");
		}
		return ret;
	}

	@Override
	public int getNameLineNumber() {
		return this.beginLine;
	}

	@Override
	public int getEndLineNumber() {
		return this.endLine;
	}

}
