module lang::rascal::tests::concrete::recovery::bugs::NoErrorsAfterDisambBug


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    input = readFile(|std:///lang/rascal/tests/basic/ListRelations.rsc|);
    testSingleCharDeletions(standardParser, recoveryParser, input, 200, 100, begin=1916, end=1916);
}
