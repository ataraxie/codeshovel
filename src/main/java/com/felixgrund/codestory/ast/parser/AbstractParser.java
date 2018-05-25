package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.util.SimilarityUtil;
import com.felixgrund.codestory.ast.util.Utl;
import com.felixgrund.codestory.ast.wrappers.FunctionSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractParser implements Yparser {

	private static final Logger log = LoggerFactory.getLogger(AbstractParser.class);

	protected String filePath;
	protected String fileContent;
	protected String commitName;
	protected String repoName;

	public abstract boolean functionNamesConsideredEqual(String aName, String bName);
	public abstract double getScopeSimilarity(Yfunction function, Yfunction compareFunction);

	public AbstractParser(String repoName, String filePath, String fileContent, String commitName) {
		this.repoName = repoName;
		this.filePath = filePath;
		this.fileContent = fileContent;
		this.commitName = commitName;
	}

	protected Yreturntypechange getReturnTypeChange(Ycommit commit, Yfunction compareFunction) {
		Yreturntypechange ret = null;
		Yreturn returnA = compareFunction.getReturnStmt();
		Yreturn returnB = commit.getMatchedFunction().getReturnStmt();
		if (returnA != null && !returnA.equals(returnB)) {
			ret = new Yreturntypechange(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Yinfilerename getFunctionRename(Ycommit commit, Yfunction compareFunction) {
		Yinfilerename ret = null;
		if (compareFunction != null) {
			if (!functionNamesConsideredEqual(commit.getMatchedFunction().getName(), compareFunction.getName())) {
				ret = new Yinfilerename(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
			}
		}
		return ret;
	}

	protected Yparameterchange getParametersChange(Ycommit commit, Yfunction compareFunction) {
		Yparameterchange ret = null;
		List<Yparameter> parametersA = compareFunction.getParameters();
		List<Yparameter> parametersB = commit.getMatchedFunction().getParameters();
		if (!parametersA.equals(parametersB)) {
			ret = new Yparameterchange(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Yexceptionschange getExceptionsChange(Ycommit commit, Yfunction compareFunction) {
		Yexceptionschange ret = null;
		Yexceptions exceptionsA = compareFunction.getExceptions();
		Yexceptions exceptionsB = commit.getMatchedFunction().getExceptions();
		if (exceptionsA != null && !exceptionsA.equals(exceptionsB)) {
			ret = new Yexceptionschange(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Ymodifierchange getModifiersChange(Ycommit commit, Yfunction compareFunction) {
		Ymodifierchange ret = null;
		Ymodifiers modifiersA = compareFunction.getModifiers();
		Ymodifiers modifiersB = commit.getMatchedFunction().getModifiers();
		if (modifiersA != null && !modifiersA.equals(modifiersB)) {
			ret = new Ymodifierchange(commit, commit.getParent(), commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Ybodychange getBodyChange(Ycommit commit, Yfunction compareFunction) {
		Ybodychange ret = null;
		Yfunction function = commit.getMatchedFunction();
		if (function != null && compareFunction != null && !function.getBody().equals(compareFunction.getBody())) {
			ret = new Ybodychange(commit);
		}
		return ret;
	}

	@Override
	public Yfunction getMostSimilarFunction(List<Yfunction> candidates, Yfunction compareFunction, boolean writeOutputFile) {
		log.info("Trying to find most similar function");
		Map<Yfunction, FunctionSimilarity> similarities = new HashMap<>();
		for (Yfunction candidate : candidates) {
			if (candidate.getId().equals(compareFunction.getId())) {
				log.info("Found function with same ID. Done.");
				return candidate;
			}

			double bodySimilarity = Utl.getBodySimilarity(compareFunction, candidate);
			if (bodySimilarity == 1) {
				log.info("Found function with body similarity of 1. Done.");
				return candidate;
			}

			int lineNumberDistance = SimilarityUtil.getLineNumberDistance(candidate, compareFunction);
			double scopeSimilarity = getScopeSimilarity(candidate, compareFunction);

			if (bodySimilarity > 0.9 && lineNumberDistance < 10 && scopeSimilarity == 1) {
				log.info("Found function with body similarity > 0.9 and line distance < 10 and scope similarity of 1. Done.");
				return candidate;
			}

			FunctionSimilarity similarity = new FunctionSimilarity();
			similarity.setBodySimilarity(bodySimilarity);
			similarity.setScopeSimilarity(scopeSimilarity);
			similarities.put(candidate, similarity);
		}

		Yfunction mostSimilarFunction = null;
		double mostSimilarFunctionSimilarity = 0;
		for (Yfunction candidate : similarities.keySet()) {
			FunctionSimilarity similarity = similarities.get(candidate);
			double lineNumberSimilarity = SimilarityUtil.getLineNumberSimilarity(candidate, compareFunction);
			similarity.setLineNumberSimilarity(lineNumberSimilarity);
			double overallSimilarity = similarity.getOverallSimilarity();
			if (mostSimilarFunction == null || overallSimilarity > mostSimilarFunctionSimilarity) {
				mostSimilarFunctionSimilarity = overallSimilarity;
				mostSimilarFunction = candidate;
			}
		}

		FunctionSimilarity similarity = similarities.get(mostSimilarFunction);
		log.info("Highest similarity with overall similarity of {}: {}", similarity);

		if (writeOutputFile) {
			Utl.writeJsonSimilarity(this.repoName, this.filePath, compareFunction, mostSimilarFunction, similarity);
		}

		if (mostSimilarFunctionSimilarity > 0.85) {
			log.info("Highest similarity is > 0.85. Accepting function.");
			return mostSimilarFunction;
		} else {
			log.info("Highest similarity is < 0.85. Rejecting function.");
			return null;
		}
	}
}
