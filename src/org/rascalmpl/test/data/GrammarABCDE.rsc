module GrammarABCDE

layout Whitespace = [\ \t\n]*;

start syntax A = "a";
start syntax B = "b";
start syntax C = A B;
start syntax D = "d";
start syntax DS = D+;
start syntax E = "e";
start syntax ES = {E ","}+;