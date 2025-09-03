module lang::rascal::tests::concrete::recovery::ErrorLocationTests

import lang::rascal::\syntax::Rascal;
import ParseTree;
import vis::Text;
import IO;
import util::ParseErrorRecovery;

/**
A smoke test to see if parse error position reporting is done correctly.
The reported parse error location should be the point where the parser originally got stuck (just before the last '|' character)
even though error recovery results in a tree that skips the string "mn" before the actual parse error.
*/
void testLocation() {
    str src = "module X\n\ndata E=a()|id(str nm|bb();";

    Module m = parse(#Module, src, allowRecovery=true);
    list[Tree] errors = findBestParseErrors(m);
    println(prettyTree(errors[0]));
    assert errors[0].parseError == |unknown:///|(30,1,<3,20>,<3,21>);
}



