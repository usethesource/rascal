module lang::rascal::tests::concrete::recovery::CycleTest3

lexical S = A | B | "a";

lexical A = B;

lexical B = S;

import ParseTree;
import IO;
import vis::Text;
import util::ErrorRecovery;

void testCycles3() {
    str input = "a";

    Tree noMemoTree = parse(#S, input, |unknown:///?parse-memoization=none|, allowAmbiguity=true);
    Tree safeNodeMemoTree = parse(#S, input, |unknown:///?parse-memoization=safe-node|, allowAmbiguity=true);

    println("Tree without memoization:");
    println(prettyTree(noMemoTree));

    print("Safe node memoization: ");
    if (treeEquality(noMemoTree, safeNodeMemoTree)) {
        println("correct");
    } else {
        println("INCORRECT");
    }

}
