module lang::rascal::tests::concrete::StoredParsers

import util::Reflective;
import IO;
import util::Math;
import lang::rascal::grammar::storage::ModuleParserStorage;


loc constructExampleProject() {
    root = |test-temp:///example-prj-<"<arbInt()>">|;
    newRascalProject(root);
    return root;
}

// fix for target scheme not working for "non-existing" projects
PathConfig getTestPathConfig(loc root) 
    = getProjectPathConfig(root)[bin = root + "target/classes"];

test bool storeParsersWorkedSimpleGrammar() {
    root = constructExampleProject();
    writeFile(root + "src/main/rascal/A.rsc", "module A
        'lexical A = [A];
        ");
    
    storeParsersForModules(getTestPathConfig(root));
    
    return exists(root + "target/classes/A.parsers");
}

test bool storeParsersWorked() {
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