module lang::rascal::tests::concrete::recovery::bugs::MultiProdRecovery

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

layout Layout = [\ ]* !>> [\ ];

syntax Prog = Stat*;

syntax Stat 
    = Expr ";"
    | "{" Stat* "}";

syntax Expr 
    = Expr "+" Expr
    | Expr "-" Expr
    | "e";

test bool multiProdOk() = checkRecovery(#Prog, "{ e + e; }", []);

test bool multiProdOperatorError() = checkRecovery(#Prog, "{ e * e; }", ["* "]);
