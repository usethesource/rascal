@synopsis{Tests for common change scenarios}
module lang::rascalcore::check::tests::ChangeScenarioTests

import lang::rascalcore::check::tests::StaticTestingUtils;
import lang::rascalcore::check::TestConfigs;
import util::Reflective;
import util::Benchmark;
import IO;
import List;
import ListRelation;

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

bool touchAndCheck(loc Top, list[str] moduleNames, PathConfig pcfg){
    println("TOUCH <moduleNames>");
    for(mname <- moduleNames){
        touch(getModuleLocation(mname, pcfg));
    }
    return expectReChecks(Top, moduleNames, pathConfig=pcfg);
}

test bool onlyTouchedModulesAreReChecked1(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    Top = getModuleLocation("analysis::grammars::Ambiguity", pcfg);
    assert checkModuleOK(Top, pathConfig = pcfg);

    assert touchAndCheck(Top, ["Exception"], pcfg);
    assert touchAndCheck(Top, ["Set"], pcfg);
    assert touchAndCheck(Top, ["Grammar"], pcfg);
    return touchAndCheck(Top, ["Exception", "Set", "Grammar"], pcfg);
}

@ignore{Very expensive test}
test bool onlyTouchedModulesAreReChecked2(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    Top = getModuleLocation("lang::rascalcore::check::Checker", pcfg);
    assert checkModuleOK(Top, pathConfig = pcfg);

    assert touchAndCheck(Top, ["Exception"], pcfg);
    assert touchAndCheck(Top, ["Set"], pcfg);
    assert touchAndCheck(Top, ["Exception", "Set"], pcfg);
    assert touchAndCheck(Top, ["lang::rascalcore::check::CollectType"], pcfg);
    return touchAndCheck(Top, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal", "lang::rascalcore::check::CollectType"], pcfg);
}

// ---- change and recheck modules --------------------------------------------

str MARKER = "//TEMPORARILY ADDED FOR TESTING";

void changeModules(list[str] moduleNames, PathConfig pcfg){
    for(moduleName <- moduleNames){
        mloc = getModuleLocation(moduleName, pcfg);
        lines = readFileLines(mloc);
        lines += MARKER;
        writeFileLines(mloc, lines);
    }
}

void restoreModules(list[str] moduleNames, PathConfig pcfg){
    for(moduleName <- moduleNames){
        mloc = getModuleLocation(moduleName, pcfg);
        lines = readFileLines(mloc);
        for(int i <- index(lines)){
            if(lines[i] == MARKER){
                writeFileLines(mloc, lines[..i]);
                break;
            }
        }
    }
}

bool changeAndCheck(loc Top, list[str] moduleNames, PathConfig pcfg){
    println("CHANGE <moduleNames>");
    changeModules(moduleNames, pcfg);
    return expectReChecks(Top, moduleNames, pathConfig=pcfg);
}

test bool onlyChangedModulesAreReChecked1(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    Top = getModuleLocation("analysis::grammars::Ambiguity", pcfg);
    assert checkModuleOK(Top, pathConfig = pcfg);

    assert changeAndCheck(Top, ["Exception"], pcfg);
    assert changeAndCheck(Top, ["Set"], pcfg);
    assert changeAndCheck(Top, ["Grammar"], pcfg);
    assert changeAndCheck(Top, ["Exception", "Set", "Grammar"], pcfg);

    restoreModules(["Exception", "Set", "Grammar"], pcfg);
    return true;
}

@ignore{Very expensive test}
test bool onlyChangedModulesAreReChecked2(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    Top = getModuleLocation("lang::rascalcore::check::Checker", pcfg);
    assert checkModuleOK(Top, pathConfig = pcfg);

    assert changeAndCheck(Top, ["Exception"], pcfg);
    assert changeAndCheck(Top, ["Set"], pcfg);
    assert changeAndCheck(Top, ["Exception", "Set"], pcfg);
    assert changeAndCheck(Top, ["lang::rascalcore::check::CollectType"], pcfg);
    assert changeAndCheck(Top, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal",
                                      "lang::rascalcore::check::CollectType"], pcfg);
    restoreModules(["Exception", "Set", "ParseTree", "analysis::typepal::TypePal",
                    "lang::rascalcore::check::CollectType"], pcfg);
    return true;
}

// Benchmarks for incremental type checking

void benchmark(str title, lrel[str, void()] cases){
    measurements = [];
	for (<str Name, runCase> <- cases) {
		measurements+= <Name, realTimeOf(runCase)>;
	}
    sum(range(measurements));
    println(title);
    iprintln(measurements);
    println("Total: <sum(range(measurements))>");
}

void miniBenchmarkRechecking(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    Top = getModuleLocation("ParseTree", pcfg);

    cases =
        [<"ParseTree, first", void(){ checkModuleOK(Top, pathConfig = pcfg); }>,
         <"ParseTree, nochange", void(){ checkModuleOK(Top, pathConfig = pcfg); }>,
         <"Exception", void(){ touchAndCheck(Top, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(Top, ["Set"], pcfg); }>,
         <"Exception+Set", void(){ touchAndCheck(Top, ["Exception", "Set"], pcfg); }>
        ];
    benchmark("miniBenchmarkRechecking", cases);
}

void mediumBenchmarkRechecking(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    Top = getModuleLocation("analysis::grammars::Ambiguity", pcfg);

    cases =
        [<"analysis::grammars::Ambiguity, first", void(){ checkModuleOK(Top, pathConfig = pcfg); }>,
         <"analysis::grammars::Ambiguity, nochange", void(){ checkModuleOK(Top, pathConfig = pcfg); }>,
         <"Exception", void(){ touchAndCheck(Top, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(Top, ["Set"], pcfg); }>,
         <"Grammar", void(){ touchAndCheck(Top, ["Grammar"], pcfg); }>,
         <"Exception+Set+Grammar", void(){ touchAndCheck(Top, ["Exception", "Set", "Grammar"], pcfg); }>
        ];

    benchmark("mediumBenchmarkRechecking", cases);
}

void largeBenchmarkRechecking(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    Top = getModuleLocation("lang::rascalcore::check::Checker", pcfg);

    cases =
        [<"lang::rascalcore::check::Checker", void(){ checkModuleOK(Top, pathConfig = pcfg); }>,
         <"Exception", void(){ touchAndCheck(Top, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(Top, ["Set"], pcfg); }>,
         <"Exception+Set", void(){ touchAndCheck(Top, ["Exception", "Set"], pcfg); }>,
         <"lang::rascalcore::check::CollectType", void(){ touchAndCheck(Top, ["lang::rascalcore::check::CollectType"], pcfg); }>,
         <"5 modules changed", void(){ touchAndCheck(Top, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal", "lang::rascalcore::check::CollectType"], pcfg); }>
        ];

    benchmark("largeBenchmarkRechecking", cases);
}