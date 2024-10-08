module lang::rascal::tests::concrete::recovery::bugs::OutOfMemoryBug


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///lang/rascal/tests/functionality/PatternSet3.rsc|;
    input = readFile(source);
    testDeleteUntilEol(standardParser, recoveryParser, source, input, 200, 150, begin=581, end=581);
}
