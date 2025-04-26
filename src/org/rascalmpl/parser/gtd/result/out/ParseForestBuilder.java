/**
 * Copyright (c) 2024-2025, NWO-I Centrum Wiskunde & Informatica (CWI) All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/
package org.rascalmpl.parser.gtd.result.out;

import java.net.URI;
import java.util.IdentityHashMap;
import java.util.Map;

import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

/**
 * This class is responsible for orchestrating the conversion of the parse graph to a parse forest.
 */
public class ParseForestBuilder<T, S> {
    private static final IRascalValueFactory rascalValues = (IRascalValueFactory) ValueFactoryFactory.getValueFactory();

    private IActionExecutor<IConstructor> actionExecutor;
    private INodeFlattener<IConstructor,S> converter;
    private int maxAmbLevel;
    private INodeConstructorFactory<IConstructor, S> nodeConstructorFactory;
    private PositionStore positionStore;

    @SuppressWarnings("unchecked")
    public ParseForestBuilder(IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, int maxAmbLevel, INodeConstructorFactory<T, S> nodeConstructorFactory, PositionStore positionStore) {
        this.actionExecutor = (IActionExecutor<IConstructor>) actionExecutor;
        this.converter = (INodeFlattener<IConstructor, S>) converter;
        this.nodeConstructorFactory = (INodeConstructorFactory<IConstructor, S>)nodeConstructorFactory;
    }

    @SuppressWarnings("unchecked")
    public T buildParseForest(URI inputURI, AbstractNode result, boolean introduceErrorNodes) {
        FilteringTracker filteringTracker = new FilteringTracker();
        // Invoke the forest flattener, a.k.a. "the bulldozer".
        Object rootEnvironment = actionExecutor != null ? actionExecutor.createRootEnvironment() : null;
        IConstructor parseResult = null;
        try {
            parseResult = converter.convert(nodeConstructorFactory, result, positionStore, filteringTracker, actionExecutor, rootEnvironment);
        }
        finally {
              actionExecutor.completed(rootEnvironment, (parseResult == null));
        }

        if(parseResult == null) {
            int offset = filteringTracker.getOffset();
            int endOffset = filteringTracker.getEndOffset();
            int length = endOffset - offset;
            int beginLine = positionStore.findLine(offset);
            int beginColumn = positionStore.getColumn(offset, beginLine);
            int endLine = positionStore.findLine(endOffset);
            int endColumn = positionStore.getColumn(endOffset, endLine);
            throw new ParseError("All results were filtered", inputURI, offset, length, beginLine + 1, endLine + 1,
                beginColumn, endColumn);
        }


        // Filter ambiguities first so introduceErrorNodes has less work to do
        if (converter.hasAmbiguities() && maxAmbLevel >= 0) {
            parseResult = pruneAmbiguities((IConstructor) parseResult, maxAmbLevel);
        }
        if (introduceErrorNodes) {
            parseResult = introduceErrorNodes(parseResult, new IdentityHashMap<IConstructor, IConstructor>());
        }

        return (T) parseResult;	    
    }

    /**
     * After parsing, parse trees will only contain `skipped` nodes. This post-processing step
     * transforms the original tree into a more useful form. In essence, subtrees containing errors look
     * like this after parsing: `appl(prod(S,[<argtypes>]),
     * [<child1>,<child2>,...,appl(skipped([<chars>]))])` This method transforms these trees into:
     * `appl(error(S,prod(S,[<argtypes>]),<dot>), [<child1>,<child2>,...,appl(skipped([<chars>]))])`
     * This means productions that failed to parse can be recognized at the top level. Note that this
     * can only be done when we know the actual type of T is IConstructor.
     */
    private IConstructor introduceErrorNodes(IConstructor tree,  Map<IConstructor, IConstructor> processedTrees) {
        IConstructor result = processedTrees.get(tree);
        if (result != null) {
            return result;
        }

        Type type = tree.getConstructorType();
        if (type == RascalValueFactory.Tree_Appl) {
            result = fixErrorAppl((ITree) tree, processedTrees);
        }
        else if (type == RascalValueFactory.Tree_Char) {
            result = tree;
        }
        else if (type == RascalValueFactory.Tree_Amb) {
            result = fixErrorAmb((ITree) tree, processedTrees);
        }
        else if (type == RascalValueFactory.Tree_Cycle) {
            result = tree;
        }
        else {
            throw new RuntimeException("Unrecognized tree type: " + type);
        }

        if (result != tree && tree.asWithKeywordParameters().hasParameter(RascalValueFactory.Location)) {
            IValue loc = tree.asWithKeywordParameters().getParameter(RascalValueFactory.Location);
            result = result.asWithKeywordParameters().setParameter(RascalValueFactory.Location, loc);
        }

        processedTrees.put(tree, result);

        return result;
    }

    private IConstructor fixErrorAppl(ITree tree, Map<IConstructor, IConstructor> processedTrees) {
        IValue prod = TreeAdapter.getProduction(tree);
        IList childList = TreeAdapter.getArgs(tree);

        ArrayList<IConstructor> newChildren = null;
        boolean errorTree = false;
        int childCount = childList.length();
        for (int i = 0; i < childCount; i++) {
            IConstructor child = (IConstructor) childList.get(i);
            IConstructor newChild = null;

            // Last child could be a skipped child
            if (i == childCount - 1 && child.getConstructorType() == RascalValueFactory.Tree_Appl && TreeAdapter
                .getProduction((ITree) child).getConstructorType() == RascalValueFactory.Production_Skipped) {
                errorTree = true;
                newChild = child;
            }
            else {
                newChild = introduceErrorNodes(child, processedTrees);
            }

            if ((newChild != child || errorTree) && newChildren == null) {
                newChildren = new ArrayList<>(childCount);
                for (int j = 0; j < i; j++) {
                    newChildren.add((IConstructor) childList.get(j));
                }
            }

            if (newChildren != null) {
                newChildren.add(newChild);
            }
        }

        if (errorTree) {
            return nodeConstructorFactory.createErrorNode(newChildren, prod);
        }
        else if (newChildren != null) {
            return nodeConstructorFactory.createSortNode(newChildren, prod);
        }

        return tree;
    }

    private IConstructor fixErrorAmb(ITree tree, Map<IConstructor, IConstructor> processedTrees) {
        ISet alternativeSet = TreeAdapter.getAlternatives(tree);
        ArrayList<IConstructor> alternatives = new ArrayList<>(alternativeSet.size());
        boolean anyChanges = false;
        for (IValue alt : alternativeSet) {
            IConstructor newAlt = introduceErrorNodes((IConstructor) alt, processedTrees);
            if (newAlt != alt) {
                anyChanges = true;
            }
            alternatives.add(newAlt);
        }

        if (anyChanges) {
            return nodeConstructorFactory.createAmbiguityNode(alternatives);
        }

        return tree;
    }

    public static IConstructor pruneAmbiguities(IConstructor tree, int maxDepth) {
        return pruneAmbiguities((ITree) tree, maxDepth, new IdentityHashMap<>());
    }

    private static ITree pruneAmbiguities(ITree tree, int pruneDepth, Map<ITree, ITree> processedNodes) {
        ITree result = processedNodes.get(tree);
        if (result != null) {
            return result;
        }

        Type type = tree.getConstructorType();
        if (type == RascalValueFactory.Tree_Appl) {
            result = pruneApplAmbiguities(tree, pruneDepth, processedNodes);
        }
        else if (type == RascalValueFactory.Tree_Amb) {
            result = pruneAmbAmbiguities(tree, pruneDepth, processedNodes);
        }
        else {
            result = tree;
        }

        processedNodes.put(tree, result);

        return result;
    }

    private static ITree pruneApplAmbiguities(ITree appl, int pruneDepth, Map<ITree, ITree> processedNodes) {
        IList args = TreeAdapter.getArgs(appl);
        IListWriter newArgs = null;
        int argCount = args.size();
        for (int i = 0; i < argCount; i++) {
            ITree arg = (ITree) args.get(i);
            ITree newArg = pruneAmbiguities(arg, pruneDepth, processedNodes);
            if (arg != newArg && newArgs == null) {
                newArgs = rascalValues.listWriter();
                for (int j = 0; j < i; j++) {
                    newArgs.append(args.get(j));
                }
            }

            if (newArgs != null) {
                newArgs.append(newArg);
            }
        }

        if (newArgs == null) {
            return appl;
        }

        return TreeAdapter.setArgs(appl, newArgs.done());
    }

    private static ITree pruneAmbAmbiguities(ITree amb, int pruneDepth, Map<ITree, ITree> processedNodes) {
        ISet alts = TreeAdapter.getAlternatives(amb);
        if (pruneDepth == 0) {
            return pruneAmbiguities((ITree) alts.iterator().next(), 0, processedNodes);
        }

        ISetWriter newAlts = rascalValues.setWriter();

        boolean anyChanges = false;
        for (IValue alt : alts) {
            ITree newAlt = pruneAmbiguities((ITree) alt, pruneDepth-1, processedNodes);
            if (newAlt != alt) {
                anyChanges = true;
            }
            newAlts.append(newAlt);
        }

        if (anyChanges) {
            ITree newAmb = rascalValues.amb(newAlts.done());
            if (amb.asWithKeywordParameters().hasParameter(RascalValueFactory.Location)) {
                IValue loc = amb.asWithKeywordParameters().getParameter(RascalValueFactory.Location);
                newAmb = (ITree) newAmb.asWithKeywordParameters().setParameter(RascalValueFactory.Location, loc);
            }
            return newAmb;
        }

        return amb;
    }

}
