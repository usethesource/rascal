module lang::rascal::tests::concrete::recovery::CycleTest2

import ParseTree;
import vis::Text;
import IO;
import util::ErrorRecovery;

lexical S = A | B | "a";

lexical A = B;

lexical B = S;

void testCycles() {
    str input = "a";
    Tree t1 = parse(#S, input, |unknown:///?parse-memoization=safe-node|, allowAmbiguity=true);
    Tree t2 = parse(#S, input, |unknown:///?parse-memoization=none&visualize-parse-result|, allowAmbiguity=true);
    println(prettyTree(t1));
    println(prettyTree(t2));

    if (treeEquality(t1, t2)) {
        println("equal");
    } else {
        println("NOT EQUAL");
    }
}
