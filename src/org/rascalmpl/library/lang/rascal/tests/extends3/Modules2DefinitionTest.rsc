module lang::rascal::tests::extends3::Modules2DefinitionTest

import lang::rascal::grammar::definition::Parameters;
import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Names;
import lang::rascal::grammar::definition::Layout;
import util::Reflective;
import Grammar;
import IO;

test bool layoutPropagationOverImportAndExtendTest() {
   testModules = { m | m <- |project://rascal/src/org/rascalmpl/library/lang/rascal/tests/extends3/|.ls, /[A-F].rsc/ := m.file };

   trees = {parseModuleWithSpaces(m).top | m <- testModules, bprintln(m)};

    for (m <- trees) {
        str main = "<m.header.name>";
        
        def = modules2definition(main, trees);
        
        gr = resolve(fuse(layouts(def)));

        // the L1 layout definition should have propagated to all of the modules
        assert layouts("L1") in gr.rules : "L1 is not in <main>";
    }

    return true;
}