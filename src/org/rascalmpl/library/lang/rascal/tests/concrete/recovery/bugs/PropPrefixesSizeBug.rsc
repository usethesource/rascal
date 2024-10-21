module lang::rascal::tests::concrete::recovery::bugs::PropPrefixesSizeBug


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testPropPrefixesSizeBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc src = |std:///lang/rascal/tests/concrete/recovery/bugs/PropPrefixesSizeBugInput.txt|;
    input = readFile(src);
    testDeleteUntilEol(standardParser, recoveryParser, src, input, 200, 100, begin=15427, end=15459);
}
