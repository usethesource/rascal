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
import util::Reflective;

private void runChecker(PathConfig pcfg, bool (loc m) validModule) {
    result = check([m | src <- pcfg.srcs, m <- find(src, "rsc"), validModule(m)], rascalCompilerConfig(pcfg)[verbose=true][logWrittenFiles=true]);
    for (/e:error(_,_) := result) {
        println(e);
    }
}

private void runChecker(PathConfig pcfg, list[str] modules) {
    result = check([getRascalModuleLocation(m, pcfg) | m <- modules], rascalCompilerConfig(pcfg)[verbose=true][logWrittenFiles=true]);
    for (/e:error(_,_) := result) {
        println(e);
    }
}

private void runChecker(PathConfig pcfg, list[loc] modules) {
    result = check(modules, rascalCompilerConfig(pcfg)[verbose=true][logWrittenFiles=true]);
    for (/e:error(_,_) := result) {
        println(e);
    }
}

void main(loc repoRoot =  |file:///Users/paulklint/git/|, loc tplRoot = |file:///Users/paulklint/rascal-tpls|) {
    rascalPcfg = pathConfig(srcs=[repoRoot + "rascal/src/org/rascalmpl/library"], bin=tplRoot + "rascal");
    salixCorePcfg = pathConfig(srcs=[repoRoot + "salix-core/src/main/rascal"], bin=tplRoot + "salix-core", libs=[rascalPcfg.bin]);
    salixContribPcfg = pathConfig(srcs=[repoRoot + "salix-contrib/src/main/rascal"], bin=tplRoot + "salix-core", libs=[rascalPcfg.bin, salixCorePcfg.bin]);

    remove(tplRoot);
    println("**** Checking rascal");
    getModuleLocation("List", rascalPcfg);
    runChecker(rascalPcfg, 
      ["List", "String", "ParseTree", "lang::json::IO", "vis::Text", "vis::Charts", "util::Webserver", "util::UUID"]
      // bool (loc m) { 
      // return  /lang.rascal/ !:= m.path && /experiments/ !:= m.path && /lang.rascal.*tests/ !:= m.path; 
      // }
      );


    println("**** Checking salix-core");
    //runChecker(salixCorePcfg, [ "salix::App" ]);
    runChecker(salixCorePcfg,
    [ 
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/Node.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/shop/Shop.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/SVG.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/Diff.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/util/Debug.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/basic/Files.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/HTML.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/todomvc/TodoMVC.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/alien/Prototype.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/basic/Random.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/Index.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/basic/All.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/util/WithPopups.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/Core.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/basic/Clock.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/App.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/basic/Bug.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/util/Highlight.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/basic/Counter.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/util/LCS.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/basic/Loop.rsc|,
      |file:///Users/paulklint/git/salix-core/src/main/rascal/salix/demo/basic/Celsius.rsc|
    ]);
    
    //runChecker(salixCorePcfg, bool(loc m) { return true; });

    println("**** Checking salix-contrib");
    runChecker(salixContribPcfg, [
      |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/mermaid/Mermaid.rsc|,
      |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/mermaid/Demo.rsc|,
      |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/jsplumb/Demo.rsc|,
      |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/mermaid/FlowChart.rsc|,
         |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/util/Mode.rsc|
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/Main.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/ace/Editor.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/cytoscape/Demo.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/canvas/Demo.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/canvas/Heart.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/ace/Demo.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/canvas/Canvas.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/charts/Charts.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/jsplumb/JSPlumb.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/cytoscape/Cytoscape.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/charts/Demo.rsc|,
      // |file:///Users/paulklint/git/salix-contrib/src/main/rascal/salix/mermaid/ClassDiagram.rsc|
]);
    //runChecker(salixContribPcfg,  bool(loc m) { return true; });
}