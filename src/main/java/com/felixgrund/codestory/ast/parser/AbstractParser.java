package com.felixgrund.codestory.ast.parser;

import com.felixgrund.codestory.ast.changes.*;
import com.felixgrund.codestory.ast.entities.*;
import com.felixgrund.codestory.ast.exceptions.ParseException;
import com.felixgrund.codestory.ast.wrappers.Environment;
import com.felixgrund.codestory.ast.util.Utl;
import com.felixgrund.codestory.ast.wrappers.FunctionSimilarity;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractParser implements Yparser {

	private static final Logger log = LoggerFactory.getLogger(AbstractParser.class);

	private Environment startEnv;

	protected String filePath;
	protected String fileContent;
	protected RevCommit commit;
	protected String repositoryName;
	protected Repository repository;

	public abstract boolean functionNamesConsideredEqual(String aName, String bName);
	public abstract double getScopeSimilarity(Yfunction function, Yfunction compareFunction);
	public abstract String getAcceptedFileExtension();
	protected abstract Object parse() throws ParseException;

	public AbstractParser(Environment startEnv, String filePath, String fileContent, RevCommit commit) throws ParseException {
		this.startEnv = startEnv;

		this.repository = startEnv.getRepository();
		this.repositoryName = startEnv.getRepositoryName();
		this.filePath = filePath;
		this.fileContent = fileContent;
		this.commit = commit;
		parse();
	}

	protected Yreturntypechange getReturnTypeChange(Ycommit commit, Yfunction compareFunction) {
		Yreturntypechange ret = null;
		Yreturn returnA = compareFunction.getReturnStmt();
		Yreturn returnB = commit.getMatchedFunction().getReturnStmt();
		if (returnA != null && !returnA.equals(returnB)) {
			ret = new Yreturntypechange(this.startEnv, commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Yrename getFunctionRename(Ycommit commit, Yfunction compareFunction) {
		Yrename ret = null;
		if (compareFunction != null) {
			if (!functionNamesConsideredEqual(commit.getMatchedFunction().getName(), compareFunction.getName())) {
				ret = new Yrename(this.startEnv, commit.getMatchedFunction(), compareFunction);
			}
		}
		return ret;
	}

	protected Yparameterchange getParametersChange(Ycommit commit, Yfunction compareFunction) {
		Yparameterchange ret = null;
		List<Yparameter> parametersA = compareFunction.getParameters();
		List<Yparameter> parametersB = commit.getMatchedFunction().getParameters();
		if (!parametersA.equals(parametersB)) {
			ret = new Yparameterchange(this.startEnv, commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Yexceptionschange getExceptionsChange(Ycommit commit, Yfunction compareFunction) {
		Yexceptionschange ret = null;
		Yexceptions exceptionsA = compareFunction.getExceptions();
		Yexceptions exceptionsB = commit.getMatchedFunction().getExceptions();
		if (exceptionsA != null && !exceptionsA.equals(exceptionsB)) {
			ret = new Yexceptionschange(this.startEnv, commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Ymodifierchange getModifiersChange(Ycommit commit, Yfunction compareFunction) {
		Ymodifierchange ret = null;
		Ymodifiers modifiersA = compareFunction.getModifiers();
		Ymodifiers modifiersB = commit.getMatchedFunction().getModifiers();
		if (modifiersA != null && !modifiersA.equals(modifiersB)) {
			ret = new Ymodifierchange(this.startEnv, commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	protected Ybodychange getBodyChange(Ycommit commit, Yfunction compareFunction) {
		Ybodychange ret = null;
		Yfunction function = commit.getMatchedFunction();
		if (function != null && compareFunction != null && !function.getBody().equals(compareFunction.getBody())) {
			ret = new Ybodychange(this.startEnv, commit.getMatchedFunction(), compareFunction);
		}
		return ret;
	}

	@Override
	public Yfunction getMostSimilarFunction(List<Yfunction> candidates, Yfunction compareFunction, boolean crossFile, boolean writeOutputFile) {
		log.info("Trying to find most similar function");
		Map<Yfunction, FunctionSimilarity> similarities = new HashMap<>();
		List<Yfunction> candidatesWithSameName = new ArrayList<>();
		for (Yfunction candidate : candidates) {

			Double bodySimilarity = null;
			if (candidate.getId().equals(compareFunction.getId())) {
				if (crossFile) {
					bodySimilarity = Utl.getBodySimilarity(compareFunction, candidate);
				}
				// If we are in-file just return because the ID will exist only once.
				// If we are cross-file, take bodySimilarity as an additional measure.
				if (!crossFile || bodySimilarity > 0.8) {
					log.info("Found function with same ID and bodySimilarity > 0.8. Done.");
					return candidate;
				}
			}

			if (bodySimilarity == null) { // Don't do it twice.
				bodySimilarity = Utl.getBodySimilarity(compareFunction, candidate);
			}

			// If the body is 100% equal we assume it's the correct candidate.
			if (bodySimilarity == 1) {
				log.info("Found function with body similarity of 1. Done.");
				return candidate;
			}

			Integer lineNumberDistance = null;
			double scopeSimilarity = getScopeSimilarity(candidate, compareFunction);

			if (bodySimilarity > 0.9 && scopeSimilarity == 1) {
				if (!crossFile) {
					lineNumberDistance = Utl.getLineNumberDistance(candidate, compareFunction);
				}
				if (crossFile || lineNumberDistance < 10) {
					log.info("Found function with body similarity > 0.9 and line distance < 10 and scope similarity of 1. Done.");
					return candidate;
				}
			}

			if (candidate.getName().equals(compareFunction.getName())) {
				candidatesWithSameName.add(candidate);
			}

			double nameSimilarity = Utl.getNameSimilarity(compareFunction, candidate);

			FunctionSimilarity similarity = new FunctionSimilarity(crossFile);
			similarity.setBodySimilarity(bodySimilarity);
			similarity.setScopeSimilarity(scopeSimilarity);
			similarity.setNameSimilarity(nameSimilarity);
			similarities.put(candidate, similarity);
		}

		Yfunction mostSimilarFunction = null;
		double mostSimilarFunctionSimilarity = 0;
		for (Yfunction candidate : similarities.keySet()) {
			FunctionSimilarity similarity = similarities.get(candidate);
			if (!crossFile) {
				double lineNumberSimilarity = Utl.getLineNumberSimilarity(candidate, compareFunction);
				similarity.setLineNumberSimilarity(lineNumberSimilarity);
			}

			double overallSimilarity = similarity.getOverallSimilarity();
			if (mostSimilarFunction == null || overallSimilarity > mostSimilarFunctionSimilarity) {
				mostSimilarFunctionSimilarity = overallSimilarity;
				mostSimilarFunction = candidate;
			}
		}

		FunctionSimilarity similarity = similarities.get(mostSimilarFunction);
		log.info("Highest similarity with overall similarity of {}: {}", similarity);

		if (writeOutputFile) {
			Utl.writeJsonSimilarity(this.repositoryName, this.filePath, compareFunction, mostSimilarFunction, similarity);
		}

		if (mostSimilarFunctionSimilarity > 0.85) {
			log.info("Highest similarity is > 0.85. Accepting function.");
			return mostSimilarFunction;
		}

		if (candidatesWithSameName.size() == 1 && mostSimilarFunctionSimilarity > 0.7) {
			log.info("Highest similarity was < 0.85. But found single candidate with same function name and similarity > 0.7. Done.");
			return candidatesWithSameName.get(0);
		}

		log.info("Highest similarity was < 0.85 and did not find single candidate with same name. Unable to find matching candidate.");
		return null;
	}
}
