
/**
 * Copyright (c) 2025, NWO-I Centrum Wiskunde & Informatica (CWI)
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
