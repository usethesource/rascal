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
module lang::rascalcore::compile::Examples::Tst4


import ParseTree;
import IO;
  
data P = prop(str name) | and(P l, P r) | or(P l, P r) | not(P a) | t() | f() | axiom(P mine = t());
data D[&T] = d1(&T fld);
data P(int size = 0);

//@ignore{Does not work after changed TypeReifier in compiler}
value main(){ //test bool allConstructorsHaveTheCommonKwParam(){
    iprintln( #P.definitions[adt("P",[])].alternatives);
    return  all (cons(_,_,kws,_) <- #P.definitions[adt("P",[])].alternatives, bprintln(kws), label("size", \int()) in kws);    
  //=  all(/choice(def, /cons(_,_,kws,_)) := #P.definitions, label("size", \int()) in kws);
}  
@ignoreCompiler{Does not work after changed TypeReifier in compiler}  
test bool axiomHasItsKwParam()
  =  /cons(label("axiom",_),_,kws,_) := #P.definitions && label("mine", \adt("P",[])) in kws;  

@ignore{Does not work after changed TypeReifier in compiler}  
test bool axiomsKwParamIsExclusive()
  =  all(/cons(label(!"axiom",_),_,kws,_) := #P.definitions, label("mine", \adt("P",[])) notin kws);
  
  
  