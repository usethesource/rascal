module lang::rascalcore::compile::Examples::Tst1

import ParseTree;

layout Whitespace = [\ \t\n]*;

start syntax D = "d";
start syntax DS = D+;

value main() = /*(DS)`d d d` :=*/ parse(#DS, "d d d") ;