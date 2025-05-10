module lang::rascal::tests::concrete::recovery::bugs::FCASlow

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::ParseErrorRecovery;
import String;
import IO;

/**
This tests the slowest error recovery in all tests < 10kb
*/
bool testFCA() {
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, maxAmbDepth=1000);
    loc source = |std:///analysis/formalconcepts/FCA.rsc?deletedUntilEol=54:2242:2261|;
    str input = getTestInput(source);
    //println("input: <input>");
    Tree result = recoveryParser(input, source);
    for (i <- [1..2])
        recoveryParser(input, source);
    println("total nodes: <countTreeNodes(result)>");
    println("unique nodes: <countUniqueTreeNodes(result)>");
    assert input == "<result>";

    return true;
}
