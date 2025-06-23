@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::compile::Examples::Tst3
import IO;
import util::Reflective;
import lang::rascalcore::check::RascalConfig;
import lang::rascalcore::check::Checker;
import lang::rascalcore::check::RascalConfig;

void testRascalLSP(loc rascalLocation = |project://rascal|, loc lspLocation = |project://rascal-lsp|, bool cleanRascal = false) {
    rascalSource = rascalLocation + "src/org/rascalmpl/library";
    rascalTpls = |tmp:///rascal-tpls|;
    println(rascalTpls);
    pcfgRascal = pathConfig(
        projectRoot = rascalLocation,
        srcs = [rascalSource],
        bin = rascalTpls
    );

    lspSource = lspLocation + "src/main/rascal";
    lspTpls = |tmp:///lsp-tpls|;
    println(lspTpls);

    pcfgLsp = pathConfig(
        projectRoot = lspLocation,
        srcs = [lspSource],
        bin = lspTpls,
        libs=[rascalTpls]
    );

    rascalModules = {
        "util/Reflective.rsc",
        "analysis/diff/edits/TextEdits.rsc",
        "IO.rsc",
        "ParseTree.rsc",
        "Message.rsc",
        "util/IDEServices.rsc",
        "lang/pico/syntax/Main.rsc",
        "util/ParseErrorRecovery.rsc"
    };

    if (cleanRascal) {
        remove(rascalTpls, recursive=true);
    }
    println("Checking rascal modules needed for LSP");
    rascalCheckResult = check([rascalSource + m | m <- rascalModules],rascalCompilerConfig(pcfgRascal)[verbose=true]);
    println("Rascal messages:");
    for (/e:error(_,_) := rascalCheckResult) {
        println(e);
    }
    // make sure the tpls are fresh for lsp
    // it's fine to not recalculate those of 
    remove(lspTpls, recursive=true);
    println("Checking rascal-lsp");
    lspCheckResult = check([lspSource + "demo/lang/pico/OldStyleLanguageServer.rsc"], rascalCompilerConfig(pcfgLsp)[verbose=true]);
    println("Rascal-LSP messages:");
    for (/e:error(_,_) := lspCheckResult) {
        println(e);
    }
}
void main(){
    testRascalLSP();
}