/**
 * Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

module lang::rascal::tests::concrete::recovery::RascalRecoveryTests

import lang::rascal::\syntax::Rascal;

import ParseTree;
import IO;
import util::Maybe;

bool debugging = false;

Tree parseRascal(type[&T] t, str input, bool visualize=false) {
    Tree result = parser(t, allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);
    if (debugging) {
    list[Tree] errors = findAllErrors(result);
    if (errors != []) {
        println("Tree has <size(errors)> errors");
        for (error <- errors) {
            println("- <getErrorText(error)>");
        }

        println("Best error: <getErrorText(findBestError(result).val)>");
    }
    }

    return result;
}

Tree parseRascal(str input, bool visualize=false) = parseRascal(#start[Module], input, visualize=visualize);

Tree parseFunctionDeclaration(str input, bool visualize=false) = parseRascal(#FunctionDeclaration, input, visualize=visualize);

Tree parseStatement(str input, bool visualize=false) = parseRascal(#Statement, input, visualize=visualize);

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

    return getErrorText(findFirstError(t)) == "x 1;";
}

test bool rascalIllegalStatement() {
    Tree t = parseRascal("module A void f(){a}");
    return getErrorText(findFirstError(t)) == "a}";
}

test bool rascalMissingCloseParen() {
    Tree t = parseRascal("module A void f({} void g(){}");

    assert getErrorText(findFirstError(t)) == "void g(";
    assert getErrorText(findBestError(t).val) == "(";

    return true;
}

test bool rascalFunctionDeclarationMissingCloseParen() {
    Tree t = parseFunctionDeclaration("void f({} void g() {}");

    assert getErrorText(findFirstError(t)) == "void g(";

    Tree error = findBestError(t).val;
    assert getErrorText(error) == "(";
    loc location = getSkipped(error).src;
    assert location.begin.column == 16 && location.length == 1;

    return true;
}

test bool rascalIfMissingExpr() {
    Tree t = parseFunctionDeclaration("void f(){if(){1;}}", visualize=false);
    return getErrorText(findBestError(t).val) == ")";
}

test bool rascalIfBodyEmpty() {
    Tree t = parseRascal("module A void f(){1;} void g(){if(1){}} void h(){1;}");

    println("error: <getErrorText(findFirstError(t))>");
    assert getErrorText(findBestError(t).val) == "} void h(){1";

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

test bool rascalIfMissingSemi() {
    Tree t = parseRascal("module A void f(){if (true) {a}}");

    println("error text: <getErrorText(findFirstError(t))>");
    return getErrorText(findFirstError(t)) == ";";
}
*/