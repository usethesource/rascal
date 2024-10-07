package org.rascalmpl.parser.gtd.recovery;

import java.util.HashMap;
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

public class ParseErrorDisambiguator {
    private final IRascalValueFactory rascalValues;

    public ParseErrorDisambiguator(IRascalValueFactory rascalValues) {
        super();
        this.rascalValues = rascalValues;
    }

    private static class ScoredTree {
        public final IConstructor tree;
        public final int score;

        public ScoredTree(IConstructor tree, int score) {
            this.tree = tree;
            this.score = score;
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
        return disambiguate(arg, allowAmbiguity.getValue(), new HashMap<>()).tree;
    }

    private ScoredTree disambiguate(IConstructor tree, boolean allowAmbiguity, Map<IConstructor, ScoredTree> processedTrees) {
        ScoredTree result = processedTrees.get(tree);
        if (result != null) {
            return result;
    }

        Type type = tree.getConstructorType();

        if (type == RascalValueFactory.Tree_Appl) {
            result = disambiguateAppl((ITree) tree, allowAmbiguity, processedTrees);
        } else if (type == RascalValueFactory.Tree_Amb) {
            result = disambiguateAmb((ITree) tree, allowAmbiguity, processedTrees);
        } else {
            // Other trees (cycle, char) do not have subtrees so they have a score of 0
            result = new ScoredTree(tree, 0);
        }

        processedTrees.put(tree, result);

        return result;
    }

    private ScoredTree disambiguateAppl(ITree appl, boolean allowAmbiguity, Map<IConstructor, ScoredTree> processedTrees) {
        if (ProductionAdapter.isSkipped(appl.getProduction())) {
            return new ScoredTree(appl, ((IList) appl.get(1)).length());
        }

        IList args = TreeAdapter.getArgs(appl);
        int totalScore = 0;
        IListWriter disambiguatedArgs = null;

        // Disambiguate and score all children
        for (int i=0; i<args.size(); i++) {
            IValue arg = args.get(i);
            ScoredTree disambiguatedArg = disambiguate((IConstructor) arg, allowAmbiguity, processedTrees);
            totalScore += disambiguatedArg.score;
            if (disambiguatedArg.tree != arg && disambiguatedArgs == null) {
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
        ITree resultTree;
        if (disambiguatedArgs != null) {
            // Some arguments have changed
            resultTree = TreeAdapter.setArgs(appl, disambiguatedArgs.done());
        } else {
            // None of the arguments have changed
            resultTree = appl;
        }

        return new ScoredTree(resultTree, totalScore);
    }

    private ScoredTree disambiguateAmb(ITree amb, boolean allowAmbiguity, Map<IConstructor, ScoredTree> processedTrees) {
        ISet originalAlts = (ISet) amb.get(0);

        ISetWriter alternativesWithoutErrors = null;
        ScoredTree errorAltWithBestScore = null;
        for (IValue alt : originalAlts) {
            ScoredTree disambiguatedAlt = disambiguate((IConstructor) alt, allowAmbiguity, processedTrees);
            if (disambiguatedAlt.score == 0) {
                // Non-error tree
                if (alternativesWithoutErrors == null) {
                    alternativesWithoutErrors = rascalValues.setWriter();
                }
                alternativesWithoutErrors.insert(disambiguatedAlt.tree);
            } else {
                // Only keep the best of the error trees
                if (errorAltWithBestScore == null || errorAltWithBestScore.score > disambiguatedAlt.score) {
                    errorAltWithBestScore = disambiguatedAlt;
                }
            }
        }

        if (alternativesWithoutErrors == null) {
            assert errorAltWithBestScore != null : "No trees with and no trees without errors?";
            return errorAltWithBestScore;
        }
        
        ISet remainingAlts = alternativesWithoutErrors.done();

        ITree resultTree;
        if (remainingAlts.size() == originalAlts.size()) {
            // All children are without errors, return the original tree
            resultTree = amb;
        } else if (remainingAlts.size() == 1) {
            // One child without errors remains, dissolve the amb tree
            resultTree = (ITree) remainingAlts.iterator().next();
        } else {
            // Create a new amb tree with the remaining non-error trees
            resultTree = rascalValues.amb(remainingAlts);

            // We have an ambiguity between non-error trees
            if (!allowAmbiguity) {
                throw new Ambiguous(resultTree);
            }
        }

        return new ScoredTree(resultTree, 0);
    }

}
