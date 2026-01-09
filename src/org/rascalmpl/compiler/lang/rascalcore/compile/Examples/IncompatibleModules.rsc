module lang::rascalcore::compile::Examples::IncompatibleModules

import IO;
import Map;
import Set;
import String;
import util::Reflective;
import lang::rascalcore::check::Checker;

// Rascal 0.41.0-RC42
public loc projectDir = |file:///Users/paulklint/git/rascal/|;

// Typepal 0.15.1, met Rascal dependency aangepast naar 0.41.0-RC42
public loc typepalLib = |mvn://org.rascalmpl--typepal--0.15.1-SNAPSHOT/|;

PathConfig pcfg = pathConfig(
    srcs = [ projectDir + "src/org/rascalmpl/library"
                   , projectDir + "src/org/rascalmpl/compiler"
                   , projectDir + "test/org/rascalmpl/benchmark"],
            bin = projectDir + "target/classes",
            libs = [typepalLib]
        );

void main() {
    mname = "lang::rascalcore::agrammar::tests::TestGrammars";
    ccfg = rascalCompilerConfig(pcfg);
    ms = rascalTModelForNames([mname], ccfg, dummy_compile1);
    <found, tm, ms> = getTModelForModule(mname, ms);
    println("Found: <found>");
    if (!found) {
        print("Messages: ");
        iprintln(ms.messages);

        <found, tm, ms> = getTModelForModule("analysis::typepal::FailMessage", ms);

        <names, ms> = isCompatibleBinaryLibrary(tm, ms);

        println("Names: ");
        iprintln(names);
    }
}

// Is binary library module compatible with its dependencies (originating from imports and extends)?
tuple[list[str], ModuleStatus] isCompatibleBinaryLibrary(TModel lib, ModuleStatus ms){
    libName = lib.modelName;
    set[loc] libLogical = domain(lib.logical2physical);
    set[loc] libDefines = { l | l <- libLogical, getModuleFromLogical(l) == libName };
    set[loc] libDependsOn = libLogical - libDefines;
    set[str] libDependsOnModules = { getModuleFromLogical(l) | l <- libDependsOn };
    set[loc] dependentsProvide = {};
    for(m <- libDependsOnModules){
       <found, tm, ms> = getTModelForModule(m, ms, convert=false);
       if(found){
           dependentsProvide += domain(tm.logical2physical);
       }
    }
    unsatisfied = libDependsOn - dependentsProvide;
    if(isEmpty(unsatisfied)){
        //println("isCompatibleBinaryLibrary <libName>: satisfied");
        return <[], ms>;
    } else {
        //println("isCompatibleBinaryLibrary, <libName> unsatisfied: <unsatisfied>");
        incompatibleModules = { split("/", u.path)[1] | u <- unsatisfied };
        return <toList(incompatibleModules), ms>;
    }
}
