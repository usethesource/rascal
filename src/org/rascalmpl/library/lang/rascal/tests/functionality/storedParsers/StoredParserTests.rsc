module lang::rascal::tests::functionality::storedParsers::StoredParserTests

import lang::rascal::grammar::storage::ModuleParserStorage;
import util::Reflective;
import util::Eval;
import IO;
import Location;
import util::PathConfig;

void prepareStorage(PathConfig pcfg) {
    storeParsersForModules(
        {resolveLocation(|project://rascal/src/org/rascalmpl/library/lang/rascal/tests/functionality/storedParsers/ExampleModule.rsc|)}, 
        pcfg);
}

test bool testStorageAndUse() {
    pcfg = getProjectPathConfig(|project://rascal|);
    prepareStorage(pcfg);

    // first we run in the source environment. There is no cached parser 
    // there necause it is written to the target folder
    rt = createRascalRuntime(pcfg=pcfg);
    rt.eval(#void, "import lang::rascal::tests::functionality::storedParsers::ExampleModule;");
    
    // a bit of fiddling to allow the Rascal project itself to be a testbed for this configuration
    modLoc = srcsFile("lang::rascal::tests::functionality::storedParsers::ExampleModule", pcfg, fileConfig());
    generatedPath = pcfg.bin + relativize(pcfg.srcs, modLoc)[extension="parsers"].path;
    targetLibrary = pcfg.bin + "org/rascalmpl/library";
    requiredPath = ((pcfg.bin + "org/rascalmpl/library") + relativize(pcfg.srcs, modLoc).path)[extension="parsers"];
    println(requiredPath);
    copy(generatedPath, requiredPath);

    // we create a runtime which includes the generate .parser file, and we run again
    pcfg.srcs = [pcfg.bin + "org/rascalmpl/library"];
    rt = createRascalRuntime(pcfg=pcfg);
    rt.eval(#void, "import lang::rascal::tests::functionality::storedParsers::ExampleModule;");

    return result(true) := rt.eval(#bool, "testCompute()");
}





