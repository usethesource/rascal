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
module lang::rascalcore::compile::Examples::Tst3

import ParseTree;
import IO;

syntax Aas
  = nil: [a]*
  | a:   [a][a]*
  | aas: [a][a][a]*
  ;
  
&T <: Tree ambFilter(amb(set[&T <: Tree] alternatives)) {
  println("enter ambFilter");
  set[&T <: Tree] result = {a | &T <: Tree a <- alternatives, !(a is nil)};
  
  if ({oneTree} := result) {
    return oneTree;
  }
  println("ambFilter will return amb");
  res = ParseTree::amb(result);
  println("amb created");
  return res;
} 

value main() //test bool resolveableAmbIsGone() 
    = amb(_) !:= parse(#Aas, "a", allowAmbiguity=true, filters={ambFilter});

//test bool twoAmbsLeft() = amb({_,_}) := parse(#Aas, "aa", allowAmbiguity=true, filters={ambFilter});
