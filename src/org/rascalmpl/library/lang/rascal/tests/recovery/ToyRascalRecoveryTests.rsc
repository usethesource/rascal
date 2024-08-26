module lang::rascal::tests::recovery::ToyRascalRecoveryTests

import lang::rascal::tests::recovery::ToyRascal;

import ParseTree;
import IO;

Tree parseToyRascal(str input, bool visualize=false) {
    Tree result = parser(#start[FunctionDeclaration], allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);
    list[Tree] errors = findAllErrors(result);
    if (errors != []) {
        println("Tree has <size(errors)> errors");
        for (error <- errors) {
            println("- <getErrorText(error)>");
        }

        Tree disambiguated = defaultErrorDisambiguationFilter(result);
        println("Best error: <getErrorText(findFirstError(disambiguated))>");
    }

    return result;
}

test bool toyRascalOk() {
    Tree t = parseToyRascal("f(){s;}");
    return !hasErrors(t);
}

test bool toyRascalMissingOpenParen() {
    Tree t = parseToyRascal("f){}", visualize=true);
    return hasErrors(t) && getErrorText(findFirstError(t)) == ")";
}

test bool toyRascalMissingCloseParen() {
    Tree t = parseToyRascal("f({}", visualize=true);
    return hasErrors(t) && getErrorText(findFirstError(t)) == ")";
}
