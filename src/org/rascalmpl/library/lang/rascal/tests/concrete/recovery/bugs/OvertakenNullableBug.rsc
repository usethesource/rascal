module lang::rascal::tests::concrete::recovery::bugs::OvertakenNullableBug


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    input = readFile(|std:///lang/rascal/tests/library/analysis/statistics/DescriptiveTests.rsc|);
    testDeleteUntilEol(standardParser, recoveryParser, input, 200, 100, begin=561, end=561);
}
