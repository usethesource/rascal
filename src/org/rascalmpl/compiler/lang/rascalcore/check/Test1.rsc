module lang::rascalcore::check::Test1 
 lexical INT = [0-9]+;
 
 lexical STR = "\"" ![\"]* "\"";
 
 syntax EXP[&T]
            = con: &T con 
            | right( mul: EXP[&T] lhs "*" EXP[&T] rhs
            > 
                add: EXP[&T] lhs "+" EXP[&T] rhs !>> [0-9]
            )
            ;
           EXP[INT] exp1;
           EXP[INT] exp2 = add(exp1, exp1);