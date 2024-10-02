module lang::rascal::tests::concrete::recovery::bugs::InfiniteLoopBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testInfiniteLoop1() {
    str input = readFile(|std:///lang/rascal/tests/concrete/recovery/bugs/InfiniteLoopInput.txt|);
    p = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    println("starting parse");
    p(input, |unknown:///?visualize=false|);
}

void testInfiniteLoop2() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    input = readFile(|std:///analysis/m3/FlowGraph.rsc|);
    testDeleteUntilEol(standardParser, recoveryParser, input, 200, 100);
}
