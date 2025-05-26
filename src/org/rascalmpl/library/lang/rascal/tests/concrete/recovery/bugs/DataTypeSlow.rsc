module lang::rascal::tests::concrete::recovery::bugs::DataTypeSlow

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::ParseErrorRecovery;
import String;
import IO;
import util::Benchmark;

bool testDataType() {
    normalParser = parser(#start[Module], allowRecovery=false);
    recoveryParser = parser(#start[Module], allowRecovery=true, maxAmbDepth=1000);
    loc source = |std:///lang/rascal/tests/functionality/DataType.rsc?deletedUntilEol=253:8344:8395|;
    str input = getTestInput(source);
    //println("input: <input>");

    int begin = realTime();
    Tree normalParse = normalParser(readFile(source), source);
    int norrmalEnd = realTime();
    Tree result = recoveryParser(input, source);
    int recoveryEnd = realTime();
    println("normal parse time: <norrmalEnd - begin>");
    println("recovery parse time: <recoveryEnd - norrmalEnd>");
    
    for (i <- [1..2])
        recoveryParser(input, source);
    println("total nodes: <countTreeNodes(result)>");
    println("unique nodes: <countUniqueTreeNodes(result)>");
    assert input == "<result>";

    return true;
}


