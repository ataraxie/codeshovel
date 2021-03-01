package com.felixgrund.codeshovel.parser.impl;

import com.felixgrund.codeshovel.entities.Yexceptions;
import com.felixgrund.codeshovel.entities.Ymodifiers;
import com.felixgrund.codeshovel.entities.Yparameter;
import com.felixgrund.codeshovel.parser.AbstractFunction;
import com.felixgrund.codeshovel.parser.Yfunction;
import com.felixgrund.codeshovel.wrappers.Commit;
import org.jrubyparser.ast.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RubyFunction extends AbstractFunction<MethodDefNode> implements Yfunction {

    RubyFunction(MethodDefNode function, Commit commit, String sourceFilePath, String sourceFileContent) {
        super(function, commit, sourceFilePath, sourceFileContent);
    }

    @Override
    protected String getInitialName(MethodDefNode rawMethod) {
        return rawMethod.getName();
    }

    @Override
    protected String getInitialType(MethodDefNode rawMethod) {
        return null;
    }

    @Override
    protected Ymodifiers getInitialModifiers(MethodDefNode rawMethod) {
        // TODO is this the best place to put a receiver? Perhaps this should be a context?
        if (rawMethod instanceof DefsNode) {
            DefsNode singletonMethod = (DefsNode) rawMethod;
            Node receiver = singletonMethod.getReceiver();
            if (receiver instanceof INameNode) {
                INameNode namedReceiver = (INameNode) receiver;
                String receiverName = namedReceiver.getName();
                return new Ymodifiers(Collections.singletonList(receiverName));
            }
        }
        return Ymodifiers.NONE;
    }

    @Override
    protected Yexceptions getInitialExceptions(MethodDefNode rawMethod) {
        return Yexceptions.NONE;
    }

    @Override
    protected List<Yparameter> getInitialParameters(MethodDefNode rawMethod) {
        // TODO how does namesOnly affect this line?
        List<String> parameterStrings = rawMethod.getArgs().getNormativeParameterNameList(false);
        List<Yparameter> parametersList = new ArrayList<>();
        for (String parameterString : parameterStrings) {
            Yparameter parameter = new Yparameter(parameterString, null);
            // TODO set metadata using default arguments and optional arguments
            // see: https://medium.com/podiihq/ruby-parameters-c178fdcd1f4e
            // parameter.setMetadata();
            parametersList.add(parameter);
        }
        return parametersList;
    }

    @Override
    protected String getInitialBody(MethodDefNode rawMethod) {
        return rawMethod.getBody().toString();
    }

    @Override
    protected int getInitialBeginLine(MethodDefNode rawMethod) {
        return rawMethod.getNamePosition().getStartLine() + 1;
    }

    @Override
    protected int getInitialEndLine(MethodDefNode rawMethod) {
        return rawMethod.getPosition().getEndLine() + 1;
    }

    @Override
    protected String getInitialParentName(MethodDefNode rawMethod) {
        // TODO this currently only covers class and module
        // node.getClosestIScope is probably more what we want, but we need custom
        //    name retrieval for every node that implements IScope
        for(Node scope = rawMethod.getParent(); scope != null; scope = scope.getParent()) {
            if (scope instanceof IScopingNode) {
                return ((IScopingNode) scope).getCPath().getName();
            }
        }

        return null;
    }

    @Override
    protected String getInitialFunctionPath(MethodDefNode rawMethod) {
        return null;
    }

    @Override
    protected String getInitialAnnotation(MethodDefNode rawMethod) {
        return null;
    }
}
