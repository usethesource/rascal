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
import lang::rascalcore::check::ModuleLocations;

private void runChecker(PathConfig pcfg, bool (loc m) validModule) {
    result = check([m | src <- pcfg.srcs, m <- find(src, "rsc"), validModule(m)], rascalCompilerConfig(pcfg));
    for (/e:error(_,_) := result) {
        println(e);
    }
}

void main(loc repoRoot = |file:///Users/paulklint/git/|, loc tplRoot = |file:///Users/paulklint/rascal-tpls|) {
    rascalPcfg = pathConfig(srcs=[repoRoot + "rascal/src/org/rascalmpl/library"], bin=tplRoot + "rascal");
    salixCorePcfg = pathConfig(srcs=[repoRoot + "salix-core/src/main/rascal"], bin=tplRoot + "salix-core", libs=[rascalPcfg.bin]);
    salixContribPcfg = pathConfig(srcs=[repoRoot + "salix-contrib/src/main/rascal"], bin=tplRoot + "salix-core", libs=[rascalPcfg.bin, salixCorePcfg.bin]);

    println("**** Checking rascal");
    // // iprintln(check([|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|,
    // //                 |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|
    // //                ], rascalCompilerConfig(rascalPcfg)));
 
    // runChecker(rascalPcfg, bool (loc m) { return  true; });

    runChecker(rascalPcfg, bool (loc m) { return  /lang.rascal/ !:= m.path && /experiments/ !:= m.path && /lang.rascal.*tests/ !:= m.path; });
    // println(getRascalModuleName(|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/json/IO.rsc|, salixCorePcfg));
    // println(getRascalModuleName(|file:///Users/paulklint/rascal-tpls/rascal/rascal/lang/json/$IO.tpl|, salixCorePcfg));

    // println(getRascalModuleName(|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|, salixCorePcfg));
    // println(getRascalModuleName(|file:///Users/paulklint/rascal-tpls/rascal/rascal/$IO.tpl|, salixCorePcfg));

    // println(getRascalModuleName(|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/tests/libraries/IO.rsc|, salixCorePcfg));
    // println(getRascalModuleName(|file:///Users/paulklint/rascal-tpls/rascal/rascal/$IO.tpl|, salixCorePcfg));

    // println("**** Checking salix-core");
    // runChecker(salixCorePcfg, bool (loc m) { return true; });

    println("**** Checking salix-contrib");
    //iprintln(check([|file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/util/Mode.rsc|], rascalCompilerConfig(salixContribPcfg)));
    // iprintln(check([
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/mermaid/Mermaid.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/mermaid/Demo.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/jsplumb/Demo.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/mermaid/FlowChart.rsc|,
    // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/util/Mode.rsc|,
    // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/Main.rsc|
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/ace/Editor.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/cytoscape/Demo.rsc|,
    // //|file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/canvas/Demo.rsc|
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/canvas/Heart.rsc|,
    // //|file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/ace/Demo.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/canvas/Canvas.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/charts/Charts.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/jsplumb/JSPlumb.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/cytoscape/Cytoscape.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/charts/Demo.rsc|,
    // // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/mermaid/ClassDiagram.rsc|
    // ], rascalCompilerConfig(salixContribPcfg)));

    runChecker(salixContribPcfg, bool (loc m) { return true; });
 }