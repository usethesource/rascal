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
module lang::rascalcore::compile::Examples::Tst4


import util::Reflective;
import IO;
import ParseTree;
import util::Math;
import lang::rascal::grammar::storage::ModuleParserStorage;

lexical W = [\ ];
layout L = W*;
lexical A = [A];
syntax As = A+;

// test bool storeParserNonModule() {
//   storeParsers(#As, |memory://test-tmp/parsersA.jar|);
//   p = loadParsers(|memory://test-tmp/parsersA.jar|);

//   return p(#As, "A A", |origin:///|) == parse(#As, "A A", |origin:///|);
// }

loc constructExampleProject() {
    root = |memory://test-tmp/example-prj-<"<arbInt()>">|;
    newRascalProject(root);
    return root;
}

// fix for target scheme not working for "non-existing" projects
PathConfig getTestPathConfig(loc root) {
    pcfg = getProjectPathConfig(root);
    pcfg.bin = root + "target/classes";
    // remove std to avoid generating parsers for all modules in the library that contain syntax definitions
    pcfg.srcs -= [|std:///|];
    return pcfg;
}

value main(){ //test bool storeModuleParsersWorkedSimpleGrammar() {
    root = constructExampleProject();
	println("root: <root>");
    writeFile(root + "src/main/rascal/A.rsc", "module A
        'lexical A = [A];
        ");
    println("getTestPathConfig(root): <getTestPathConfig(root)>");
    storeParsersForModules(getTestPathConfig(root));
    
    return exists(root + "target/classes/A.parsers");
}

// test bool storeModuleParsersWorkedForBiggerGrammar() {
//     root = constructExampleProject();
//     writeFile(root + "src/main/rascal/A.rsc", "module A
//         'lexical W = [\\ ];
//         'layout L = W*;
//         'lexical A = [A];
//         'syntax As = A+;
//         ");
    
//     storeParsersForModules(getTestPathConfig(root));
    
//     return exists(root + "target/classes/A.parsers");
// }



// import util::Reflective;
// import IO;
// import ParseTree;
// import util::Math;
// import lang::rascal::grammar::storage::ModuleParserStorage;

// lexical W = [\ ];
// layout L = W*;
// lexical A = [A];
// syntax As = A+;

// value main() { //test bool storeParserNonModule() {
//   storeParsers(#As, |memory://test-tmp/parsersA.jar|);
    
//   p = loadParsers(|memory://test-tmp/parsersA.jar|);

//   reifiedAs = type(sort("As"),(lex("W"):choice(lex("W"),{prod(lex("W"),[\char-class([range(32,32)])],{})}),layouts("L"):choice(layouts("L"),{prod(layouts("L"),[\iter-star(lex("W"))],{})}),sort("As"):choice(sort("As"),{prod(sort("As"),[\iter-seps(lex("A"),[layouts("L")])],{})}),lex("A"):choice(lex("A"),{prod(lex("A"),[\char-class([range(65,65)])],{})})));
//   //return p(type(sort("As"), ()), "A A", |origin:///|) == parse(#As, "A A", |origin:///|);
//   return p(#As, "A A", |origin:///|) == parse(#As, "A A", |origin:///|);
//   //return p(reifiedAs, "A A", |origin:///|) == parse(#As, "A A", |origin:///|);
// }