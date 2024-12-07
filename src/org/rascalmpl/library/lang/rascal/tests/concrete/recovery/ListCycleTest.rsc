module lang::rascal::tests::concrete::recovery::ListCycleTest

import ParseTree;
import vis::Text;
import IO;
import util::ErrorRecovery;

syntax S = T?*;

syntax T = X T? | "$";

syntax X = "b"?;

void testListCycles() {
    str input = "b$";
    Tree t1 = parse(#S, input, |unknown:///|, allowAmbiguity=true);
    Tree t2 = parse(#S, input, |unknown:///?disable-memoization=true|, allowAmbiguity=true);
    println(prettyTree(t1));
    println(prettyTree(t2));

    if (treeEquality(t1, t2)) {
        println("equal");
    } else {
        println("NOT EQUAL");
    }
}
