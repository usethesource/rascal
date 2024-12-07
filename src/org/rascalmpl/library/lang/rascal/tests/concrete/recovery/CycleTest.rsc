module lang::rascal::tests::concrete::recovery::CycleTest

import ParseTree;
import vis::Text;
import IO;
import util::ErrorRecovery;

syntax S = T | U;

syntax T = X T? | "$";

syntax U = X T | "$";

syntax X = "b"? | "c";

void testCycles() {
    str input = "bc$";
    //str input = "bcbcbcccbb$";
    Tree t1 = parse(#S, input, |unknown:///|, allowAmbiguity=true);
    Tree t1b = parse(#S, input, |unknown:///|, allowAmbiguity=true);
    Tree t2 = parse(#S, input, |unknown:///?disable-memoization=true|, allowAmbiguity=true);
    println(prettyTree(t1));
    println(prettyTree(t2));

    if (treeEquality(t1, t2)) {
        println("equal");
    } else {
        println("NOT EQUAL");
    }

    if (treeEquality(t1, t1b)) {
        println("t1 and t1b are tree-equal");
    } else {
        println("t1 and t1b NOT EQUAL");
    }

    if (t1 == t1b) {
        println("identical");
    } else {
        println("IDENTICAL PARSE CALLS BUT NOT IDENTICAL");
    }
}
