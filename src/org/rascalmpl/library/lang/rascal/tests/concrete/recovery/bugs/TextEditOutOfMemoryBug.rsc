module lang::rascal::tests::concrete::recovery::bugs::TextEditOutOfMemoryBug


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import util::Benchmark;

int timeoutLimit = 0;

void testTextEditBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, filters={timeoutFilter});
    loc source = |std:///analysis/diff/edits/TextEdits.rsc|;
    input = readFile(source);
    setTimeout(realTime() + 1000);
    testSingleCharDeletions(standardParser, recoveryParser, source, input, 200, 150, begin=3367, end=3367);
}
