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

module lang::rascal::tests::concrete::recovery::BasicRecoveryTests

import ParseTree;
import util::ErrorRecovery;

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

layout Layout = [\ ]* !>> [\ ];

syntax S = T;

syntax T = ABC End;
syntax ABC = 'a' 'b' 'c';
syntax End = "$";

test bool basicOk() = checkRecovery(#S, "a b c $", []);

test bool abx() = checkRecovery(#S, "a b x $", ["x "]);

test bool axc() = checkRecovery(#S, "a x c $", ["x c"]);

test bool autoDisambiguation() {
    str input = "a x $";

    assert checkRecovery(#S, input, ["x "]);

    Tree autoDisambiguated = parser(#S, allowRecovery=true, allowAmbiguity=false)(input, |unknown:///|);
    return size(findAllErrors(autoDisambiguated)) == 1;
}
