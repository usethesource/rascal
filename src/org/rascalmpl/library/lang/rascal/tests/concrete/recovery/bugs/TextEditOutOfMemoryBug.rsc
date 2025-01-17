module lang::rascal::tests::concrete::recovery::bugs::TextEditOutOfMemoryBug


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import util::Benchmark;
import util::ErrorRecovery;
import String;

int timeoutLimit = 0;

void testTextEditBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, filters={timeoutFilter});
    loc source = |std:///analysis/diff/edits/TextEdits.rsc|;
    input = readFile(source);
    setTimeout(realTime() + 1000);
    loc statLoc = |memory://test-tmp/test.stats|;
    writeFile(statLoc, "");
    testSingleCharDeletions(standardParser, recoveryParser, source, input, 200, 150, begin=3367, end=3367, statFile=statLoc);

    println(readFile(statLoc));
}

test bool testPostParseFilterBug() {
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    //loc source = |std:///lang/rascal/tests/concrete/PostParseFilter.rsc?visualize-parse-result|;
    loc source = |std:///lang/rascal/tests/concrete/PostParseFilter.rsc|;
    loc noMemoSource = source[query = "?parse-memoization=none"];

    input = readFile(source);
    modifiedInput1 = substring(input, 0, 298) + substring(input, 299);

    modifiedInput2 = readFile(|std:///lang/rascal/tests/concrete/recovery/bugs/SafeMemoizationBug.txt|);

    modifiedInput = modifiedInput1;
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