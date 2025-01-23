module lang::rascal::tests::concrete::recovery::bugs::FlowGraphOutOfMemoryBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import util::Benchmark;
import util::ErrorRecovery;
import String;

int timeoutLimit = 0;

void testFlowGraphBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, filters={timeoutFilter});
    loc source = |std:///analysis/m3/FlowGraph.rsc|;
    input = readFile(source);
    setTimeout(realTime() + 1000);
    loc statLoc = |memory://test-tmp/test.stats|;
    writeFile(statLoc, "");
    testSingleCharDeletions(standardParser, recoveryParser, source, input, 200, 150, begin=387, end=387, statFile=statLoc);

    println(readFile(statLoc));
}
