module lang::rascal::tests::functionality::storedParsers::StoredParserTests

import lang::rascal::grammar::storage::ModuleParserStorage;
import util::Reflective;
import util::Eval;
import IO;

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
    
    // we create a runtime which includes the generate .parser file, and we run again
    pcfg.srcs = [resolveLocation(|target:///|)];
    rt = createRascalRuntime(pcfg=pcfg);
    rt.eval(#void, "import lang::rascal::tests::functionality::storedParsers::ExampleModule;");

    return result(true) := rt.eval(#bool, "testCompute()");
}





