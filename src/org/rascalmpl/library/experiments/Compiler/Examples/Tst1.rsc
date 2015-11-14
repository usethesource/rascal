module experiments::Compiler::Examples::Tst1

import Map;

test bool notequal3(map[&K,&V] M1, map[&K,&V] M2)
    = (M1 != M2) ==>
    (domain(M1) != domain(M2)
    || range(M1) != range(M2)
    || isEmpty(M1) 
    || any(x <- M1, M1[x] != M2[x]));