module lang::rascal::tests::concrete::recovery::bugs::SlowExceptionBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import util::Benchmark;
import String;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///Exception.rsc|;
    input = readFile(source);
    int begin = realTime();
    str modifiedInput = substring(input, 0, 1744) + substring(input, 1745);
    Tree t = recoveryParser(modifiedInput, source);
    int duration = realTime() - begin;
    println("duration: <duration> ms.");
}
