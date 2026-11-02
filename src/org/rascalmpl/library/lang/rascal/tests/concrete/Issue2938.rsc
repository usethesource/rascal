module lang::rascal::tests::concrete::Issue2938

import ParseTree;

layout L = [\ ]* !>> [\ ];

syntax Expr = id: [a-z]+ !>> [a-z] | paren: "(" Expr ")";

syntax L1 = {Expr ","}+;

syntax L2 = "[" {Expr!paren ","}+ "]";

start syntax S = L1 | L2;

test bool tryParser() = Expr _ := parse(#Expr, "(a)");