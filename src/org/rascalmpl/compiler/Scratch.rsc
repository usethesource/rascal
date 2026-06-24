module Scratch

import Prelude;
import lang::rascalcore::check::Checker;
import lang::rascalcore::check::ModuleLocations;
import util::FileSystem;
import util::SystemAPI;
import util::Benchmark;

import analysis::typepal::Solver;

loc rascalLib() {
    loc rascalLib = resolveLocation(|std:///|);
    if (/org.rascalmpl.library.?$/ := rascalLib.path) {
        rascalLib = rascalLib.parent.parent.parent;
    }
    return rascalLib;
}

loc typepalLib() {
    return |mvn://org.rascalmpl--typepal--0.17.0-RC4-SNAPSHOT/|;
}

int typecheckBirdCore(bool force = false) = realTimeOf(void() { typecheck(|home:///Desktop/bird/bird-core|, ["src/main/rascal"], force = force); });

int typecheckRascalLibraryAnalysis(bool force = false) = realTimeOf(void() { typecheck(|home:///Desktop/usethesource2/rascal|, ["src/org/rascalmpl/library"], dir = "analysis", force = force); });

int typecheckTypepal(bool force = false) = realTimeOf(void() { typecheck(|home:///Desktop/usethesource2/typepal|, ["src"], force = force); });
// 1454032
// 1207079 - remove check in Result constructor -- UNSOUND
// 1149042 - add cache for getSimpleVariable

bool typecheck(loc project, list[str] srcs, str dir = "", bool force = false) {
    
    // Create path config

    println();
    println("## Create path config");
    println();

    PathConfig pcfg = pathConfig(
        srcs = [project + src | str src <- srcs],
        bin = project + "target/classes",
        libs = [rascalLib()]
    );
    
    iprintln(pcfg);
    println();

    // Delete `bin`

    if (force) {
        println("## Delete `bin`");
        println();

        remove(pcfg.bin);

        iprintln(sort([*find(pcfg.bin, "tpl")]));
        println();
    }

    // Typecheck `srcs`

    println("## Typecheck `srcs`");
    println();

    list[str] moduleNames = [getRascalModuleName(l, pcfg) | loc src <- pcfg.srcs, loc l <- find(src + dir, "rsc")];

    iprintln(sort(moduleNames));
    println();

    calculatorFailureCount = 0;
    calculatorFailureNanoTime = 0;
    requirementFailureCount = 0;
    requirementFailureNanoTime = 0;
    ModuleStatus ms = rascalTModelForNames(moduleNames, rascalCompilerConfig(pcfg)[enableErrorFixes = false], dummy_compile1);

    iprintln(sort([*find(pcfg.bin, "tpl")]));
    println();

    return (true | it && found | str moduleName <- moduleNames, <bool found, _, _> := getTModelForModule(moduleName, ms));
}

void z1(int n = 10) {
    set[loc] locs = {};
    for (int i <- [0..n]) {
        locs += |unknown:///| + "<i>";
        // if (i % 1000 == 0) {
        //     println(i);
        // }
    }
}

void z2(int n = 10) {
    list[int] elems = [];
    for (int i <- [0..n]) {
        elems += i;
        if (i % (10*1000) == 0) {
            println(i);
        }
    }
}

void z3(int n = 10) {
    set[value] elems = {};
    for (int i <- [0..n]) {
        elems += "<i>";
    }
}

/*

6-tuples zijn duur, projectie is mogelijk goedkoper

  - overloaded type zijn triples (bijv. alleen 3e element gebruikt)
      - over het algemeen kort, sommige 100 overloads
  - defines zijn 6-tuples


Misschien vergelijkbaar met lange ADT constructors


komen zowel in Rascal typechecker als in typepal voor




binaire relaties geoptimaliseerd in Vallang. n-ary relaties niet zo erg.
6-tuple vervangen door 6 binary relaties????



Aanpak:

  - hoog niveau gaan meten welke delen van de checker de meeste tijd kosten

*/