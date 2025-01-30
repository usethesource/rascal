module lang::rascal::tests::concrete::recovery::bugs::PreludeOutOfMemoryBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///Prelude.rsc|;
    input = readFile(source);
    testSingleCharDeletions(standardParser, recoveryParser, source, input, 200, 150, begin=1312, end=1313);
}
