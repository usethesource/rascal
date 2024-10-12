module lang::rascal::tests::concrete::recovery::bugs::AfterEof


import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

void testEof() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///lang/rascal/tests/concrete/recovery/bugs/AfterEofInput.txt|;
    input = readFile(source);
    Tree t = recoveryParser(input, source);
    println("<t>");
}
