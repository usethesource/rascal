package org.rascalmpl.library.lang.rascal.tests.concrete.recovery;

import java.io.PrintWriter;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.utils.IResourceLocationProvider;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

public class ErrorRecovery {
	private final IValueFactory values;
	private final IRascalValueFactory rascalValues;
    private final PrintWriter out;
	private final TypeStore store;

    public ErrorRecovery(IValueFactory values, IRascalValueFactory rascalValues, PrintWriter out, TypeStore store, IRascalMonitor monitor, IResourceLocationProvider resourceProvider) {
		super();
		
		this.values = values;
		this.rascalValues = rascalValues;
		this.store = store;
		this.out = out;
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
     * Disambiguate error trees, faster implementation of the original Rascal variant:
Tree defaultErrorDisambiguationFilter(Tree t) {
  return visit(t) {
    case a:amb(_) => ambDisambiguation(a)
  };
}

private Tree ambDisambiguation(amb(set[Tree] alternatives)) {
  // Go depth-first
  rel[int score, Tree alt] scoredErrorTrees = { <scoreErrors(alt), alt> | Tree alt <- alternatives };
  set[Tree] nonErrorTrees = scoredErrorTrees[0];

  if (nonErrorTrees == {}) {
    return (getFirstFrom(scoredErrorTrees) | it.score > c.score ? c : it | c <- scoredErrorTrees).alt;
  }
  
  if ({Tree single} := nonErrorTrees) {
    // One ambiguity left, no ambiguity concerns here
    return single;
  }

  // Multiple non-error trees left, return an ambiguity node with just the non-error trees
  return amb(nonErrorTrees);
}

private int scoreErrors(Tree t) = (0 | it + getSkipped(e).src.length | /e:appl(error(_,_,_),_) := t);

// Handle char and cycle nodes
default Tree defaultErrorDisambiguationFilter(Tree t) = t;
     **/

    public IConstructor disambiguateErrors(IConstructor arg) {
        long start = System.currentTimeMillis();
        IConstructor result = disambiguate(arg).tree;
        long duration = System.currentTimeMillis() - start;
        System.err.println("disambiguateErrors took " + duration + " ms.");
        return result;
    }

    private ScoredTree disambiguate(IConstructor tree) {
        Type type = tree.getConstructorType();

		if (type == RascalValueFactory.Tree_Appl) {
            return disambiguateAppl((ITree) tree);
        } else if (type == RascalValueFactory.Tree_Amb) {
            return disambiguateAmb(tree);
        }

        return new ScoredTree(tree, 0);
    }

    private ScoredTree disambiguateAppl(ITree appl) {
        if (ProductionAdapter.isSkipped(appl.getProduction())) {
            return new ScoredTree(appl, ((IList) appl.get(1)).length());
        }

        IList args = TreeAdapter.getArgs(appl);
        int totalScore = 0;
        IValue[] disambiguatedArgs = new IValue[args.size()];

        int index=0;
        boolean anyChanges = false;

        // Disambiguate and score all children
        for (IValue arg : args) {
            ScoredTree disambiguatedArg = disambiguate((IConstructor) arg);
            totalScore += disambiguatedArg.score;
            disambiguatedArgs[index++] = disambiguatedArg.tree;
            if (disambiguatedArg.tree != arg) {
                anyChanges = true;
            }
        }

        // Only build a new tree if at least one of the arguments has changed
        ITree resultTree;
        if (anyChanges) {
            IListWriter argWriter = rascalValues.listWriter();
            for (IValue arg : disambiguatedArgs) {
                argWriter.append(arg);
            }
            resultTree = TreeAdapter.setArgs(appl, argWriter.done());
        } else {
            resultTree = appl;
        }


        return new ScoredTree(resultTree, totalScore);
    }

    private ScoredTree disambiguateAmb(IConstructor amb) {
        ISet alts = (ISet) amb.get(0);

        ISetWriter nonErrorAlts = null;
        ScoredTree bestErrorAlt = null;
        for (IValue alt : alts) {
            ScoredTree disambiguatedAlt = disambiguate((IConstructor) alt);
            if (disambiguatedAlt.score == 0) {
                // Non-error tree
                if (nonErrorAlts == null) {
                    nonErrorAlts = values.setWriter();
                }
                nonErrorAlts.insert(disambiguatedAlt.tree);
            } else {
                // Only keep the best of the error trees
                if (bestErrorAlt == null || bestErrorAlt.score > disambiguatedAlt.score) {
                    bestErrorAlt = disambiguatedAlt;
                }
            }
        }

        if (nonErrorAlts == null) {
            return bestErrorAlt;
        }
        
        ISet newAlts = nonErrorAlts.done();
        int altCount = newAlts.size();

        IConstructor resultTree;
        if (altCount == alts.size()) {
            // All children are without errors, return the original tree
            resultTree = amb;
        } else if (altCount == 1) {
            // One child without errors remains, dissolve the amb tree
            resultTree = (IConstructor)newAlts.iterator().next();
        } else {
            // Create a new amb tree with the remaining non-error trees
            resultTree = rascalValues.amb(newAlts);
        }

        return new ScoredTree(resultTree, 0);
    }

}
