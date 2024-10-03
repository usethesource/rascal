module lang::rascal::tests::concrete::recovery::bugs::LostSkipBug


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    input = readFile(|std:///analysis/diff/edits/ExecuteTextEdits.rsc|);
    testSingleCharDeletions(standardParser, recoveryParser, input, 200, 100, begin=235, end=235);
}
