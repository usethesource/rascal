module lang::rascal::tests::concrete::recovery::bugs::MultiErrorPico

import lang::pico::\syntax::Main;
import ParseTree;
import IO;
import util::ErrorRecovery;
import List;
import vis::Text;

bool multiErrorPico() {
    str input = readFile(|std:///lang/rascal/tests/concrete/recovery/bugs/MultiErrorPicoInput.pico|);
    Tree t = parser(#start[Program], allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=true|);

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

