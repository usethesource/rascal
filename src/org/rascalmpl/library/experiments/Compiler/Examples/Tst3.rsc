module experiments::Compiler::Examples::Tst3


import IO;
import Node;
import ParseTree;

syntax A = "a";

// we only allow declarations on Tree for now, for lack of a syntax to declare them on non-terminals.
data Tree(str y = "y");

// to be able to access the kw param feature, you have to remove the loc annotation first (until we remove annotations):
&T<:Tree get(&T<:Tree e) = delAnnotation(e, "loc");


//@ignoreCompiler{FIX: Not implemented}
test bool eqTest() = get((A)`a`) == get((A)`a`);

//@ignoreCompiler{FIX: Not implemented}
test bool eqTest2() = get((A)`a`).y == get((A)`a`)[y="y"].y;