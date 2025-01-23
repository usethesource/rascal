module lang::rascal::tests::concrete::recovery::bugs::TypeOutOfMemoryBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import util::Benchmark;
import util::ErrorRecovery;
import String;

int timeoutLimit = 0;

void testTypeBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, filters={timeoutFilter});
    loc source = |std:///lang/rascal/tests/library/Type.rsc|;
    input = readFile(source);
    setTimeout(realTime() + 1000);
    loc statLoc = |memory://test-tmp/test.stats|;
    writeFile(statLoc, "");
    RecoveryTestConfig config = recoveryTestConfig(statFile=statLoc, memoVerificationTimeout=2000, abortOnNoMemoTimeout=false);
    testSingleCharDeletions(config, standardParser, recoveryParser, source, input, 200, 150, begin=392, end=392);
    //testSingleCharDeletions(config, standardParser, recoveryParser, source, input, 200, 150);

    println(readFile(statLoc));
}
