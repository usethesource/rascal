module lang::rascalcore::compile::Examples::Tst1

import ParseTree;

layout Whitespace = [\ \t\n]*;

start syntax D = "d";

value main() = parse(#D, "d");