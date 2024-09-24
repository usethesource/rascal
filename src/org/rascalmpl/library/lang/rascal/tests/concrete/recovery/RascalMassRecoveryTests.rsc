module lang::rascal::tests::concrete::recovery::RascalMassRecoveryTests

import lang::rascal::\syntax::Rascal;
import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import IO;
import ParseTree;

start[Module] (value input, loc origin) rascalParser = parser(#start[Module], allowAmbiguity=true, allowRecovery=true);
str source = readFile(|std:///lang/rascal/vis/ImportGraph.rsc|);

bool testRascalDeletions() {
    TestStats stats = testSingleCharDeletions(rascalParser, source);
    printStats(stats);
    return true;
}