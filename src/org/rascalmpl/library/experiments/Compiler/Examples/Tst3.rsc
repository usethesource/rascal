module experiments::Compiler::Examples::Tst3

import Map;
import Set;
import Relation;

// toRel (on plain maps)
@ignoreCompiler{FIX: Typechecker says: Multiple functions found which could be applied} 
test bool toRel_g1() = toRel(()) == {};