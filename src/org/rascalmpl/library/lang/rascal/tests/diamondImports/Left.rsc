module lang::rascal::tests::diamondImports::Left

import lang::rascal::tests::diamondImports::Bottom;

data Exp
    = or(Exp lhs, Exp rhs)
    | maybe()
    | \true()
    | \false()
    ;

data Exp2 = and(Exp lhs, Exp rhs);

public str global = "World";