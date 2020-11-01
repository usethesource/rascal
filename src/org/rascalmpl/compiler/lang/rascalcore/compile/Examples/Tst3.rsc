module lang::rascalcore::compile::Examples::Tst3

import lang::rascalcore::compile::Examples::Tst2;

list[&T <: num] abs(list[&T <: num] nums) 
    = [abs(n) | n <- nums]; 