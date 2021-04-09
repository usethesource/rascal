module lang::rascalcore::compile::Examples::Tst1

layout Whitespace = [\ \t\n]*;

start syntax D = "d";
start syntax DS = D+;

value main() = (DS)`d d d`;
