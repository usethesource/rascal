module lang::rascal::tests::functionality::ConcreteSyntaxKeywordFields

import IO;
import Node;

syntax A = "a";
syntax B = "b" | [b]; // ambiguous on purpose

// we only allow declarations on Tree for now, for lack of a syntax to declare them on non-terminals.
data Tree(str y = "y");

// to be able to access the kw param feature, you have to remove the loc annotation first (until we remove annotations):
&T<:Tree get(&T<:Tree e) = delAnnotation(e, "loc");

test bool assignKw() {
   a = get((A) `a`);
   a.y = "2";
   return a.y == "2";
}

test bool eqTest() = get((A)`a`) == get((A)`a`);

test bool eqTest2() = get((A)`a`).y == get((A)`a`)[y="y"].y;

test bool eqTest3() = get((A)`a`) != get((A)`a`)[y="y"] && eqTest2() /* superfluous for doc purposes */;

test bool updateKw() = get((A)`a`)[y="z"].y == "z";

test bool neqKwTest1() = get((A)`a`)[y="z"] != (A)`a`;

test bool defKw() = get((A)`a`).y == "y";

test bool normalProd() = prod(sort("A"),[lit("a")],{}) := get((A)`a`)[y="z"].prod; 

test bool normalArgs() = [_] := get((A) `a`)[y="y"].args;



test bool ambTest() = get([B] "b").y == "y";

test bool ambTest2() = {_,_} := get([B] "b")[y="z"].alternatives; 

test bool ambTest3() = get([B] "b")[y="z"].y == "z";

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

