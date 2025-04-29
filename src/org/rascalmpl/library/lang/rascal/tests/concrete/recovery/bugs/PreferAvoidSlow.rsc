module lang::rascal::tests::concrete::recovery::bugs::PreferAvoidSlow

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::ParseErrorRecovery;
import String;
import IO;

test bool testPreferAvoid() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, maxAmbDepth=1000);
    loc source = |std:///lang/sdf2/filters/PreferAvoid.rsc?deletedUntilEol=8:208:271|;
    str input = getTestInput(source);
    println("input: <input>");
    Tree result = recoveryParser(input, source);
    println("total nodes: <countTreeNodes(result)>");
    println("unique nodes: <countUniqueTreeNodes(result)>");
    assert input == "<result>";

    for (i <- [0..10]) {
        println("Pruning at level <i>");
        pruned = pruneAmbiguities(result, maxDepth=i);
        println("  total nodes at level <i>: <countTreeNodes(pruned)>");
        println("  unique nodes at level <i>: <countUniqueTreeNodes(pruned)>");
    }

    return true;
}
