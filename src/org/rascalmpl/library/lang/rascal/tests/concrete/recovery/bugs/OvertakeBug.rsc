module lang::rascal::tests::concrete::recovery::bugs::OvertakeBug

import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

bool testOvertakeBug() {
    str input = readFile(|std:///lang/rascal/tests/concrete/recovery/bugs/OvertakeBugInput.txt|);
    parser(#Module, allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=false|);
    return true;
}