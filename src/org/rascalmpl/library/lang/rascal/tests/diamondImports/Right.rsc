module lang::rascal::tests::diamondImports::Right

import lang::rascal::tests::diamondImports::Bottom;

data Exp 
    = and(Bool lhs, Bool rhs)
    ;

data Exp2 
    = or(Exp lhs, Exp rhs)
    ;

 public str global = "Hello"; 