module lang::rascalcore::compile::Examples::Tst1

import ParseTree;
layout Whitespace = [\ \t\n]*;

start syntax D = "d";

value main() = (start[D])`d` := parse(#start[D], " d ");
//value main() = (D)`d` := parse(#start[D], " d ").top;