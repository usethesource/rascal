
module lang::rascal::tests::concrete::recovery::bugs::Patterns3Slow

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::ParseErrorRecovery;
import util::Benchmark;
import String;
import IO;

/**
This tests the slowest error recovery in all tests < 10kb
*/
bool testPatterns3() {
    loc source = |std:///lang/rascal/tests/concrete/Patterns3.rsc?deletedUntilEol=85:2133:2136|;
    str input = getTestInput(source);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, maxRecoveryAttempts=50, maxRecoveryTokens=30);

    // First fast run to generate the parser
    recoveryParser(readFile(source), source);

    // Now time a regular parse
    int normalBegin = realTime();
    Tree tree = recoveryParser(readFile(source), source);

    int recoveryBegin = realTime();
    println("normal parse time: <recoveryBegin - normalBegin>");

    Tree result = recoveryParser(input, source);
    for (i <- [1..10]) {
        recoveryParser(input, source);
    }
    int end = realTime();
    println("recovery time: <end - recoveryBegin>");

    println("total nodes: <countTreeNodes(result)>");
    println("unique nodes: <countUniqueTreeNodes(result)>");
    return input == "<result>";
}

