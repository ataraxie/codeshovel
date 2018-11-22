package com.felixgrund.codeshovel.parser;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.*;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.util.Utl;
import com.felixgrund.codeshovel.visitors.MethodVisitor;
import com.felixgrund.codeshovel.wrappers.FunctionSimilarity;
import com.felixgrund.codeshovel.wrappers.GlobalEnv;
import com.felixgrund.codeshovel.wrappers.StartEnvironment;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractParser implements Yparser {

	private static final Logger log = LoggerFactory.getLogger(AbstractParser.class);
	protected String filePath;
	protected String fileContent;
	protected Commit commit;
	protected String repositoryName;
	protected List<Yfunction> allMethods;
	private StartEnvironment startEnv;

	public AbstractParser(StartEnvironment startEnv, String filePath, String fileContent, Commit commit) throws ParseException {
		this.startEnv = startEnv;

		this.filePath = filePath;
		this.fileContent = fileContent;
		this.commit = commit;
		this.allMethods = parseMethods();
	}

	public abstract double getScopeSimilarity(Yfunction function, Yfunction compareFunction);

	public abstract String getAcceptedFileExtension();

	protected abstract List<Yfunction> parseMethods() throws ParseException;

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


	protected Yparametermetachange getParametersMetaChange(Ycommit commit, Yfunction compareFunction) {
		Yparametermetachange ret = null;
		List<Yparameter> parametersA = compareFunction.getParameters();
		List<Yparameter> parametersB = commit.getMatchedFunction().getParameters();
		if (!Utl.parametersMetadataEqual(parametersA, parametersB)) {
			ret = new Yparametermetachange(this.startEnv, commit.getMatchedFunction(), compareFunction);
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
	public List<Ysignaturechange> getMajorChanges(Ycommit commit, Yfunction compareFunction) throws Exception {
		List<Ysignaturechange> changes = new ArrayList<>();
		Yparameterchange yparameterchange = getParametersChange(commit, compareFunction);
		Yrename yinfilerename = getFunctionRename(commit, compareFunction);
		if (yinfilerename != null) {
			changes.add(yinfilerename);
		}
		if (yparameterchange != null) {
			changes.add(yparameterchange);
		}
		return changes;
	}

	@Override
	public Yfunction getMostSimilarFunction(List<Yfunction> candidates, Yfunction compareFunction, boolean crossFile) {
		log.trace("Trying to find most similar function");
		Map<Yfunction, FunctionSimilarity> similarities = new HashMap<>();
		List<Yfunction> candidatesWithSameName = new ArrayList<>();
		Map<String, Yfunction> functionIdMap = Utl.functionsToIdMap(candidates);

		Yfunction sameIdFunction = functionIdMap.get(compareFunction.getId());
		if (sameIdFunction != null) {
			Double bodySimilarity = null;
			if (crossFile) {
				bodySimilarity = Utl.getBodySimilarity(compareFunction, sameIdFunction);
			}
			// If we are in-file just return because the ID will exist only once.
			// If we are cross-file, take bodySimilarity as an additional measure.
			if (!crossFile || bodySimilarity > 0.8) {
				log.trace("Found function with same ID and bodySimilarity > 0.8. Done.");
				return sameIdFunction;
			}
		}


		for (Yfunction candidate : candidates) {
			Double bodySimilarity = Utl.getBodySimilarity(compareFunction, candidate);

			// If the body is 100% equal we assume it's the correct candidate.
			if (bodySimilarity == 1) {
				log.trace("Found function with body similarity of 1. Done.");
				return candidate;
			}

			Integer lineNumberDistance = null;
			double scopeSimilarity = getScopeSimilarity(candidate, compareFunction);

			if (bodySimilarity > 0.9 && scopeSimilarity == 1) {
				if (!crossFile) {
					lineNumberDistance = Utl.getLineNumberDistance(candidate, compareFunction);
				}
				if (crossFile || lineNumberDistance < 10) {
					log.trace("Found function with body similarity > 0.9 and line distance < 10 and scope similarity of 1. Done.");
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
		log.trace("Highest similarity with overall similarity of {}: {}", similarity);

		if (GlobalEnv.WRITE_SIMILARITIES && compareFunction != null && mostSimilarFunction != null) {
			Utl.writeJsonSimilarity(this.repositoryName, this.filePath, compareFunction, mostSimilarFunction, similarity);
		}

		if (candidatesWithSameName.size() == 1) {
			Yfunction candidateWithSameName = candidatesWithSameName.get(0);
			FunctionSimilarity candidateSimilarity = similarities.get(candidateWithSameName);
			log.trace("Highest similarity was < 0.85. But found single candidate with same function name. Using lower similarity threshold");

			// TODO: this is temporary! We need to make our similarity algorithm much smarter!
			// Use return statement, parameters, signature in general and other things! This should all go into
			// FunctionSimilarity.
			// If this is cross-file, we need to be more strict: it's much more likely that a method with the same
			// name was removed that is completely independent.
			if (crossFile & candidateSimilarity.getBodySimilarity() > 0.8) {
				log.trace("Cross-file comparison and body similarity > 0.8. Accepting function.");
				return candidateWithSameName;
			} else if (!crossFile && candidateSimilarity.getOverallSimilarity() > 0.5) {
				log.trace("In-file comparison and overall similarity > 0.5. Accepting function.");
				return candidateWithSameName;
			}
		}

		if (mostSimilarFunctionSimilarity > 0.82) {
			if (!shouldBodyBeVerySimilar(compareFunction, mostSimilarFunction) || mostSimilarFunctionSimilarity > 0.95) {
				log.trace("Highest similarity is high enough. Accepting function.");
				return mostSimilarFunction;
			}
		}

		log.trace("Highest similarity was < 0.85 and did not find single candidate with same name. Unable to find matching candidate.");
		return null;
	}

	private boolean shouldBodyBeVerySimilar(Yfunction aFunction, Yfunction bFunction) {
		boolean ret = false;
		String aBody = aFunction.getBody();
		String bBody = bFunction.getBody();
		if (Utl.countLineNumbers(aBody) <= 3 || Utl.countLineNumbers(bBody) <= 3) {
			ret = aBody.length() < 60 || bBody.length() < 60;
		}
		return ret;
	}

	@Override
	public Map<String, Yfunction> getAllMethodsCount() {
		return transformMethodsToMap(getAllMethods());
	}

	@Override
	public List<Yfunction> getAllMethods() {
		return this.allMethods;
	}

	@Override
	public boolean functionNamesConsideredEqual(String aName, String bName) {
		return aName != null && aName.equals(bName);
	}

	protected Map<String, Yfunction> transformMethodsToMap(List<Yfunction> methods) {
		Map<String, Yfunction> ret = new HashMap<>();
		for (Yfunction method : methods) {
			ret.put(method.getId(), method);
		}
		return ret;
	}

	@Override
	public List<Yfunction> findMethodsByLineRange(int beginLine, int endLine) {
		return findAllMethods(new MethodVisitor(this.allMethods) {
			@Override
			public boolean methodMatches(Yfunction method) {
				int lineNumber = method.getNameLineNumber();
				return lineNumber >= beginLine && lineNumber <= endLine;
			}
		});
	}

	protected List<Yfunction> findAllMethods(MethodVisitor visitor) {
		List<Yfunction> matchedMethods = new ArrayList<>();
		for (Yfunction method : this.allMethods) {
			if (visitor.methodMatches(method)) {
				matchedMethods.add(method);
			}
		}
		return matchedMethods;
	}

	private Yfunction findMethod(MethodVisitor visitor) {
		Yfunction ret = null;
		List<Yfunction> matchedNodes = findAllMethods(visitor);
		if (matchedNodes.size() > 0) {
			ret = matchedNodes.get(0);
		}
		return ret;
	}

	@Override
	public Yfunction findFunctionByNameAndLine(String name, int line) {
		return findMethod(new MethodVisitor(this.allMethods) {
			@Override
			public boolean methodMatches(Yfunction method) {
				String methodName = method.getName();
				int methodLineNumber = method.getNameLineNumber();
				return name.equals(methodName) && line == methodLineNumber;
			}
		});
	}

}
