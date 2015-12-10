module lang::rascal::tests::functionality::ParsingTests

import ParseTree;
import IO;

start syntax A = "a";
layout WS = [\ \t\n\r]*;

start syntax B = "b" | [a-z];

test bool strExpr() = [A] "a" == parse(#A,"a");

test bool allowAmb() = /amb(_) := parse(#B, "b", allowAmbiguity=true);

test bool disallowAmb() {
  try {
    parse(#B, "b");
    return false; // should have thrown Ambiguity exception
  }
  catch Ambiguity(_,_,_) :
    return true;
}

test bool disallowAmb2() {
  try {
    parse(#B, "b", allowAmbiguity=false);
    return false; // should have thrown Ambiguity exception
  }
  catch Ambiguity(_,_,_) :
    return true;
}

@ignoreCompiler{TC does not yet allow [A] loc}
test bool locExpr() {
  writeFile(|tmp:///locExpr.txt|,"a");
  return [A] |tmp:///locExpr.txt| == parse(#A, |tmp:///locExpr.txt|);
}