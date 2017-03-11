module lang::rascal::tests::functionality::ConcreteSyntaxKeywordFields

import IO;
import Node;
import ParseTree;

syntax A = "a";
syntax B = "b" | [b]; // ambiguous on purpose

// we only allow declarations on Tree for now, for lack of a syntax to declare them on non-terminals.
data Tree(str y = "y");

// to be able to access the kw param feature, you have to remove the loc annotation first (until we remove annotations):
&T<:Tree get(&T<:Tree e) = delAnnotation(e, "loc");
@ignoreCompiler{FIX: Not implemented}
test bool assignKw() {
   a = get((A) `a`);
   a.y = "2";
   return a.y == "2";
}
@ignoreCompiler{FIX: Not implemented}
test bool eqTest() = get((A)`a`) == get((A)`a`);
@ignoreCompiler{FIX: Not implemented}
test bool eqTest2() = get((A)`a`).y == get((A)`a`)[y="y"].y;
@ignoreCompiler{FIX: Not implemented}
test bool eqTest3() = get((A)`a`) != get((A)`a`)[y="y"] && eqTest2() /* superfluous for doc purposes */;
@ignoreCompiler{FIX: Not implemented}
test bool updateKw() = get((A)`a`)[y="z"].y == "z";
@ignoreCompiler{FIX: Not implemented}
test bool neqKwTest1() = get((A)`a`)[y="z"] != (A)`a`;
@ignoreCompiler{FIX: Not implemented}
test bool defKw() = get((A)`a`).y == "y";
@ignoreCompiler{FIX: Not implemented}
test bool normalProd() = prod(sort("A"),[lit("a")],{}) := get((A)`a`)[y="z"].prod; 
@ignoreCompiler{FIX: Not implemented}
test bool normalArgs() = [_] := get((A) `a`)[y="y"].args;


@ignoreCompiler{FIX: Not implemented}
test bool ambTest() = get([B] "b").y == "y";
@ignoreCompiler{FIX: Not implemented}
test bool ambTest2() = {_,_} := get([B] "b")[y="z"].alternatives; 
@ignoreCompiler{FIX: Not implemented}
test bool ambTest3() = get([B] "b")[y="z"].y == "z";
@ignoreCompiler{FIX: Not implemented}
test bool ambTest4() {
   t = get([B] "b");
   t.y = "z";
   return t.y == "z";
}

test bool charTest() = get(char(32)).y == "y";

test bool charTest2() = get(char(32))[y="z"].y == "z";

test bool charTest3() = get(char(32))[y="z"].character == 32;

test bool charTest4() {
   t = get(char(32));
   t.y = "z";
   return t.y == "z";
}

