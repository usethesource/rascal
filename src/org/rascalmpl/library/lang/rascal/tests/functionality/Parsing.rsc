module lang::rascal::tests::functionality::Parsing

import ParseTree;

start syntax A = "a";
layout WS = [\ \t\n\r]*;

test bool expr() = [A] "a" == parse(#A,"a");