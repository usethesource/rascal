module lang::rascal::tests::functionality::ParsingTests

import ParseTree;
import IO;

start syntax A = "a";
layout WS = [\ \t\n\r]*;

test bool strExpr() = [A] "a" == parse(#A,"a");

test bool locExpr() {
  writeFile(|tmp:///locExpr.txt|,"a");
  return [A] |tmp:///locExpr.txt| == parse(#A, |tmp:///locExpr.txt|);
}