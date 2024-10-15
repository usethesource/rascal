module lang::rascal::tests::concrete::recovery::bugs::DisambiguationPerformanceTest


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testPerformance() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///lang/box/util/Box2Text.rsc|;
    input = readFile(source);
    FileStats stats = testDeleteUntilEol(standardParser, recoveryParser, source, input, 200, 100, begin=17496, end=17496);
    println("<stats>");
}
