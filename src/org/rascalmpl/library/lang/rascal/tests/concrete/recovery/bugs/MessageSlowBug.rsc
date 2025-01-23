module lang::rascal::tests::concrete::recovery::bugs::MessageSlowBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import util::Benchmark;
import util::ErrorRecovery;
import String;

int timeoutLimit = 0;

void testMessageBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, filters={timeoutFilter});
    loc source = |std:///Message.rsc|;

    input = readFile(source);
    modifiedInput = substring(input, 0, 996) + substring(input, 997);

    begin = realTime();
    Tree memoTree = recoveryParser(modifiedInput, source);
    println("with memoization duration: <realTime() - begin>");
    println("Total nodes: <countTreeNodes(memoTree)>");
    println("Max shared nodes: <countUniqueTreeNodes(maximallyShareTree(memoTree))>");
    println("Unique nodes: <countUniqueTreeNodes(memoTree)>");
}
