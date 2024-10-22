module lang::rascal::tests::concrete::recovery::bugs::MultiErrorBug


import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import util::ErrorRecovery;
import List;
import vis::Text;

bool multiErrorBug() {
    str input = readFile(|std:///lang/rascal/tests/concrete/recovery/bugs/MultiErrorBugInput.txt|);
    Tree t = parser(#start[Module], allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=false|);

    println(prettyTree(t));

    list[Tree] errors = findAllErrors(t);
    println("<size(errors)> Errors");
    for (Tree error <- errors) {
        Tree skipped = getSkipped(error);
        println("  <skipped@\loc>: <getErrorText(error)>");
    }
    Tree disambiguated = disambiguateErrors(t);
    list[Tree] disambiguatedErrors = findAllErrors(disambiguated);
    println("After disambiguating:");
    println("<size(disambiguatedErrors)> Errors");
    for (Tree error <- disambiguatedErrors) {
        Tree skipped = getSkipped(error);
        println("  <skipped@\loc>: <getErrorText(error)>");
    }
    return true;
}

