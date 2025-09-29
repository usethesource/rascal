/**
 * Copyright (c) 2024-2025, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/
package org.rascalmpl.library.util;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.Map;
import java.util.Objects;

import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class ParseErrorRecovery {
    private final IRascalValueFactory rascalValues;

    public ParseErrorRecovery(IRascalValueFactory rascalValues) {
        this.rascalValues = rascalValues;
    }

    private static class ScoredTree {
        public final IConstructor tree;
        public final int score;
        public final boolean hasErrors;

        public ScoredTree(IConstructor tree, int score, boolean hasErrors) {
            this.tree = tree;
            this.score = score;
            this.hasErrors = hasErrors;
        }
    }

    /**
    * Disambiguate error trees. Error recovery often produces ambiguous trees where errors can be recovered in multiple ways.
    * This filter removes error trees until no ambiguities caused by error recovery are left.
    * Regular ambiguous trees remain in the parse forest unless `allowAmbiguity` is set to false in which case an error is thrown.
    * It can happen that the original tree has errors but the result does not: 
    * when a tree has an ambiguity where one branch has an error and the other has not the error branch is pruned leaving an error-free tree
    * possibly without ambiguities.
    */

    public IConstructor disambiguateParseErrors(IConstructor arg, IBool allowAmbiguity) {
        return disambiguate(arg, allowAmbiguity.getValue(), true, new HashMap<>()).tree;
    }

    private ScoredTree disambiguate(IConstructor tree, boolean allowAmbiguity, boolean buildTree, Map<IConstructor, ScoredTree> processedTrees) {
        Type type = tree.getConstructorType();
        ScoredTree result;

        if (type == RascalValueFactory.Tree_Appl) {
            result = disambiguateAppl((ITree) tree, allowAmbiguity, buildTree, processedTrees);
        } else if (type == RascalValueFactory.Tree_Amb) {
            result = disambiguateAmb((ITree) tree, allowAmbiguity, buildTree, processedTrees);
        } else {
            // Other trees (cycle, char) do not have subtrees so they have a score of 0 and no errors
            result = new ScoredTree(tree, 0, false);
        }

        return result;
    }

    private ScoredTree disambiguateAppl(ITree appl, boolean allowAmbiguity, boolean buildTree, Map<IConstructor, ScoredTree> processedTrees) {
        ScoredTree result = processedTrees.get(appl);
        if (result != null) {
            return result;
        }

        if (ProductionAdapter.isSkipped(appl.getProduction())) {
            result = new ScoredTree(appl, ((IList) appl.get(1)).length(), true);
        } else {
            IList args = TreeAdapter.getArgs(appl);
            int totalScore = 0;
            boolean hasErrors = false;
            IListWriter disambiguatedArgs = null;

            // Disambiguate and score all children
            for (int i=0; i<args.size(); i++) {
                IValue arg = args.get(i);
                ScoredTree disambiguatedArg = disambiguate((IConstructor) arg, allowAmbiguity, buildTree, processedTrees);
                totalScore += disambiguatedArg.score;
                hasErrors |= disambiguatedArg.hasErrors;
                if (buildTree && disambiguatedArg.tree != arg && disambiguatedArgs == null) {
                    disambiguatedArgs = rascalValues.listWriter();
                    for (int j=0; j<i; j++) {
                        disambiguatedArgs.append(args.get(j));
                    }
                }

                if (disambiguatedArgs != null) {
                    disambiguatedArgs.append(disambiguatedArg.tree);
                }
            }

            // Only build a new tree if at least one of the arguments has changed
            ITree resultTree = null;
            if (disambiguatedArgs != null) {
                // Some arguments have changed
                resultTree = TreeAdapter.setArgs(appl, disambiguatedArgs.done());
            } else if (buildTree) {
                // None of the arguments have changed
                resultTree = appl;
            }

            result = new ScoredTree(resultTree, totalScore, hasErrors);
        }

        processedTrees.put(appl, result);

        return result;
    }

    private ScoredTree disambiguateAmb(ITree amb, boolean allowAmbiguity, boolean buildTree, Map<IConstructor, ScoredTree> processedTrees) {
        ScoredTree result = processedTrees.get(amb);
        if (result != null) {
            return result;
        }

        ISet originalAlts = (ISet) amb.get(0);

        ISetWriter alternativesWithoutErrors = null;

        ScoredTree errorAltWithBestScore = null;
        int altsWithoutErrorsCount  = 0;
        for (IValue alt : originalAlts) {
            ScoredTree disambiguatedAlt = disambiguate((IConstructor) alt, allowAmbiguity, buildTree, processedTrees);
            if (disambiguatedAlt.hasErrors) {
                // Only keep the best of the error trees
                if (errorAltWithBestScore == null || errorAltWithBestScore.score > disambiguatedAlt.score) {
                    errorAltWithBestScore = disambiguatedAlt;
                }
            } else {
                // Non-error tree
                altsWithoutErrorsCount++;
                if (buildTree) {
                    if (alternativesWithoutErrors == null) {
                        alternativesWithoutErrors = rascalValues.setWriter();
                    }
                    alternativesWithoutErrors.insert(disambiguatedAlt.tree);
                }
            }
        }

        if (altsWithoutErrorsCount == 0) {
            assert errorAltWithBestScore != null : "No trees with and no trees without errors?";
            processedTrees.put(amb, errorAltWithBestScore);
            return errorAltWithBestScore;
        }

        if (altsWithoutErrorsCount > 1 && !allowAmbiguity) {
            // We have an ambiguity between non-error trees
            ITree resultTree = buildTree ? resultTree = rascalValues.amb(alternativesWithoutErrors.done()) : amb;
            throw new Ambiguous(resultTree);
        }

        ITree resultTree = null;
        if (buildTree) {
            ISet remainingAlts = alternativesWithoutErrors.done();
            if (remainingAlts.size() == originalAlts.size()) {
                // All children are without errors, return the original tree
                resultTree = amb;
            } else if (remainingAlts.size() == 1) {
                // One child without errors remains, dissolve the amb tree
                resultTree = (ITree) remainingAlts.iterator().next();
            } else {
                // Create a new amb tree with the remaining non-error trees
                resultTree = rascalValues.amb(remainingAlts);
            }
        }

        result = new ScoredTree(resultTree, 0, false);
        processedTrees.put(amb, result);

        return result;
    }

    public IList findAllParseErrors(IConstructor tree) {
        IListWriter errors = rascalValues.listWriter();
        collectErrors((ITree) tree, errors, new HashSet<>());
        return errors.done();
    }

    private void collectErrors(ITree tree, IListWriter errors, Set<IConstructor> processedTrees) {
        Type type = tree.getConstructorType();

        if (type == RascalValueFactory.Tree_Appl) {
            collectApplErrors(tree, errors, processedTrees);
        } else if (type == RascalValueFactory.Tree_Amb) {
            collectAmbErrors(tree, errors, processedTrees);
        }
    }

    private void collectApplErrors(ITree appl, IListWriter errors, Set<IConstructor> processedTrees) {
        if (!processedTrees.add(appl)) {
            return;
        }

        if (ProductionAdapter.isError(appl.getProduction())) {
            errors.append(appl);
        }

        IList args = TreeAdapter.getArgs(appl);
        for (int i=0; i<args.size(); i++) {
            IValue arg = args.get(i);
            collectErrors((ITree) arg, errors, processedTrees);
        }
    }

    private void collectAmbErrors(ITree amb, IListWriter errors, Set<IConstructor> processedTrees) {
        if (!processedTrees.add(amb)) {
            return;
        }

        for (IValue alt : TreeAdapter.getAlternatives(amb)) {
            collectErrors((ITree) alt, errors, processedTrees);
        }
    }

    // We need to keep track of trees that are equal, and be able to find them again based on
    // reference equality of both trees.
    private static class TreePair {
        private ITree tree1;
        private ITree tree2;
        private int hashCode;

        public TreePair(ITree tree1, ITree tree2) {
            this.tree1 = tree1;
            this.tree2 = tree2;
            hashCode = Objects.hash(System.identityHashCode(tree1), System.identityHashCode(tree2));
        }

        @Override
        public boolean equals(Object peer) {
            TreePair pair = (TreePair) peer;
            return tree1 == pair.tree1 && tree2 == pair.tree2;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    };

    public IBool treeEquality(IConstructor tree1, IConstructor tree2) {
        return rascalValues.bool(checkTreeEquality((ITree) tree1, (ITree) tree2, new HashSet<>()));
    }

    private boolean checkTreeEquality(ITree tree1, ITree tree2, Set<TreePair> equalTrees) {
        if (tree1 == tree2) {
            return true;
        }

        Type type = tree1.getConstructorType();

        if (!type.equals(tree2.getConstructorType())) {
            return false;
        }

        if (!checkLocationEquality(tree1, tree2)) {
            return false;
        }

        if (type == RascalValueFactory.Tree_Char) {
            return checkCharEquality(tree1, tree2);
        } else if (type == RascalValueFactory.Tree_Cycle) {
            return checkCycleEquality(tree1, tree2);
        }

        TreePair pair = new TreePair(tree1, tree2);
        if (equalTrees.contains(pair)) {
            return true;
        }

        boolean result;
        if (type == RascalValueFactory.Tree_Appl) {
            result = checkApplEquality(tree1, tree2, equalTrees);
        } else if (type == RascalValueFactory.Tree_Amb) {
            result = checkAmbEquality(tree1, tree2, equalTrees);
        } else {
            throw new IllegalArgumentException("unknown tree type: " + type);
        }

        if (result) {
            equalTrees.add(pair);
        }

        return result;
    }

    private boolean checkApplEquality(ITree tree1, ITree tree2, Set<TreePair> equalTrees) {
        IConstructor type1 = ProductionAdapter.getType(tree1);
        IConstructor type2 = ProductionAdapter.getType(tree2);

        if (!type1.equals(type2)) {
            return false;
        }

        IList args1 = TreeAdapter.getArgs(tree1);
        IList args2 = TreeAdapter.getArgs(tree2);
        int size = args1.size();
        if (size != args2.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            ITree arg1 = (ITree) args1.get(i);
            ITree arg2 = (ITree) args2.get(i);
            if (!checkTreeEquality(arg1, arg2, equalTrees)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkAmbEquality(ITree tree1, ITree tree2, Set<TreePair> equalTrees) {
        ISet alts1 = tree1.getAlternatives();
        ISet alts2 = tree2.getAlternatives();

        if (alts1.size() != alts2.size()) {
            return false;
        }

        BitSet alts2Checked = new BitSet(alts2.size());

        boolean ok = true;

        for (IValue alt1 : alts1) {
            int index2 = 0;
            // This relies on iteration order being stable, which is a pretty safe bet.
            for (IValue alt2 : alts2) {
                if (!alts2Checked.get(index2) && checkTreeEquality((ITree) alt1, (ITree) alt2, equalTrees)) {
                    alts2Checked.set(index2);
                    break;
                }

                index2++;
            }

            if (index2 == alts2.size()) {
                // We did not find alt1 in alts2
                ok = false;
            }
        }

        return ok;
    }

    private boolean checkCharEquality(ITree tree1, ITree tree2) {
        return ((IInteger) tree1.get(0)).intValue() == ((IInteger) tree2.get(0)).intValue();
    }

    private boolean checkCycleEquality(ITree tree1, ITree tree2) {
        return tree1.get(0).equals(tree2.get(0)) && ((IInteger) tree1.get(1)).intValue() == ((IInteger) tree2.get(1)).intValue();
    }

    private boolean checkLocationEquality(ITree tree1, ITree tree2) {
        ISourceLocation loc1 = TreeAdapter.getLocation(tree1);
        ISourceLocation loc2 = TreeAdapter.getLocation(tree2);
        if (loc1 == null && loc2 == null) {
            return true;
        }

        if (loc1 == null || loc2 == null) {
            return false;
        }

        return loc1.getOffset() == loc2.getOffset() && loc1.getLength() == loc2.getLength();
    }

    public IInteger countUniqueTreeNodes(IConstructor tree) {
        BigInteger count = countNodes((ITree) tree, true, new IdentityHashMap<>());
        return rascalValues.integer(count.toByteArray());
    }

    public IInteger countTreeNodes(IConstructor tree) {
        BigInteger count = countNodes((ITree) tree, false, new IdentityHashMap<>());
        return rascalValues.integer(count.toByteArray());
    }

    private BigInteger countNodes(ITree tree, boolean unique, Map<IConstructor, BigInteger> processedNodes) {
        Type type = tree.getConstructorType();
        if (type == RascalValueFactory.Tree_Appl || type == RascalValueFactory.Tree_Amb) {
            BigInteger result = processedNodes.get(tree);
            if (result != null) {
                return unique ? BigInteger.ZERO : result;
            }

            if (type == RascalValueFactory.Tree_Appl) {
                result = countApplNodes(tree, unique, processedNodes);
            } else { // Must be amb
                result = countAmbNodes(tree, unique, processedNodes);
            }

            processedNodes.put(tree, result);

            return result;
        } else {
            return BigInteger.ONE; // Cycle and char trees are counted as one node
        }
    }

    private BigInteger countApplNodes(ITree appl, boolean unique, Map<IConstructor, BigInteger> processedNodes) {
        BigInteger count = BigInteger.ONE;
        IList args = TreeAdapter.getArgs(appl);
        for (int i=args.size()-1; i>=0; i--) {
            count = count.add(countNodes((ITree) args.get(i), unique, processedNodes));
        }
        return count;
    }

    private BigInteger countAmbNodes(ITree amb, boolean unique, Map<IConstructor, BigInteger> processedNodes) {
        BigInteger count = BigInteger.ONE;
        ISet originalAlts = (ISet) amb.get(0);
        for (IValue alt : originalAlts) {
            count = count.add(countNodes((ITree) alt, unique, processedNodes));
        }
        return count;
    }

    public IConstructor maximallyShareTree(IConstructor tree) {
        return maximallyShareTree((ITree) tree, new HashMap<>());
    }

    private ITree maximallyShareTree(ITree tree, Map<ITree, ITree> processedNodes) {
        ITree result = processedNodes.get(tree);
        if (result != null) {
            return result;
        }

        Type type = tree.getConstructorType();
        if (type == RascalValueFactory.Tree_Appl) {
            result = maximallyShareAppl(tree, processedNodes);
        } else if (type == RascalValueFactory.Tree_Amb) {
            result = maximallyShareAmb(tree, processedNodes);
        } else {
            result = tree;
        }

        processedNodes.put(tree, result);

        return result;
    }

    private ITree maximallyShareAppl(ITree appl, Map<ITree, ITree> processedNodes) {
        IList args = TreeAdapter.getArgs(appl);
        IListWriter newArgs = null;
        int argCount = args.size();
        for (int i=0; i<argCount; i++) {
            ITree arg = (ITree) args.get(i);
            ITree newArg = maximallyShareTree(arg, processedNodes);
            if (arg != newArg && newArgs == null) {
                newArgs = rascalValues.listWriter();
                for (int j=0; j<i; j++) {
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

    private ITree maximallyShareAmb(ITree amb, Map<ITree, ITree> processedNodes) {
        ISet alts = TreeAdapter.getAlternatives(amb);
        ISetWriter newAlts = rascalValues.setWriter();

        boolean anyChanges = false;
        for (IValue alt : alts) {
            ITree newAlt = maximallyShareTree((ITree) alt, processedNodes);
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

    public void checkForRegularAmbiguities(IConstructor parseForest) {
        disambiguate(parseForest, false, false, new HashMap<>());
    }

    public IConstructor pruneAmbiguities(IConstructor tree, IInteger maxDepth) {
        return pruneAmbiguities((ITree) tree, maxDepth.intValue(), new IdentityHashMap<>());
    }

    private ITree pruneAmbiguities(ITree tree, int pruneDepth, Map<ITree, ITree> processedNodes) {
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

    private ITree pruneApplAmbiguities(ITree appl, int pruneDepth, Map<ITree, ITree> processedNodes) {
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

    private ITree pruneAmbAmbiguities(ITree amb, int pruneDepth, Map<ITree, ITree> processedNodes) {
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

    public IBool hasParseErrors(IConstructor tree) {
        return rascalValues.bool(hasParseErrors((ITree) tree, new IdentityHashMap<>()));
    }

    private boolean hasParseErrors(ITree tree, Map<ITree, Boolean> processedTrees) {
        Boolean result = processedTrees.get(tree);
        if (result != null) {
            return result;
        }

        Type type = tree.getConstructorType();
        if (type == RascalValueFactory.Tree_Appl) {
            result = hasApplParseErrors(tree, processedTrees);
        } else if (type == RascalValueFactory.Tree_Amb) {
            result = hasAmbParseErrors(tree, processedTrees);
        } else {
            result = false;
        }

        processedTrees.put(tree, result);

        return result;
    }

    private boolean hasApplParseErrors(ITree appl, Map<ITree, Boolean> processedTrees) {
        if (ProductionAdapter.isError(appl.getProduction())) {
            return true;
        }

        IList args = TreeAdapter.getArgs(appl);
        for (int i=0; i<args.size(); i++) {
            IValue arg = args.get(i);
            if (hasParseErrors((ITree) arg, processedTrees)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAmbParseErrors(ITree amb, Map<ITree, Boolean> processedTrees) {
        ISet alts = TreeAdapter.getAlternatives(amb);
        for (IValue alt : alts) {
            if (hasParseErrors((ITree) alt, processedTrees)) {
                return true;
            }
        }

        return false;
    }
}

