module lang::rascal::tests::concrete::ModulesWithStoredParsers

import util::Reflective;
import IO;
import ParseTree;
import util::Math;
import lang::rascal::grammar::storage::ModuleParserStorage;

lexical W = [\ ];
layout L = W*;
lexical A = [A];
syntax As = A+;

test bool storeParserNonModule() {
  storeParsers(#As, |test-temp:///parsersA.jar|);
  p = loadParsers(|test-temp:///parsersA.jar|);

  return p(type(sort("As"), ()), "A A", |origin:///|) == parse(#As, "A A", |origin:///|);
}

loc constructExampleProject() {
    root = |test-temp:///example-prj-<"<arbInt()>">|;
    newRascalProject(root);
    return root;
}

// fix for target scheme not working for "non-existing" projects
PathConfig getTestPathConfig(loc root) 
    = getProjectPathConfig(root)[bin = root + "target/classes"];

test bool storeModuleParsersWorkedSimpleGrammar() {
    root = constructExampleProject();
    writeFile(root + "src/main/rascal/A.rsc", "module A
        'lexical A = [A];
        ");
    
    storeParsersForModules(getTestPathConfig(root));
    
    return exists(root + "target/classes/A.parsers");
}

test bool storeModuleParsersWorkedForBiggerGrammar() {
    root = constructExampleProject();
    writeFile(root + "src/main/rascal/A.rsc", "module A
        'lexical W = [\\ ];
        'layout L = W*;
        'lexical A = [A];
        'syntax As = A+;
        ");
    
    storeParsersForModules(getTestPathConfig(root));
    
    return exists(root + "target/classes/A.parsers");
}

