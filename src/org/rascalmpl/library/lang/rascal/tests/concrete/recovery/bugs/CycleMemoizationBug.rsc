module lang::rascal::tests::concrete::recovery::bugs::CycleMemoizationBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import String;
import vis::Text;
import util::ErrorRecovery;
import util::Benchmark;

/**
* Originally memoization inside cycles was turned off. This caused this test to take a long time and then crash with an out-of-memory error.
* With the new safe node memoization this test should run fine.
*/
void testCycleMemoizationFailure() {
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///lang/aterm/syntax/ATerm.rsc?parse-memoization=safe-node|;

    input = readFile(source);
    modifiedInput = substring(input, 0, 369) + substring(input, 399);

    begin = realTime();
    Tree tree = recoveryParser(modifiedInput, source);
    duration = realTime() - begin;
    println("with memoization duration: <duration>");

    println("Total nodes: <countTreeNodes(tree)>");
    println("Max shared nodes: <countUniqueTreeNodes(maximallyShareTree(tree))>");
    println("Unique nodes: <countUniqueTreeNodes(tree)>");

    assert "<tree>" == modifiedInput;
}
