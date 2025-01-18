module lang::rascal::tests::concrete::recovery::bugs::SafeMemoizationBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import String;
import vis::Text;
import util::ErrorRecovery;
import util::Benchmark;

test bool testSafeMemoizationFailure() {
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    //loc source = |std:///lang/rascal/tests/concrete/PostParseFilter.rsc?visualize-parse-result|;
    loc source = |std:///lang/rascal/tests/concrete/PostParseFilter.rsc|;
    loc noMemoSource = source[query = "?parse-memoization=none"];

    input = readFile(source);
    modifiedInput1 = substring(input, 0, 298) + substring(input, 299);

    modifiedInput2 = readFile(|std:///lang/rascal/tests/concrete/recovery/bugs/SafeMemoizationBug.txt|);

    if (modifiedInput1 != modifiedInput2) {
        println("input difference?");
    }

    modifiedInput = modifiedInput2;
    println("Modified input: <modifiedInput>");

    begin = realTime();
    Tree memoTree = recoveryParser(modifiedInput, source);
    println("with memoization duration: <realTime() - begin>");
    println("Total nodes: <countTreeNodes(memoTree)>");
    println("Max shared nodes: <countUniqueTreeNodes(maximallyShareTree(memoTree))>");
    println("Unique nodes: <countUniqueTreeNodes(memoTree)>");

    println();
    
    begin = realTime();
    Tree noMemoTree = recoveryParser(modifiedInput, noMemoSource);
    println("no memoization duration: <realTime() - begin>");
    println("Total nodes: <countTreeNodes(noMemoTree)>");
    println("Max shared nodes: <countUniqueTreeNodes(maximallyShareTree(noMemoTree))>");
    println("Unique nodes: <countUniqueTreeNodes(noMemoTree)>");

    return treeEquality(memoTree, noMemoTree);
}
