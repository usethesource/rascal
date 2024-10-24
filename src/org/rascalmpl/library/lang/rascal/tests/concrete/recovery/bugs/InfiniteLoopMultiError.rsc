module lang::rascal::tests::concrete::recovery::bugs::InfiniteLoopMultiError


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testInfiniteLoopMultiError() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    source = |std:///Exception.rsc|;
    input = readFile(source);
    testDeleteUntilEol(standardParser, recoveryParser, source, input, 200, 100, begin=662, end=664);
}
