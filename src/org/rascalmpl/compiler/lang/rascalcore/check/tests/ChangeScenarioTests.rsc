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
@synopsis{Tests for common change scenarios}
module lang::rascalcore::check::tests::ChangeScenarioTests

import lang::rascalcore::check::tests::StaticTestingUtils;
import lang::rascalcore::check::TestConfigs;
extend lang::rascalcore::check::CheckerCommon;
import util::Reflective;
import util::Benchmark;
import IO;
import ValueIO;
import List;
import String;
import ListRelation;
import util::FileSystem;

bool validateBOMs(PathConfig pcfg){
    map[str,datetime] lastModRSC = ();
    map[str,datetime] lastModTPL = ();
    nOudatedTPLs = 0;
    for(srcdir <- pcfg.srcs, !contains(srcdir.scheme, "jar")){
        for(loc mloc <- find(srcdir, "rsc")){
            m = getRascalModuleName(mloc, pcfg);
            lastModRSC[m] = lastModified(mloc);
            <found, tpl> = getTPLReadLoc(m, pcfg);
            if(found){
                lastModTPL[m] = lastModified(tpl);
                if(lastModRSC[m] > lastModTPL[m]){
                    nOudatedTPLs += 1;
                    println("<m>: rsc: <lastModRSC[m]> \> <lastModTPL[m]>");
                }
                //println("<m>: <lastModRSC[m]> (rsc), <lastModTPL[m]> (tpl)");
            }
        }
    }
    //println("<nOudatedTPLs> outdated TPLs");
    valid = nOudatedTPLs == 0;
    for(srcdir <- pcfg.srcs, !contains(srcdir.scheme, "jar")){
        for(loc mloc <- find(srcdir, "rsc")){
            m = getRascalModuleName(mloc, pcfg);
            <found, tpl> = getTPLReadLoc(m, pcfg);
           
            if(found){
                tm = readBinaryValueFile(#TModel, tpl);
                if(tm.store[key_bom]? && rel[str,datetime,PathRole] bom := tm.store[key_bom]){
                   for(<str mname, datetime timestampInBom, PathRole pathRole> <- bom){
                        if(timestampInBom != lastModRSC[mname]){
                            valid = false;
                            println("In BOM of <m> (<lastModRSC[m]>), unequal src time for <mname>: <timestampInBom> != <lastModRSC[mname]>");
                        }
                        if(timestampInBom > lastModTPL[mname]){
                            valid = false;
                            println("In BOM of <m> (<lastModRSC[m]>), outdated tpl for <mname>: <timestampInBom> \> <lastModTPL[mname]>");
                        }
                        if(mname == m && timestampInBom != lastModRSC[m]){
                            valid = false;
                            println("In BOM of <m> (<lastModRSC[m]>), <timestampInBom> != <lastModRSC[m]>");
                        }
                    }
                }
            }
        }
    }
    return valid;
}

// ------------------------------- Tests --------------------------------------

test bool fixMissingImport(){
    clearMemory();
    assert missingModuleInModule("module B import A;");
    writeModule("module A");
    return checkModuleOK("module C import B;");
}

test bool fixMissingExtend(){
    clearMemory();
    assert missingModuleInModule("module B extend A;");
    writeModule("module A");
    return checkModuleOK("module C extend B;");
}

test bool fixErrorInImport(){
    clearMemory();
    assert checkModuleOK("module A public bool b = false;");
    B = "module B import A; int n = b + 1;";
    assert unexpectedTypeInModule(B);
    assert checkModuleOK("module A public int b = 0;"); // change b to type int
    return checkModuleOK(B);
}

test bool fixErrorInExtend(){
    clearMemory();
    assert checkModuleOK("module A bool b = false;");
    B = "module B extend A; int n = b + 1;";
    assert unexpectedTypeInModule(B);
    assert checkModuleOK("module A int b = 0;"); // change b to type int
    return checkModuleOK(B);
}

test bool introduceErrorInImport(){
    clearMemory();
    assert checkModuleOK("module A public int b = 0;");
    B = "module B import A; int n = b + 1;";
    assert checkModuleOK(B);
    assert checkModuleOK("module A public bool b = false;");
    return unexpectedTypeInModule(B);
}

test bool introduceErrorInExtend(){
    clearMemory();
    assert checkModuleOK("module A int b = 0;");
    B = "module B extend A; int n = b + 1;";
    assert checkModuleOK(B);
    assert checkModuleOK("module A bool b = false;");
    return unexpectedTypeInModule(B);
}

test bool removeImportedAndRestoreIt1(){
    clearMemory();
    assert checkModuleOK("module A");
    B = "module B import A;";
    assert checkModuleOK(B);
    removeModule("A");
    assert missingModuleInModule(B);
    assert checkModuleOK("module A");
    return checkModuleOK(B);
}

test bool removeImportedAndRestoreIt2(){
    clearMemory();
    A = "module A int twice(int n) = n * n;";
    assert checkModuleOK(A);
    B = "module B import A; int quad(int n) = twice(twice(n));";
    assert checkModuleOK(B);
    removeModule("A");
    assert missingModuleInModule(B);
    assert checkModuleOK(A);
    return checkModuleOK(B);
}

test bool removeExtendedAndRestoreIt1(){
    clearMemory();
    A = "module A";
    assert checkModuleOK(A);
    B = "module B extend A;";
    assert checkModuleOK(B);
    removeModule("A");
    assert missingModuleInModule(B);
    assert checkModuleOK(A);
    return checkModuleOK(B);
}

test bool removeExtendedAndRestoreIt2(){
    clearMemory();
    A = "module A int twice(int n) = n * n;";
    assert checkModuleOK(A);
    B = "module B extend A; int quad(int n) = twice(twice(n));";
    assert checkModuleOK(B);
    removeModule("A");
    assert missingModuleInModule(B);
    assert checkModuleOK(A);
    return checkModuleOK(B);
}

test bool removeOverloadAndRestoreIt(){
    clearMemory();
    A1 = "module A
            int dup(int n) = n + n;
            str dup(str s) = s + s;";
    A2 = "module A
            int dup(int n) = n + n;";
    assert checkModuleOK(A1);
    B = "module B import A;  str f(str s) = dup(s);";
    assert checkModuleOK(B);
    removeModule("A");
    assert missingModuleInModule(B);

    assert checkModuleOK(A2);
    assert argumentMismatchInModule(B);
    assert checkModuleOK(A1);
    return checkModuleOK(B);
}

test bool removeConstructorAndRestoreIt(){
    clearMemory();
    A1 = "module A
            data D = d(int n) | d(str s);";
    A2 = "module A
            data D = d(int n);";
    assert checkModuleOK(A1);
    B = "module B import A;  D f(str s) = d(s);";
    assert checkModuleOK(B);
    removeModule("A");
    assert missingModuleInModule(B);

    assert checkModuleOK(A2);
    assert argumentMismatchInModule(B);
    assert checkModuleOK(A1);
    return checkModuleOK(B);
}

// ---- incremental type checking ---------------------------------------------

// Legend:
//      X --> Y: X import Y
//      X ==> Y: X extends Y
//      X >>> Y: replace X by Y
//      *X     : check starts at X
//      X!     : X is (re)checked
//      X?     : X contains error

// Scenarios:
//     I        II      III     IV      V       VI
//
//     *A1!     A1  >>> A2!     A2  >>> A3!     A3
//              ^       ^       ^       ^       ^
//              |       |       |       |       |
//             *B1!     B1      B1      B1 >>>  B2!
//                              ^       ^       ^
//                              |       |       |
//                             *C1!    *C1     *C1

test bool nobreakingChange1(){
    clearMemory();
    A1 = "module A";
    A2 = "module A
            public int n = 3;";
    A3 = "module A
            public int n = 3;
            data D = d1();";

    assert expectReChecks(A1, ["A"]);    // I

    B1 = "module B
            import A;";
    B2 = "module B
            import A;
            public int m = n + 1;";
    assert expectReChecks(B1, ["B"]);    // II

    writeModule(A2);
    assert expectReChecks(B1, ["A"]);    // III

    C1 = "module C
            import B;
            int f() = 2;";
    assert expectReChecks(C1, ["C"]);    // IV

    writeModule(A3);

    assert expectReChecks(C1, ["A"]);    // V

    writeModule(B2);
    return expectReChecks(C1, ["B"]);    // VI
}


//      I                   II
//
//      A1!<-----+          A1<-------+
//      ^        |          |         |
//      |        |          |         |
//      B1!<-+   |    >>>   B2!<-+    |
//      ^    |   |          |    |    |
//      |    |   |          |    |    |
//      C1!  +---D1!        C1   +----D1
//      ^        ^          ^         ^
//      |        |          |         |
//      +--*E!---+          +---*E----+

test bool nobreakingChange2(){
    clearMemory();
    A1 = "module A";
    B1 = "module B import A;";
    C1 = "module C import B;";
    D1 = "module D import A; import B;";
    E1 = "module E import C; import D;";

    B2 = "module B import A; int n = 0;";

    writeModules(A1, B1, C1, D1, E1);

    assert expectReChecks(E1, ["A", "B", "C", "D", "E"]);    // I

    writeModule(B2);
    return expectReChecks(C1, ["B"]);                        // II
}

//      I                   II
//
//      A1!<-----+          A1<-------+
//      ^        |          ^         |
//      |        |          |         |
//      B1!<-+   |    >>>   B2!<-+    |
//      |    |   |          |    |    |
//      C1!  +---D1!        C1!  +----D1
//      ^        ^          ^         ^
//      |        |          |         |
//      +--*E!---+          +---*E----+

test bool noBreakingChange3(){
    clearMemory();
    A1 = "module A";
    B1 = "module B import A; int b() = 1;";
    C1 = "module C import B; int c() = b();";
    D1 = "module D import A; import B;";
    E1 = "module E import C; import D;";

    B2 = "module B import A; int b(int n) = n;";

    writeModules(A1, B1, C1, D1, E1);

    assert expectReChecks(E1, ["A", "B", "C", "D", "E"]);   // I

    writeModule(B2);
    return expectReChecksWithErrors(C1, ["B", "C"]);        // II
}

//      I       II             III       IV
//
//      A1!     A1<----+  >>>  A2!       A2<------+
//      ^       ^      |       ^         ^        |
//      |       |      |       |         |        |
//      B1!     B1    *D1!     B1        B1       D1!
//      ^       |              ^         ^        ^
//      |       |              |         |        |
//     *C1!     C1            *C1   ==> *C2! -----+
//
test bool noBreakingChange4(){
    clearMemory();
    A1 = "module A";
    A2 = "module A int a() = 0;";
    B1 = "module B import A; int b() = 1;";
    C1 = "module C import B; int c() = b();";
    C2 = "module C import B; import D; int c() = b();";
    D1 = "module D import A;";

    writeModules(A1, B1, C1);

    assert expectReChecks(C1, ["A", "B", "C"]);  // I

    writeModule(D1);
    assert expectReChecks(D1, ["D"]);            // II

    writeModule(A2);
    assert expectReChecks(C1, ["A"]);            // III

     writeModule(C2);
     return expectReChecks(C2, ["C", "D"]);      // IV
}

//          Math---------->Exception
//           | ^             ^  ^
//           |  \           /   |
//           |    \        /    |
//           |      \     /     |
//           |        Set       |
//           |      /  ^        |
//           |    /    |        |
//           |  /      |        |
//           v v       |        |
//  Map <----List<----Top       |
//            |                 |
//            +-----------------+

test bool noBreakingChange5(){
    Exception1 = "module Exception";
    Exception2 = "module Exception
                            int n = 0;";
    Math = "module Math import Exception; import List; ";
    List = "module List import Map; import Exception; ";
    Map  = "module Map";
    Set1 = "module Set import List; import Exception; import Math;";
    Set2 = "module Set import List; import Exception; import Math;
                        int m = 0;";
    Top  = "module Top  import List; import Set; ";

    writeModules(Exception1, Math, List, Map, Set1, Top);

    assert expectReChecks(Top, ["Exception", "Math", "List", "Map", "Set", "Top"]);

    writeModule(Exception2);
    assert expectReChecks(Top, ["Exception"]);

    writeModule(Set2);
    assert expectReChecks(Top, ["Set"]);

    writeModule(Exception1);
    writeModule(Set1);
    return expectReChecks(Top, ["Exception", "Set"]);
}

//   +-->A1!<-+  >>>  +-->A2!<-+         +-->A2<--+
//   |        |       |        |         |        |
//   B!       C1!     B!       C1!?  >>> B        C2!
//   ^        ^       ^        ^         ^        ^
//   |        |       |        |         |        |
//   +---D!---+       +----D---+         +----D---+

test bool breakingChange1(){
    clearMemory();
    A1 = "module A int f(int n) = n; int g() = 42;";
    A2 = "module A int f(int n) = n;";
    B  = "module B import A; int ff(int n) = f(n);";
    C1 = "module C import A; int fff(int n) = f(n); int gg() = g();";
    C2 = "module C import A; int fff(int n) = f(n);";
    D  = "module D import B; import C;";

    writeModules(A1, B, C1, D);

    assert expectReChecks(D, ["A", "B", "C", "D"]);
    writeModule(A2);
    assert expectReChecksWithErrors(D, ["A", "B", "C"]);
    writeModule(C2);
    return expectReChecks(D, ["C", "D"]);
}

// ---- touch and recheck modules ---------------------------------------------

bool touchAndCheck(loc topLoc, list[str] moduleNames, PathConfig pcfg){
    println("TOUCH <moduleNames>");
    mlocs = [ getRascalModuleLocation(mname, pcfg) | mname <- moduleNames ];
    for(mloc <- mlocs){
            touch(mloc);
    }
    return expectReChecks([topLoc, *mlocs], moduleNames + getModuleName(topLoc, pcfg), pathConfig=pcfg);
}

void safeRemove(loc l){
    try remove(l, recursive=true); catch _:;
}

test bool onlyTouchedModulesAreReChecked0(){
    pcfg = getRascalWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    topLoc = getRascalModuleLocation("List", pcfg);
    assert checkModuleOK(topLoc, pathConfig = pcfg);
    assert validateBOMs(pcfg);

    assert touchAndCheck(topLoc, ["Exception"], pcfg);
    return touchAndCheck(topLoc, ["Map"], pcfg);
}

test bool onlyTouchedModulesAreReChecked1(){
    pcfg = getRascalWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    topLoc = getRascalModuleLocation("analysis::grammars::Ambiguity", pcfg);
    assert checkModuleOK(topLoc, pathConfig = pcfg);
    assert validateBOMs(pcfg);

    assert touchAndCheck(topLoc, ["Exception"], pcfg);
    assert touchAndCheck(topLoc, ["Set"], pcfg);
    assert touchAndCheck(topLoc, ["Grammar"], pcfg);
    return touchAndCheck(topLoc, ["Exception", "Set", "Grammar"], pcfg);
}

@ignore{Very expensive test}
test bool onlyTouchedModulesAreReChecked2(){
    pcfg = getAllSrcWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    topLoc = getRascalModuleLocation("lang::rascalcore::check::Checker", pcfg);
    assert checkModuleOK(topLoc, pathConfig = pcfg);
    assert validateBOMs(pcfg);

    assert touchAndCheck(topLoc, ["Exception"], pcfg);
    assert touchAndCheck(topLoc, ["Set"], pcfg);
    assert touchAndCheck(topLoc, ["Exception", "Set"], pcfg);
    assert touchAndCheck(topLoc, ["lang::rascalcore::check::CollectType"], pcfg);
    return touchAndCheck(topLoc, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal", "lang::rascalcore::check::CollectType"], pcfg);
}

// ---- change and recheck modules --------------------------------------------

str MARKER = "//TEMPORARILY ADDED FOR TESTING";

void changeModules(list[str] moduleNames, PathConfig pcfg, str injectedError=""){
    for(moduleName <- moduleNames){
        mloc = getRascalModuleLocation(moduleName, pcfg);
        lines = readFileLines(mloc);
        lines += MARKER;
        lines += injectedError;
        writeFileLines(mloc, lines);
    }
}

void restoreModules(list[str] moduleNames, PathConfig pcfg){
    for(moduleName <- moduleNames){
        mloc = getRascalModuleLocation(moduleName, pcfg);
        lines = readFileLines(mloc);
        for(int i <- index(lines)){
            if(lines[i] == MARKER){
                writeFileLines(mloc, lines[..i]);
                break;
            }
        }
    }
}

bool changeAndCheck(loc topLoc, list[str] moduleNames, PathConfig pcfg, str injectedError=""){
    println("CHANGE <moduleNames>");
    mlocs = [ getModuleLocation(mname, pcfg) | mname <- moduleNames ];
    changeModules(moduleNames, pcfg, injectedError=injectedError);
    return expectReChecks([topLoc, *mlocs], moduleNames, pathConfig=pcfg, errorsAllowed = injectedError?);
}

test bool onlyChangedModulesAreReChecked0(){
    pcfg = getRascalWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    topLoc = getRascalModuleLocation("List", pcfg);
    assert checkModuleOK(topLoc, pathConfig = pcfg);
    assert validateBOMs(pcfg);

    assert changeAndCheck(topLoc, ["Exception"], pcfg);
    return changeAndCheck(topLoc, ["Map"], pcfg);
}

test bool onlyChangedModulesAreReChecked1(){
    pcfg = getRascalWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    str topName = "analysis::grammars::Ambiguity";
    loc topLoc = getRascalModuleLocation(topName, pcfg);
    assert checkModuleOK(topLoc, pathConfig = pcfg);
    assert validateBOMs(pcfg);

    assert changeAndCheck(topLoc, ["Exception"], pcfg);
    assert changeAndCheck(topLoc, ["Set"], pcfg);
    assert changeAndCheck(topLoc, ["Grammar"], pcfg);
    assert changeAndCheck(topLoc, ["Exception", "Set", "Grammar"], pcfg);
    
    assert validateBOMs(pcfg);

    restoreModules(["Exception", "Set", "Grammar"], pcfg);

    // Error scenario's
    assert changeAndCheck(topLoc, [topName], pcfg, injectedError="int X = false;");
    assert unexpectedDeclarationInModule(topLoc, pathConfig=pcfg);
    restoreModules([topName], pcfg);
    
    loc exceptionLoc = getRascalModuleLocation("Exception", pcfg);
    assert changeAndCheck(topLoc, ["Exception"], pcfg, injectedError="str S = 10;");
    assert unexpectedDeclarationInModule(exceptionLoc, pathConfig=pcfg);
    restoreModules(["Exception"], pcfg);
    assert checkModuleOK(topLoc, pathConfig=pcfg);

    return true;
}

@ignore{Very expensive test}
test bool onlyChangedModulesAreReChecked2(){
    pcfg = getAllSrcWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    topLoc = getRascalModuleLocation("lang::rascalcore::check::Checker", pcfg);
    assert checkModuleOK(topLoc, pathConfig = pcfg);
    assert validateBOMs(pcfg);

    assert changeAndCheck(topLoc, ["Exception"], pcfg);
    assert changeAndCheck(topLoc, ["Set"], pcfg);
    assert changeAndCheck(topLoc, ["Exception", "Set"], pcfg);
    assert changeAndCheck(topLoc, ["lang::rascalcore::check::CollectType"], pcfg);
    assert changeAndCheck(topLoc, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal",
                                      "lang::rascalcore::check::CollectType"], pcfg);
     
    assert validateBOMs(pcfg);
    restoreModules(["Exception", "Set", "ParseTree", "analysis::typepal::TypePal",
                    "lang::rascalcore::check::CollectType"], pcfg);
    return true;
}

// Benchmarks for incremental type checking

void benchmark(str title, lrel[str, void()] cases){
    measurements = [];
	for (<str Name, runCase> <- cases) {
        println("Running <Name>");
		measurements+= <Name, realTimeOf(runCase)>;
	}
    sum(range(measurements));
    println(title);
    iprintln(measurements);
    println("Total: <sum(range(measurements))>");
}

void touchOne(){
    pcfg = getRascalWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    str topName = "ParseTree";
    topLoc = getRascalModuleLocation("ParseTree", pcfg);
    cases =
        [<"<topName>, first", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>
  //       <"<topName>, touched", void(){ touchAndCheck(topLoc, [topName], pcfg); }>
        ];
    benchmark("touchOne", cases);
}

void miniBenchmarkRechecking1(){
    pcfg = getRascalWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    topName = "Type";
    topLoc = getRascalModuleLocation("Type", pcfg);

    cases =
        [<"<topName>, first", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>,
         <"<topName>, nochange", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>,
         <"<topName>, touched", void(){ touchAndCheck(topLoc, [topName], pcfg); }>,
         <"Exception", void(){ touchAndCheck(topLoc, ["Exception"], pcfg); }>,
         <"Map", void(){ touchAndCheck(topLoc, ["Map"], pcfg); }>,
         <"Exception+Map", void(){ touchAndCheck(topLoc, ["Exception", "Map"], pcfg); }>
        ];
    benchmark("miniBenchmarkRechecking1", cases);
}

void miniBenchmarkRechecking2(){
    pcfg = getRascalWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    topName = "ParseTree";
    topLoc = getRascalModuleLocation("ParseTree", pcfg);

    cases =
        [<"<topName>, first", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>,
         <"<topName>, nochange", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>,
         <"<topName>, touched", void(){ touchAndCheck(topLoc, [topName], pcfg); }>,
         <"Exception", void(){ touchAndCheck(topLoc, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(topLoc, ["Set"], pcfg); }>,
         <"Exception+Set", void(){ touchAndCheck(topLoc, ["Exception", "Set"], pcfg); }>
        ];
    benchmark("miniBenchmarkRechecking2", cases);
}

void mediumBenchmarkRechecking(){
    pcfg = getRascalWritablePathConfig();
    safeRemove(pcfg.generatedResources);
    topName = "analysis::grammars::Ambiguity";
    topLoc = getRascalModuleLocation(topName, pcfg);

    cases =
        [<"<topName>, first", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>,
         <"<topName>, nochange", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>,
         <"<topName>, touched", void(){ touchAndCheck(topLoc, [topName], pcfg); }>,
         <"Exception", void(){ touchAndCheck(topLoc, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(topLoc, ["Set"], pcfg); }>,
         <"Grammar", void(){ touchAndCheck(topLoc, ["Grammar"], pcfg); }>,
         <"Exception+Set+Grammar", void(){ touchAndCheck(topLoc, ["Exception", "Set", "Grammar"], pcfg); }>
        ];

    benchmark("mediumBenchmarkRechecking", cases);
}

void largeBenchmarkRechecking(){
    pcfg = getAllSrcREPOPathConfig();
    safeRemove(pcfg.generatedResources);
    topName = "lang::rascalcore::check::Checker";
    topLoc = getRascalModuleLocation(topName, pcfg);

    cases =
        [<"<topName>, first", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>,
         <"<topName>, nochange", void(){ checkModuleOK(topLoc, pathConfig = pcfg); }>,
         <"<topName>, touched", void(){ touchAndCheck(topLoc, [topName], pcfg); }>,
         <"Exception", void(){ touchAndCheck(topLoc, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(topLoc, ["Set"], pcfg); }>,
         <"Grammar", void(){ touchAndCheck(topLoc, ["Grammar"], pcfg); }>,
         <"Exception+Set+Grammar", void(){ touchAndCheck(topLoc, ["Exception", "Set", "Grammar"], pcfg); }>,
         <"lang::rascalcore::check::CollectType", void(){ touchAndCheck(topLoc, ["lang::rascalcore::check::CollectType"], pcfg); }>,
         <"5 modules changed", void(){ touchAndCheck(topLoc, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal", "lang::rascalcore::check::CollectType"], pcfg); }>
        ];

    benchmark("largeBenchmarkRechecking", cases);
}

void allBenchmarks(){
    beginTime = cpuTime();
    miniBenchmarkRechecking1();
    miniBenchmarkRechecking2();
    mediumBenchmarkRechecking();
    //largeBenchmarkRechecking();
    println("Total time: <(cpuTime() - beginTime)/1000000> ms");
}