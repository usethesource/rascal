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

module lang::rascal::tests::concrete::recovery::ToyRascalRecoveryTests

import lang::rascal::tests::concrete::recovery::ToyRascal;

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

        println("Best error: <getErrorText(findBestError(result))>");
    }

    return result;
}

test bool toyRascalOk() {
    Tree t = parseToyRascal("f(){s;}");
    return !hasErrors(t);
}

test bool toyRascalMissingOpenParen() {
    Tree t = parseToyRascal("f){}", visualize=true);
    return hasErrors(t) && getErrorText(findBestError(t)) == ")";
}

test bool toyRascalMissingCloseParen() {
    Tree t = parseToyRascal("f({}", visualize=true);
    return hasErrors(t) && getErrorText(findBestError(t)) == "(";
}

test bool toyRascalMissingIfBody() {
    Tree t = parseToyRascal("f(){if(1){}}", visualize=true);
    return hasErrors(t) && getErrorText(findBestError(t)) == "}";
}