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
module lang::rascalcore::compile::Examples::Tst1
import lang::rascalcore::check::Checker;
import util::FileSystem;
import util::PathConfig;
import IO;
import Message;

private void runChecker(PathConfig pcfg, bool (loc m) validModule) {
    result = check([m | src <- pcfg.srcs, m <- find(src, "rsc"), validModule(m)], rascalCompilerConfig(pcfg));
    for (/e:error(_,_) := result) {
        println(e);
    }
}

void showFiles(PathConfig pcfg){
    for(src <- pcfg.srcs, m <- find(src, "rsc")){
        println(m);
    }
}

void main(loc repoRoot = |file:///Users/paulklint/git/|, loc tplRoot = |file:///Users/paulklint/rascal-tpls|) {

    // rascalPcfg = pathConfig(srcs=[repoRoot + "rascal/src/org/rascalmpl/library"], bin=tplRoot + "rascal");
    // salixCorePcfg = pathConfig(srcs=[repoRoot + "salix-core/src/main/rascal"], bin=tplRoot + "salix-core", libs=[rascalPcfg.bin]);
    // salixContribPcfg = pathConfig(srcs=[repoRoot + "salix-contrib/src/main/rascal"], bin=tplRoot + "salix-core", libs=[rascalPcfg.bin, salixCorePcfg.bin]);


    rascalWithTypepalPcfg =
        pathConfig(srcs = [repoRoot + "rascal/src/org/rascalmpl/library",
                           repoRoot + "rascal/src/org/rascalmpl/compiler",
                           repoRoot + "rascal/src/org/rascalmpl/compiler",
                           repoRoot + "typepal/src/"], 
                   bin  =  tplRoot + "rascal");
    
    LSP_REPO = repoRoot + "rascal-language-servers/rascal-lsp";
    lspPcfg = 
        pathConfig(srcs = [LSP_REPO + "src/main/rascal/library",
                           LSP_REPO + "src/main/rascal/lsp",
                           LSP_REPO + "src/test/rascal"],
                   bin  = tplRoot + "lsp",
                   libs = [rascalWithTypepalPcfg.bin]);

    // println("**** Checking rascal + typepal");
    // runChecker(rascalWithTypepalPcfg, bool (loc m) { return  /*/lang.rascal/ !:= m.path &&*/ /experiments/ !:= m.path && /lang.rascal.*tests/ !:= m.path; });


    println("**** Checking lsp");
    msgs = checkModules([
    // "demo::lang::pico::LanguageServer",
     "util::Util",
    "util::LanguageServer",
    "lang::rascal::lsp::Actions",
    "lang::rascal::lsp::DocumentSymbols",
    "lang::rascal::lsp::IDECheckerWrapper",
    "lang::rascal::lsp::Templates",
    "lang::rascal::lsp::refactor::Rename",
    "lang::rascal::lsp::refactor::rename::Common",
    "lang::rascal::lsp::refactor::rename::Constructors",
    "lang::rascal::lsp::refactor::rename::Fields",
    "lang::rascal::lsp::refactor::rename::Functions",
    "lang::rascal::lsp::refactor::rename::Grammars",
    "lang::rascal::lsp::refactor::rename::Modules",
    "lang::rascal::lsp::refactor::rename::Parameters",
    "lang::rascal::lsp::refactor::rename::Types",
    "lang::rascal::lsp::refactor::rename::Variables",
    "lang::rascal::tests::rename::Annotations",
    "lang::rascal::tests::rename::Benchmark",
    "lang::rascal::tests::rename::Constructors",
    "lang::rascal::tests::rename::Fields",
    "lang::rascal::tests::rename::FormalParameters",
    "lang::rascal::tests::rename::Functions",
    "lang::rascal::tests::rename::Grammars",
    "lang::rascal::tests::rename::Modules",
    "lang::rascal::tests::rename::Performance",
    "lang::rascal::tests::rename::ProjectOnDisk",
    "lang::rascal::tests::rename::TestUtils",
    "lang::rascal::tests::rename::Types",
    "lang::rascal::tests::rename::ValidNames",
    "lang::rascal::tests::rename::Variables",
    "lang::rascal::tests::semanticTokenizer::NestedCategories",
    "lang::rascal::tests::semanticTokenizer::Pico",
    "lang::rascal::tests::semanticTokenizer::Rascal",
    "lang::rascal::tests::semanticTokenizer::Util"
    ], rascalCompilerConfig(lspPcfg));
    for (/e:error(_,_) := msgs) {
        println(e);
    }
    //runChecker(lspPcfg, bool (loc m) { return true; });
}