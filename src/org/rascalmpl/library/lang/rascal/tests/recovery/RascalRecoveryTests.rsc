module lang::rascal::tests::recovery::RascalRecoveryTests

import lang::rascal::\syntax::Rascal;

import ParseTree;
import IO;

Tree parseRascal(str input, bool visualize=false) {
    Tree result = parser(#start[Module], allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);
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

Tree parseFunctionDeclaration(str input, bool visualize=false) {
    Tree result = parser(#FunctionDeclaration, allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);
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

test bool rascalOk() {
    Tree t = parseRascal("
    module A

    int inc(int i) {
        return i+1;
    }
    ");
    return !hasErrors(t);
}

test bool rascalFunctionDeclarationOk() {
    Tree t = parseFunctionDeclaration("void f(){}");
    return !hasErrors(t);
}


test bool rascalModuleFollowedBySemi() {
    Tree t = parseRascal("
    module A
    ;
    ");

    // There are a lot of productions in Rascal that have a ; as terminator.
    // The parser assumes the user has only entered the ; on one of them,
    // so the error list contains them all.
    list[Tree] errors = findAllErrors(t);
    assert size(errors) == 10;

    return getErrorText(findFirstError(t)) == ";";
}

test bool rascalOperatorTypo() {
    Tree t = parseRascal("
    module A

    int f() = 1 x 1;
    ");

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == "x 1";
}

test bool rascalIllegalStatement() {
    Tree t = parseRascal("module A void f(){a}");

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == "a}";
}

test bool rascalMissingCloseParen() {
    Tree t = parseRascal("module A void f({} void g(){}");

    assert getErrorText(findFirstError(t)) == "void g(";
    assert getErrorText(findFirstError(defaultErrorDisambiguationFilter(t))) == "(";

    return true;
}

test bool rascalFunctionDeclarationMissingCloseParen() {
    Tree t = parseFunctionDeclaration("void f({} void g() {}", visualize=false);

    assert getErrorText(findFirstError(t)) == "void g(";

    Tree error = findFirstError(defaultErrorDisambiguationFilter(t));
    assert getErrorText(error) == "(";
    Tree skipped = getSkipped(error);
    loc location = getSkipped(error).src;
    assert location.begin.column == 16 && location.length == 1;

    return true;
}

// Not working yet:
/*
test bool rascalMissingOpeningParen() {
    Tree t = parseRascal("module A void f){} void g() { }");

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == "a}";
}

test bool rascalFunFunMissingCloseParen() {
    Tree t = parseRascal("module A void f(){void g({}} void h(){}");

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == "a}";
}

test bool rascalIfMissingExpr() {
    Tree t = parseRascal("module A void f(){if(){}} 1;", visualize=false);

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == ";";
}

test bool rascalIfMissingOpeningParen() {
    Tree t = parseRascal("module A void f(){if 1){}}", visualize=false);

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == ";";
}

test bool rascalIfMissingCloseParen() {
    Tree t = parseRascal("module A void f(){if(1{}}", visualize=false);

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == ";";
}

test bool rascalIfEmptyBody() {
    Tree t = parseRascal("module A void f(){if(1){}} 1;");

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == ";";
}

test bool rascalIfMissingSemi() {
    Tree t = parseRascal("module A void f(){if (true) {a}}");

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == ";";
}
*/
