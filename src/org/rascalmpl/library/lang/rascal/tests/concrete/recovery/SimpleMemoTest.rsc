module lang::rascal::tests::concrete::recovery::SimpleMemoTest

import ParseTree;
import vis::Text;
import IO;
import util::ErrorRecovery;

//lexical S = "^" A "$";

//lexical A = "a";

lexical S = A ;

lexical A = A1*;

lexical A1 = "a"?;

lexical A2 = "a";


void testMemo() {
    str input = "a";
    //str input = "bcbcbcccbb$";
    Tree t1 = parse(#S, input, |unknown:///|, allowAmbiguity=true);
    Tree t2 = parse(#S, input, |unknown:///?parse-memoization=none&visualize-parse-result|, allowAmbiguity=true);
    println(prettyTree(t1));
    println(prettyTree(t2));

    if (treeEquality(t1, t2)) {
        println("equal");
    } else {
        println("NOT EQUAL");
    }
}
