module lang::rascal::tests::concrete::recovery::bugs::SlowExceptionBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///Exception.rsc|;
    input = readFile(source);
    //testSingleCharDeletions(standardParser, recoveryParser, source, input, 200, 150, begin=1744, end=1744);
    testSingleCharDeletions(standardParser, recoveryParser, source, input, 200, 150);
}
