module lang::rascalcore::check::tests::AnnotationUsingStdLibTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool posHasCorrectType()
    = checkOK("true;",
            initialDecls = ["void f(Tree t){ loc l = t@\\loc; }"],
            importedModules = ["ParseTree"]);