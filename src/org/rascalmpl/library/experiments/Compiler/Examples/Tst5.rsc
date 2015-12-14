module experiments::Compiler::Examples::Tst5

import util::Math;

list[&T <: num] abs(list[&T <: num] nums) 
    = [abs(n) | n <- nums]; 