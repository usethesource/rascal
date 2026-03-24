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
    rt = createRascalRuntime(pcfg=pcfg);
    rt.eval(#void, "import lang::rascal::tests::functionality::storedParsers::ExampleModule;");
    
    return result(true) := rt.eval(#bool, "testCompute()");
}





