package com.felixgrund.codeshovel.parser;

import com.felixgrund.codeshovel.changes.*;
import com.felixgrund.codeshovel.entities.*;
import com.felixgrund.codeshovel.exceptions.ParseException;
import com.felixgrund.codeshovel.util.Thresholds;
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
    protected final String filePath;
    protected final String fileContent;
    protected final Commit commit;
    private final StartEnvironment startEnv;

    protected String repositoryName;
    protected List<Yfunction> allMethods;

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

    protected Ydocchange getDocChange(Ycommit commit, Yfunction compareFunction) {
        Ydocchange ret = null;
        String docA = compareFunction.getFunctionDoc();
        String docB = commit.getMatchedFunction().getFunctionDoc();
        if(!docA.equals(docB)) {
            ret = new Ydocchange(this.startEnv, commit.getMatchedFunction(), compareFunction);
        }
        return ret;
    }

    protected Yannotationchange getAnnotationChange(Ycommit commit, Yfunction compareFunction) {
        Yannotationchange ret = null;
        String annotationA = compareFunction.getAnnotation();
        String annotationB = commit.getMatchedFunction().getAnnotation();
        if (!annotationA.equals(annotationB)) {
            ret = new Yannotationchange(this.startEnv, commit.getMatchedFunction(), compareFunction);
        }
        return ret;
    }

    protected Yformatchange getFormatChange(Ycommit commit, Yfunction compareFunction) {
        Yformatchange ret = null;
        Yfunction function = commit.getMatchedFunction();
        if (Utl.isFormatChange(function, compareFunction)) {
            ret = new Yformatchange(this.startEnv, commit.getMatchedFunction(), compareFunction);
        }
        return ret;
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
    public List<Ychange> getMinorChanges(Ycommit commit, Yfunction compareFunction) {
        List<Ychange> changes = new ArrayList<>();
        Ybodychange ybodychange = getBodyChange(commit, compareFunction);
        if (ybodychange != null) {
            changes.add(ybodychange);
        }
        return changes;
    }

    @Override
    public List<Ysignaturechange> getMajorChanges(Ycommit commit, Yfunction compareFunction) {
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

    /**
     * Finds the most similar function to compareFunction from a list of candidates.
     * <p>
     * This is used by both:
     * CrossFileInterpreter
     * InFileInterpreter
     * <p>
     * Making two copies of this function, one for cross-file and one for
     * within-file would make the code simpler to understand, but might
     * cause challenges when updating in the future when changes need to be
     * made to both.
     *
     * @param candidates      Set of candidate methods
     * @param compareFunction Similar method
     * @param crossFile       True if other files in commit should be searched, false if only in-file
     * @return a match, if one is made, otherwise null
     */
    @Override
    public Yfunction getMostSimilarFunction(List<Yfunction> candidates, Yfunction compareFunction, boolean crossFile) {
        log.trace("Trying to find most similar function");
        final double EXACT_MATCH = 1;

        Map<Yfunction, FunctionSimilarity> similarities = new HashMap<>();
        Map<String, Yfunction> functionSignatureMap = Utl.functionsToIdMap(candidates);

        // Phase 1
        // First look for a similar match with the same (exact) signature
        Yfunction sameSignatureFunction = functionSignatureMap.get(compareFunction.getId());
        if (sameSignatureFunction != null) {
            // If we are in-file just return because the signature will exist only once.
            // If we are cross-file, take bodySimilarity as an additional measure.
            if (crossFile == false) {
                log.trace("Matched same sig within file.");
                return sameSignatureFunction;
            } else {
                // use body similarity, not function similarity as the signature is already known to be identical
                double bodySimilarity = Utl.getBodySimilarity(compareFunction, sameSignatureFunction);
                if (isFunctionSimilar(compareFunction, sameSignatureFunction, bodySimilarity)) {
                    log.trace("Matched same sig cross file.");
                    return sameSignatureFunction;
                }
            }
        }

        // Phase 2
        // Next, look for candidates that have extremely similar bodies
        if (crossFile == false) {
            // If the body is identical, assume it the same method
            for (Yfunction candidate : candidates) {
                double bodySimilarity = Utl.getBodySimilarity(compareFunction, candidate);
                if (bodySimilarity == EXACT_MATCH) {
                    log.trace("Matched same body within file.");
                    return candidate;
                }
            }
        } else {
            // If the body and scope are similar
            for (Yfunction candidate : candidates) {
                double bodySimilarity = Utl.getBodySimilarity(compareFunction, candidate);
                double scopeSimilarity = getScopeSimilarity(compareFunction, candidate);
                // If the body is identical -or-
                // The scope is identical and the body is very close
                if (bodySimilarity == EXACT_MATCH ||
                        (bodySimilarity > Thresholds.MOST_SIM_FUNCTION_MAX.val() &&  // BODY_SIM_THRESHOLD
                                scopeSimilarity == EXACT_MATCH)) {
                    log.trace("Matched extremely similar body cross file.");
                    return candidate;
                }
            }
        }

        // Generate similarity scores for all candidates
        // Identify functions with the same name
        // (but maybe not with the same full signature)
        List<Yfunction> candidatesWithSameName = new ArrayList<>();
        for (Yfunction candidate : candidates) {
            if (candidate.getName().equals(compareFunction.getName())) {
                candidatesWithSameName.add(candidate);
            }

            double bodySimilarity = Utl.getBodySimilarity(compareFunction, candidate);
            double scopeSimilarity = getScopeSimilarity(compareFunction, candidate);
            double nameSimilarity = Utl.getNameSimilarity(compareFunction, candidate);

            FunctionSimilarity similarity = new FunctionSimilarity(crossFile);
            similarity.setBodySimilarity(bodySimilarity);
            similarity.setScopeSimilarity(scopeSimilarity);
            similarity.setNameSimilarity(nameSimilarity);
            if (crossFile == false) {
                // line number similarity does not make sense cross-file
                double lineNumberSimilarity = Utl.getLineNumberSimilarity(candidate, compareFunction);
                similarity.setLineNumberSimilarity(lineNumberSimilarity);
            }
            similarities.put(candidate, similarity);
        }

        // Phase 3
        // If a function does have the same name, consider it ahead of other matches
        if (candidatesWithSameName.size() > 0) {
            // prepopulate with first option
            Yfunction highestCandidate = candidatesWithSameName.get(0);
            FunctionSimilarity highestSimilarity = similarities.get(highestCandidate);

            // if there are candidates with the same name, take the best
            for (Yfunction candidate : candidatesWithSameName) {
                FunctionSimilarity similarity = similarities.get(candidate);
                if (similarity.getOverallSimilarity() > highestSimilarity.getOverallSimilarity()) {
                    highestSimilarity = similarity;
                    highestCandidate = candidate;
                }
            }

            // TODO: Function name matching could be much stronger:
            // Could use parameters, param types, return type, etc.
            // Easiest way would be to build them into FunctionSimilarity
            // so that getOverallSimilarity would consider them.

            if (crossFile == false) {
                // Within-file similarity can be less strict
                if (highestSimilarity.getOverallSimilarity() > Thresholds.BODY_SIM_WITHIN_FILE.val()) {
                    log.trace("Matched most similar function within file.");
                    return highestCandidate;
                }
            } else {
                // Cross-file similarity is more strict
                // as it's much more likely that a method with the same
                // name was removed that is completely independent.
                if (highestSimilarity.getBodySimilarity() > Thresholds.BODY_SIM_CROSS_FILE.val()) {
                    log.trace("Matched most similar function cross file.");
                    return highestCandidate;
                }
            }
        }

        // Phase 4: Find the most similar function
        // Does not have cross-file dependencies because this is handled
        // by FunctionSimilarity::computeOverallSimilarity()
        Yfunction mostSimilarFunction = null;
        double mostSimilarFunctionSimilarity = -1;
        for (Yfunction candidate : similarities.keySet()) {
            FunctionSimilarity similarity = similarities.get(candidate);

            double overallSimilarity = similarity.getOverallSimilarity();
            if (overallSimilarity > mostSimilarFunctionSimilarity) {
                mostSimilarFunctionSimilarity = overallSimilarity;
                mostSimilarFunction = candidate;
            }
        }

        FunctionSimilarity similarity = similarities.get(mostSimilarFunction);
        // log.trace("Highest similarity with overall similarity of {}", similarity);

        // Write logs, if requested
        if (GlobalEnv.WRITE_SIMILARITIES && mostSimilarFunction != null) {
            Utl.writeJsonSimilarity(this.repositoryName, this.filePath,
                    compareFunction, mostSimilarFunction, similarity);
        }

        // Finally, see if the best-matched function that remains is close enough
        // Use Overall similarity, not body similarity
        if (mostSimilarFunction != null) {
            if (isFunctionSimilar(compareFunction, mostSimilarFunction, mostSimilarFunctionSimilarity)) {
                log.trace("Matched close enough function.");
                return mostSimilarFunction;
            }
        }

        log.trace("Failed to match.");
        return null;
    }

    private boolean isFunctionSimilar(Yfunction compareFcn, Yfunction candidateFcn, double simScore) {
        if (compareFcn == null || candidateFcn == null) {
            return false;
        }
        if (isShortFunction(compareFcn, candidateFcn)) {
            // be more strict because small functions are
            // more prone to spurious matches
            if (simScore > Thresholds.MOST_SIM_FUNCTION_MAX.val()) {
                return true;
            }
        } else {
            // initial threshold check is sufficient for long functions
            if (simScore > Thresholds.MOST_SIM_FUNCTION.val()) {
                return true;
            }
        }
        return false;
    }

    private boolean isShortFunction(Yfunction aFunction, Yfunction bFunction) {
        String aBody = aFunction.getBody();
        String bBody = bFunction.getBody();

        return aBody.length() < Thresholds.LONG_METHOD_CHAR_THRESHOLD.val() ||
                bBody.length() < Thresholds.LONG_METHOD_CHAR_THRESHOLD.val();
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

    private Yfunction getCandidateWithSameParent(List<Yfunction> candidates, Yfunction compareMethod) {
        for (Yfunction candidateMethod : candidates) {
            if (candidateMethod.getParentName() != null && candidateMethod.getParentName().equals(compareMethod.getParentName())) {
                log.trace("Found correct candidate. Parent name: {}", candidateMethod.getParentName());
                return candidateMethod;
            }
        }
        return null;
    }

    @Override
    public Yfunction findFunctionByOtherFunction(Yfunction otherMethod) {
        Yfunction function = null;
        List<Yfunction> candidatesByNameAndParams = findFunctionsByNameAndParams(otherMethod);
        if (candidatesByNameAndParams.size() == 1) {
            function = candidatesByNameAndParams.get(0);
        } else if (candidatesByNameAndParams.size() > 1) {
            log.trace("Found more than one matches for name and parameters. Finding candidate with highest body similarity");
            function = getMostSimilarFunction(candidatesByNameAndParams, otherMethod, false);
        }

        return function;
    }

    private List<Yfunction> findFunctionsByNameAndParams(Yfunction otherFunction) {
        return findAllMethods(new MethodVisitor(this.allMethods) {
            @Override
            public boolean methodMatches(Yfunction method) {
                List<Yparameter> parametersCurrent = method.getParameters();
                String functionNameCurrent = method.getName();
                boolean nameMatches = functionNamesConsideredEqual(functionNameCurrent, otherFunction.getName());
                boolean paramsMatch = parametersCurrent.equals(otherFunction.getParameters());
                return nameMatches && paramsMatch;
            }
        });
    }

}
