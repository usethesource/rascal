package org.rascalmpl.library.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Objects;

import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.parser.util.DebugUtil;
import org.rascalmpl.parser.util.ParseStateVisualizer;
import org.rascalmpl.unicode.UnicodeOutputStreamWriter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.util.visualize.dot.DotGraph;
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

public class ErrorRecovery {
    private final IRascalValueFactory rascalValues;

    public ErrorRecovery(IRascalValueFactory rascalValues) {
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
        Type type = tree.getConstructorType();
        ScoredTree result;

        if (type == RascalValueFactory.Tree_Appl) {
            result = disambiguateAppl((ITree) tree, allowAmbiguity, processedTrees);
        } else if (type == RascalValueFactory.Tree_Amb) {
            result = disambiguateAmb((ITree) tree, allowAmbiguity, processedTrees);
        } else {
            // Other trees (cycle, char) do not have subtrees so they have a score of 0
            result = new ScoredTree(tree, 0);
        }

        return result;
    }

    private ScoredTree disambiguateAppl(ITree appl, boolean allowAmbiguity, Map<IConstructor, ScoredTree> processedTrees) {
        ScoredTree result = processedTrees.get(appl);
        if (result != null) {
            return result;
        }

        if (ProductionAdapter.isSkipped(appl.getProduction())) {
            result = new ScoredTree(appl, ((IList) appl.get(1)).length());
        } else {
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

            result = new ScoredTree(resultTree, totalScore);
        }

        processedTrees.put(appl, result);

        return result;
    }

    private ScoredTree disambiguateAmb(ITree amb, boolean allowAmbiguity, Map<IConstructor, ScoredTree> processedTrees) {
        ScoredTree result = processedTrees.get(amb);
        if (result != null) {
            return result;
        }

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
            processedTrees.put(amb, errorAltWithBestScore);
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

        result = new ScoredTree(resultTree, 0);
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

        public boolean equals(Object peer) {
            TreePair pair = (TreePair) peer;
            return tree1 == pair.tree1 && tree2 == pair.tree2;
        }

        public int hashCode() {
            return hashCode;
        }
    };

    public IBool treeEquality(IConstructor tree1, IConstructor tree2) {
        return rascalValues.bool(checkTreeEquality((ITree) tree1, (ITree) tree2, new HashSet<>(), true));
    }

    private boolean checkTreeEquality(ITree tree1, ITree tree2, Set<TreePair> equalTrees, boolean logInequality) {
        if (tree1 == tree2) {
            return true;
        }

        Type type = tree1.getConstructorType();

        if (!type.equals(tree2.getConstructorType())) {
            if (logInequality) {
                System.out.println("types not equal");
            }
            return false;
        }

        if (!checkLocationEquality(tree1, tree2)) {
            if (logInequality) {
                System.out.println("locations not equal");
            }
            return false;
        }

        if (type == RascalValueFactory.Tree_Char) {
            if (checkCharEquality(tree1, tree2)) {
                return true;
            }
        
            if (logInequality) {
                System.out.println("char nodes not equal");
            }

            return false;
        } else if (type == RascalValueFactory.Tree_Cycle) {
            if (checkCycleEquality(tree1, tree2)) {
                return true;
            }

            if (logInequality) {
                System.out.println("char nodes not equal");
            }

            return false;
        }

        TreePair pair = new TreePair(tree1, tree2);
        if (equalTrees.contains(pair)) {
            return true;
        }

        boolean result;
        if (type == RascalValueFactory.Tree_Appl) {
            result = checkApplEquality(tree1, tree2, equalTrees, logInequality);
            if (logInequality && !result) {
                System.out.println("appls not equal");
            }
        } else if (type == RascalValueFactory.Tree_Amb) {
            result = checkAmbEquality(tree1, tree2, equalTrees, logInequality);
            if (logInequality && !result) {
                System.out.println("ambs not equal");
            }
        } else {
            throw new IllegalArgumentException("unknown tree type: " + type);
        }

        if (result) {
            equalTrees.add(pair);
        }

        return result;
    }

    private boolean checkApplEquality(ITree tree1, ITree tree2, Set<TreePair> equalTrees, boolean logInequality) {
        IConstructor type1 = ProductionAdapter.getType(tree1);
        IConstructor type2 = ProductionAdapter.getType(tree2);

        if (!type1.equals(type2)) {
            if (logInequality) {
                System.out.println("types do not match: " + DebugUtil.prodToString(type1) + " != " + DebugUtil.prodToString(type2));
            }
            return false;
        }

        IList args1 = TreeAdapter.getArgs(tree1);
        IList args2 = TreeAdapter.getArgs(tree2);
        int size = args1.size();
        if (size != args2.size()) {
            if (logInequality) {
                System.out.println("argument count mismatch");
            }
            return false;
        }

        for (int i = 0; i < size; i++) {
            ITree arg1 = (ITree) args1.get(i);
            ITree arg2 = (ITree) args2.get(i);
            if (!checkTreeEquality(arg1, arg2, equalTrees, logInequality)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkAmbEquality(ITree tree1, ITree tree2, Set<TreePair> equalTrees, boolean logInequality) {
        ISet alts1 = tree1.getAlternatives();
        ISet alts2 = tree2.getAlternatives();

        if (alts1.size() != alts2.size()) {
            if (logInequality) {
                System.out.println("amb nodes do not have an equal size");
            }
            return false;
        }

        BitSet alts2Checked = new BitSet(alts2.size());

        boolean ok = true;

        IValue missingAlt = null;

        for (IValue alt1 : alts1) {
            int index2 = 0;
            // This relies on iteration order being stable, which is a pretty safe bet.
            for (IValue alt2 : alts2) {
                if (!alts2Checked.get(index2) && checkTreeEquality((ITree) alt1, (ITree) alt2, equalTrees, false)) {
                    alts2Checked.set(index2);
                    break;
                }

                index2++;
            }

            if (index2 == alts2.size()) {
                // We did not find alt1 in alts2
                if (logInequality) {
                    System.out.println("amb nodes not equal");
                    System.err.println("  rhs amb node not found: " + index2);
                    missingAlt = alt1;
                }
                ok = false;
            }
        }

        if (!ok && logInequality && missingAlt != null) {
            int index2 = 0;
            for (IValue alt2 : alts2) {
                if (!alts2Checked.get(index2)) {
                    System.err.println("  lhs amb node not found: " + index2);
                    checkTreeEquality((ITree) missingAlt, (ITree) alt2, equalTrees, true);
                    System.err.println();
                }
                index2++;
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

    public void parseTree2Dot(IConstructor tree, ISourceLocation dotFile) {
        ParseStateVisualizer visualizer = new ParseStateVisualizer("ParseTree");
        DotGraph graph = visualizer.createGraph((ITree) tree);

        URIResolverRegistry reg = URIResolverRegistry.getInstance();

        try {
            ISourceLocation dotLocation = reg.logicalToPhysical(dotFile);

            OutputStream outStream = reg.getOutputStream(dotLocation, false);
            try (UnicodeOutputStreamWriter out = new UnicodeOutputStreamWriter(outStream, "UTF-8", false)) {
                out.append(graph.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
