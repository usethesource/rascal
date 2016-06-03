module lang::rascal::tests::functionality::ConcreteSyntaxKeywordFields

import IO;
import Node;

syntax Aap = "a";

data Tree(int x = 0);

test bool assignKw() {
   a = [Aap] "a";
   a = delAnnotation(a, "loc");
   a.x = 2;
   return a.x == 2;
}