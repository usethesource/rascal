/**
 * Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class ErrorRecovery {
    private final IRascalValueFactory rascalValues;

    public ErrorRecovery(IRascalValueFactory rascalValues) {
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

    public IConstructor disambiguateErrors(IConstructor arg, IBool allowAmbiguity) {
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
        for (IValue alt : originalAlts) {
            ScoredTree disambiguatedAlt = disambiguate((IConstructor) alt, allowAmbiguity, buildTree, processedTrees);
            if (disambiguatedAlt.hasErrors) {
                // Only keep the best of the error trees
                if (errorAltWithBestScore == null || errorAltWithBestScore.score > disambiguatedAlt.score) {
                    errorAltWithBestScore = disambiguatedAlt;
                }
            } else {
                // Non-error tree
                if (alternativesWithoutErrors == null) {
                    alternativesWithoutErrors = rascalValues.setWriter();
                }
                alternativesWithoutErrors.insert(disambiguatedAlt.tree);
            }
        }

        if (alternativesWithoutErrors == null) {
            assert errorAltWithBestScore != null : "No trees with and no trees without errors?";
            processedTrees.put(amb, errorAltWithBestScore);
            return errorAltWithBestScore;
        }

        ISet remainingAlts = alternativesWithoutErrors.done();

        ITree resultTree = null;

        if (remainingAlts.size() > 1 && !allowAmbiguity) {
            // We have an ambiguity between non-error trees
            resultTree = rascalValues.amb(remainingAlts);
            throw new Ambiguous(resultTree);
        }

        if (buildTree) {
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

    public IList findAllErrors(IConstructor tree) {
        IListWriter errors = rascalValues.listWriter();
        collectErrors((ITree) tree, errors, new HashSet<>());
        return errors.done();
    }

    public boolean hasErrors(IConstructor tree) {
        return !findAllErrors(tree).isEmpty();
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

    public void checkForRegularAmbiguities(IConstructor parseForest) {
        disambiguate(parseForest, false, false, new HashMap<>());
    }
}
