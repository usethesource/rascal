module lang::rascal::tests::concrete::recovery::bugs::HtmlAstSlow

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::ParseErrorRecovery;
import util::Benchmark;
import String;
import IO;

bool testHtmlAst() {
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true, maxAmbDepth=2);
    //loc source = |home:///input/rascal/src/org/rascalmpl/library/lang/html/AST.rsc?deletedUntilEol=131:4688:4690|;
    loc source = |home:///input/rascal/src/org/rascalmpl/library/lang/html/AST.rsc?deletedUntilEol=133:4695:4716|;
    str input = getTestInput(source);
    //println("input: <input>");
    int startTime = realTime();
    Tree result = char(0);
    for (i <- [1..10]) {
        try {
            result = recoveryParser(input, source);
        } catch ParseError(_): {
            println("ParseError");
        }
    }
    println("duration: <realTime() - startTime>");

    println("total nodes: <countTreeNodes(result)>");
    println("unique nodes: <countUniqueTreeNodes(result)>");

    return true;
}
