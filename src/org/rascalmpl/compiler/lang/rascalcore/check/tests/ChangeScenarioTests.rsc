@synopsis{Tests for common change scenarios}
module lang::rascalcore::check::tests::ChangeScenarioTests

import lang::rascalcore::check::tests::StaticTestingUtils;
import lang::rascalcore::check::TestConfigs;
import util::Reflective;
import util::Benchmark;
import IO;
import List;

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
    moduleB = "module B import A; int n = b + 1;";
    assert unexpectedTypeInModule(moduleB);
    assert checkModuleOK("module A public int b = 0;"); // change b to type int
    return checkModuleOK(moduleB);
}

test bool fixErrorInExtend(){
    clearMemory();
    assert checkModuleOK("module A bool b = false;");
    moduleB = "module B extend A; int n = b + 1;";
    assert unexpectedTypeInModule(moduleB);
    assert checkModuleOK("module A int b = 0;"); // change b to type int
    return checkModuleOK(moduleB);
}

test bool introduceErrorInImport(){
    clearMemory();
    assert checkModuleOK("module A public int b = 0;");
    moduleB = "module B import A; int n = b + 1;";
    assert checkModuleOK(moduleB);
    assert checkModuleOK("module A public bool b = false;");
    return unexpectedTypeInModule(moduleB);
}

test bool introduceErrorInExtend(){
    clearMemory();
    assert checkModuleOK("module A int b = 0;");
    moduleB = "module B extend A; int n = b + 1;";
    assert checkModuleOK(moduleB);
    assert checkModuleOK("module A bool b = false;");
    return unexpectedTypeInModule(moduleB);
}

test bool removeImportedModuleAndRestoreIt1(){
    clearMemory();
    assert checkModuleOK("module A");
    moduleB = "module B import A;";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);
    assert checkModuleOK("module A");
    return checkModuleOK(moduleB);
}

test bool removeImportedModuleAndRestoreIt2(){
    clearMemory();
    moduleA = "module A int twice(int n) = n * n;";
    assert checkModuleOK(moduleA);
    moduleB = "module B import A; int quad(int n) = twice(twice(n));";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);
    assert checkModuleOK(moduleA);
    return checkModuleOK(moduleB);
}

test bool removeExtendedModuleAndRestoreIt1(){
    clearMemory();
    moduleA = "module A";
    assert checkModuleOK(moduleA);
    moduleB = "module B extend A;";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);
    assert checkModuleOK(moduleA);
    return checkModuleOK(moduleB);
}

test bool removeExtendedModuleAndRestoreIt2(){
    clearMemory();
    moduleA = "module A int twice(int n) = n * n;";
    assert checkModuleOK(moduleA);
    moduleB = "module B extend A; int quad(int n) = twice(twice(n));";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);
    assert checkModuleOK(moduleA);
    return checkModuleOK(moduleB);
}

test bool removeOverloadAndRestoreIt(){
    clearMemory();
    moduleA1 = "module A
                int dup(int n) = n + n;
                str dup(str s) = s + s;";
    moduleA2 = "module A
                int dup(int n) = n + n;";
    assert checkModuleOK(moduleA1);
    moduleB = "module B import A;  str f(str s) = dup(s);";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);

    assert checkModuleOK(moduleA2);
    assert argumentMismatchInModule(moduleB);
    assert checkModuleOK(moduleA1);
    return checkModuleOK(moduleB);
}

test bool removeConstructorAndRestoreIt(){
    clearMemory();
    moduleA1 = "module A
                data D = d(int n) | d(str s);";
    moduleA2 = "module A
                data D = d(int n);";
    assert checkModuleOK(moduleA1);
    moduleB = "module B import A;  D f(str s) = d(s);";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);

    assert checkModuleOK(moduleA2);
    assert argumentMismatchInModule(moduleB);
    assert checkModuleOK(moduleA1);
    return checkModuleOK(moduleB);
}

// ---- incremental type checking ---------------------------------------------

// Legend:
//      X --> Y: X import Y
//      X ==> Y: X extends Y
//      X >>> Y: replace X by Y
//      *X     : check starts at X
//      X!     : X is (re)checked

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
    moduleA1 = "module A";
    moduleA2 = "module A
                    public int n = 3;";
    moduleA3 = "module A
                    public int n = 3;
                    data D = d1();";

    assert expectReChecks(moduleA1, ["A"]);    // I

    moduleB1 = "module B
                    import A;";
    moduleB2 = "module B
                    import A;
                    public int m = n + 1;";
    assert expectReChecks(moduleB1, ["B"]);    // II

    writeModule(moduleA2);
    assert expectReChecks(moduleB1, ["A"]);    // III

    moduleC1 = "module C
                    import B;
                    int f() = 2;";
    assert expectReChecks(moduleC1, ["C"]);    // IV

    writeModule(moduleA3);

    assert expectReChecks(moduleC1, ["A"]);    // V

    writeModule(moduleB2);
    return expectReChecks(moduleC1, ["B"]);    // VI
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
    moduleA1 = "module A";
    moduleB1 = "module B import A;";
    moduleC1 = "module C import B;";
    moduleD1 = "module D import A; import B;";
    moduleE1 = "module E import C; import D;";

    moduleB2 = "module B import A; int n = 0;";

    writeModule(moduleA1);
    writeModule(moduleB1);
    writeModule(moduleC1);
    writeModule(moduleD1);
    writeModule(moduleE1);

    assert expectReChecks(moduleE1, ["A", "B", "C", "D", "E"]);    // I

    writeModule(moduleB2);
    return expectReChecks(moduleC1, ["B"]);                        // II
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
    moduleA1 = "module A";
    moduleB1 = "module B import A; int b() = 1;";
    moduleC1 = "module C import B; int c() = b();";
    moduleD1 = "module D import A; import B;";
    moduleE1 = "module E import C; import D;";

    moduleB2 = "module B import A; int b(int n) = n;";

    writeModule(moduleA1);
    writeModule(moduleB1);
    writeModule(moduleC1);
    writeModule(moduleD1);
    writeModule(moduleE1);
                                                        // I
    assert expectReChecks(moduleE1, ["A", "B", "C", "D", "E"]);

    writeModule(moduleB2);
    return expectReChecksWithErrors(moduleC1, ["B", "C"]);  // II
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
    moduleA1 = "module A";
    moduleA2 = "module A int a() = 0;";
    moduleB1 = "module B import A; int b() = 1;";
    moduleC1 = "module C import B; int c() = b();";
    moduleC2 = "module C import B; import D; int c() = b();";
    moduleD1 = "module D import A;";

    writeModule(moduleA1);
    writeModule(moduleB1);
    writeModule(moduleC1);

    assert expectReChecks(moduleC1, ["A", "B", "C"]);  // I

    writeModule(moduleD1);
    assert expectReChecks(moduleD1, ["D"]);            // II

    writeModule(moduleA2);
    assert expectReChecks(moduleC1, ["A"]);            // III

     writeModule(moduleC2);
     return expectReChecks(moduleC2, ["C", "D"]);      // IV
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
    moduleException1 = "module Exception";
    moduleException2 = "module Exception
                            int n = 0;";
    moduleMath = "module Math import Exception; import List; ";
    moduleList = "module List import Map; import Exception; ";
    moduleMap = "module Map";
    moduleSet1 = "module Set import List; import Exception; import Math;";
    moduleSet2 = "module Set import List; import Exception; import Math;
                        int m = 0;";
    moduleTop = "module Top  import List; import Set; ";

    writeModule(moduleException1);
    writeModule(moduleMath);
    writeModule(moduleList);
    writeModule(moduleMap);
    writeModule(moduleSet1);
    writeModule(moduleTop);

    assert expectReChecks(moduleTop, ["Exception", "Math", "List", "Map", "Set", "Top"]);

    writeModule(moduleException2);
    assert expectReChecks(moduleTop, ["Exception"]);

    writeModule(moduleSet2);
    assert expectReChecks(moduleTop, ["Set"]);

    writeModule(moduleException1);
    writeModule(moduleSet1);
    return expectReChecks(moduleTop, ["Exception", "Set"]);
}
// ---- touch and recheck modules ---------------------------------------------

bool touchAndCheck(loc topModule, list[str] moduleNames, PathConfig pcfg){
    println("TOUCH <moduleNames>");
    for(mname <- moduleNames){
        touch(getModuleLocation(mname, pcfg));
    }
    return expectReChecks(topModule, moduleNames, pathConfig=pcfg);
}

test bool onlyTouchedModulesAreReChecked1(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    topModule = getModuleLocation("analysis::grammars::Ambiguity", pcfg);
    assert checkModuleOK(topModule, pathConfig = pcfg);

    assert touchAndCheck(topModule, ["Exception"], pcfg);
    assert touchAndCheck(topModule, ["Set"], pcfg);
    assert touchAndCheck(topModule, ["Grammar"], pcfg);
    return touchAndCheck(topModule, ["Exception", "Set", "Grammar"], pcfg);
}

@ignore{Very expensive test}
test bool onlyTouchedModulesAreReChecked2(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    topModule = getModuleLocation("lang::rascalcore::check::Checker", pcfg);
    assert checkModuleOK(topModule, pathConfig = pcfg);

    assert touchAndCheck(topModule, ["Exception"], pcfg);
    assert touchAndCheck(topModule, ["Set"], pcfg);
    assert touchAndCheck(topModule, ["Exception", "Set"], pcfg);
    assert touchAndCheck(topModule, ["lang::rascalcore::check::CollectType"], pcfg);
    return touchAndCheck(topModule, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal", "lang::rascalcore::check::CollectType"], pcfg);
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

bool changeAndCheck(loc topModule, list[str] moduleNames, PathConfig pcfg){
    println("CHANGE <moduleNames>");
    changeModules(moduleNames, pcfg);
    return expectReChecks(topModule, moduleNames, pathConfig=pcfg);
}

test bool onlyChangedModulesAreReChecked1(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    topModule = getModuleLocation("analysis::grammars::Ambiguity", pcfg);
    assert checkModuleOK(topModule, pathConfig = pcfg);

    assert changeAndCheck(topModule, ["Exception"], pcfg);
    assert changeAndCheck(topModule, ["Set"], pcfg);
    assert changeAndCheck(topModule, ["Grammar"], pcfg);
    assert changeAndCheck(topModule, ["Exception", "Set", "Grammar"], pcfg);

    restoreModules(["Exception", "Set", "Grammar"], pcfg);
    return true;
}

@ignore{Very expensive test}
test bool onlyChangedModulesAreReChecked2(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    topModule = getModuleLocation("lang::rascalcore::check::Checker", pcfg);
    assert checkModuleOK(topModule, pathConfig = pcfg);

    assert changeAndCheck(topModule, ["Exception"], pcfg);
    assert changeAndCheck(topModule, ["Set"], pcfg);
    assert changeAndCheck(topModule, ["Exception", "Set"], pcfg);
    assert changeAndCheck(topModule, ["lang::rascalcore::check::CollectType"], pcfg);
    assert changeAndCheck(topModule, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal",
                                      "lang::rascalcore::check::CollectType"], pcfg);
    restoreModules(["Exception", "Set", "ParseTree", "analysis::typepal::TypePal",
                    "lang::rascalcore::check::CollectType"], pcfg);
    return true;
}

// Benchmarks for incremental type checking

map[str,num] benchmark(lrel[str, void()] cases){
    measurements = [];
	for (<str Name, runCase> <- cases) {
		measurements+= <Name, realTimeOf(runCase)>;
	}
    return measurements;
}

void miniBenchmarkRechecking(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    topModule = getModuleLocation("ParseTree", pcfg);

    cases =
        [<"ParseTree, first", void(){ checkModuleOK(topModule, pathConfig = pcfg); }>,
         <"ParseTree, nochange", void(){ checkModuleOK(topModule, pathConfig = pcfg); }>,
         <"Exception", void(){ touchAndCheck(topModule, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(topModule, ["Set"], pcfg); }>,
         <"Exception+Set", void(){ touchAndCheck(topModule, ["Exception", "Set"], pcfg); }>
        ];
    iprintln(benchmark(cases));
}

void mediumBenchmarkRechecking(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    topModule = getModuleLocation("analysis::grammars::Ambiguity", pcfg);

    cases =
        [<"analysis::grammars::Ambiguity, first", void(){ checkModuleOK(topModule, pathConfig = pcfg); }>,
         <"analysis::grammars::Ambiguity, nochange", void(){ checkModuleOK(topModule, pathConfig = pcfg); }>,
         <"Exception", void(){ touchAndCheck(topModule, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(topModule, ["Set"], pcfg); }>,
         <"Grammar", void(){ touchAndCheck(topModule, ["Grammar"], pcfg); }>,
         <"Exception+Set+Grammar", void(){ touchAndCheck(topModule, ["Exception", "Set", "Grammar"], pcfg); }>
        ];

    iprintln(benchmark(cases));
}

void largeBenchmarkRechecking(){
    pcfg = getAllSrcPathConfig();
    remove(pcfg.resources, recursive=true);
    topModule = getModuleLocation("lang::rascalcore::check::Checker", pcfg);

    cases =
        [<"lang::rascalcore::check::Checker", void(){ checkModuleOK(topModule, pathConfig = pcfg); }>,
         <"Exception", void(){ touchAndCheck(topModule, ["Exception"], pcfg); }>,
         <"Set", void(){ touchAndCheck(topModule, ["Set"], pcfg); }>,
         <"Exception+Set", void(){ touchAndCheck(topModule, ["Exception", "Set"], pcfg); }>,
         <"lang::rascalcore::check::CollectType", void(){ touchAndCheck(topModule, ["lang::rascalcore::check::CollectType"], pcfg); }>,
         <"5 modules changed", void(){ touchAndCheck(topModule, ["Exception", "Set", "ParseTree", "analysis::typepal::TypePal", "lang::rascalcore::check::CollectType"], pcfg); }>
        ];

    iprintln(benchmark(cases));
}