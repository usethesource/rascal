@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::compile::Examples::Trans

// fix rel[int,int] t = r given rel[int,int] r;
// fix rel[int,int] t += t o r given rel[int,int] r;

import IO;
import Relation;
import util::Benchmark;
import util::Math;
import Set;

rel[int,int] trans0(rel[int,int] R){

  rel[int,int] T = R;
    
  solve (T) {
    T = T + (T o R);
  }

  return T;
}

map[int, set[int]] mappify(rel[int,int] r){
    map[int, set[int]] m = ();
    for(<a, b> <- r){
       defs = m[a] ? {};
       m[a] = defs + b;
    }
    return m;
}

rel[int, int] demappify_invert(map[int, set[int]] m) =
    { *{<a, b> | a <- m[b]}  | b <- m };

rel[int,int] trans1(rel[int,int] r){

    // initialize
    t = r;
    
    // carrier elements that can create a new tuple in the composition
    interact = range(t) & domain(r);   
    
    map[int, set[int]] r_forward = mappify(r);
    map[int, set[int]] t_backward = mappify(invert(t));
   
    work = interact;
    while(!isEmpty(work)){
        work1 = {};
        for(x <- work, a <- t_backward[x], b <- r_forward[x]){
            if(a notin t_backward[b]){
               t_backward[b] += a;
               work1 += {a, b};
            }
        };
       
        work = work1 & interact;
    }   

    // reconstruct t from mappified invert(t)
    return demappify_invert(t_backward);
}

value main() = measure();

test bool t0() = trans0({<1,2>, <2,3>, <3,4>}) == {<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};
test bool t1() = trans1({<1,2>, <2,3>, <3,4>}) == {<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};
test bool tt2(rel[int, int] r) = r+ == trans1(r);

rel[int,int] build(int n) = {<arbInt(3000), arbInt(3000)> | _ <- [0..n]};

value measure(){
    r = build(100000);
    println("r+:        <cpuTimeOf((){ r+; })/1000000>");
    println("trans0(r): <cpuTimeOf((){ trans0(r); })/1000000>");
    println("trans1(r):  <cpuTimeOf((){ trans1(r); })/1000000>");
    return true;
}