module experiments::Compiler::Examples::Tst1

import ParseTree;

layout Whitespace = [\ \t\n]*;

start syntax D = "d";
start syntax DS = D+;

value parseDS() = parse(#DS, "d d d");// == */ (DS)`d d d`;

value main() = parseDS();
