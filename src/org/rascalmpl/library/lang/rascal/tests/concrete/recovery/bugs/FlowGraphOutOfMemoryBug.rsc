module lang::rascal::tests::concrete::recovery::bugs::FlowGraphOutOfMemoryBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import util::Benchmark;
import util::ErrorRecovery;
import String;

void testFlowGraphBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///analysis/m3/FlowGraph.rsc|;
    input = readFile(source);
    // Resulted in extremely long runtime and eventually an out-of-memory exception
    testSingleCharDeletions(standardParser, recoveryParser, source, input, 200, 150, begin=387, end=387);
}
