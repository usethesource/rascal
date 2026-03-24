module lang::rascal::tests::functionality::storedParsers::StoredParserTests

import lang::rascal::grammar::storage::ModuleParserStorage;


void prepareTests() {
    pcfg = getProjectPathConfig(|project://rascal|);
    storeParsersForModules(
        |project://rascal/src/org/rascalmpl/library/lang/rascal/tests/functionality/storedParsers/ExampleModule.rsc|, 
        pcfg);
}