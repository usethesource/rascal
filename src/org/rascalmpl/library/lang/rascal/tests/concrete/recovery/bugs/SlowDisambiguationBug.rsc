module lang::rascal::tests::concrete::recovery::bugs::SlowDisambiguationBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    input = readFile(|std:///lang/rascal/grammar/tests/TestGrammars.rsc|);
    testDeleteUntilEol(standardParser, recoveryParser, input, 200, 100, begin=278, end=278);
}

