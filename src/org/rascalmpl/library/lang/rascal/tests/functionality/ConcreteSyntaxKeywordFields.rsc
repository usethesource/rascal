module lang::rascal::tests::functionality::ConcreteSyntaxKeywordFields

import IO;
import Node;

syntax A = "a";

data Tree(str y = "y");

&T<:Tree get(&T<:Tree e) = delAnnotation(e, "loc");

test bool assignKw() {
   a = get((A) `a`);
   a.y = "2";
   return a.y == "2";
}

test bool eqTest() = get((A)`a`) == get((A)`a`);

test bool updateKw() = get((A)`a`)[y="z"].y == "z";

test bool neqKwTest1() = get((A)`a`)[y="z"] != (A)`a`;

test bool defKw() = get((A)`a`).y == "y";

