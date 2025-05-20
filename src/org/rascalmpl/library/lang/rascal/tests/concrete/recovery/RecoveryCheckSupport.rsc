/**
 * Copyright (c) 2024-2025, NWO-I Centrum Wiskunde & Informatica (CWI)
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

module lang::rascal::tests::concrete::recovery::RecoveryCheckSupport

import ParseTree;
import util::ParseErrorRecovery;
import String;
import IO;
import Grammar;
import analysis::statistics::Descriptive;
import util::Math;
import Set;
import List;
import vis::Text;

bool checkRecovery(type[&T<:Tree] begin, str input, list[str] expectedErrors, bool visualize=false) {
    Tree t = parser(begin, allowRecovery=true, allowAmbiguity=true, maxRecoveryTokens=1000, maxRecoveryAttempts=1000)(input, |unknown:///?visualize=<"<visualize>">|);
    return checkErrors(t, expectedErrors);
}

// Print a list of errors
void printErrors(list[Tree] errors) {
    for (Tree error <- errors) {
        println("\'<getErrorText(error)>\'");
    }
}

// Check a tree contains exactly the expected error
bool checkError(Tree t, str expectedError) = checkErrors(t, [expectedError]);

// Check if a tree contains exactly the expected errors
bool checkErrors(Tree t, list[str] expectedErrors) {
    list[Tree] errors = findBestParseErrors(t);
    if (size(errors) != size(expectedErrors)) {
        println("Expected <size(expectedErrors)> errors, found <size(errors)>");
        printErrors(errors);
        return false;
    }

    for (error <- errors) {
        str errorText = getErrorText(error);
        if (errorText notin expectedErrors) {
            println("Unexpected error: \'<errorText>\'");
            println("All errors found:");
            printErrors(errors);
            return false;
        }
    }

    return true;
}
