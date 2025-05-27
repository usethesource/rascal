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
module lang::rascalcore::check::tests::ParameterizedTypes

import lang::rascalcore::check::tests::StaticTestingUtils;


test bool typeParamOK() = checkOK("&T f(&T x) = x;");
test bool newTypeParamInReturnNotOK() = unexpectedType("&T f(&S x) = x;");

test bool Issue1300a() = unexpectedTypeInModule("
        module Issue1300a
            data Wrap[&T] = wrap(&T val);
            &T id(&T arg) = arg;
        
            &T f(&T param) {
                Wrap[&T] x = wrap(param);
                   
                return id(x);
            }
        ");    
                                   
test bool Issue1300b() = unexpectedTypeInModule("
        module Issue1300b
            data Wrap[&T] = wrap(&T val);
            &T id(&T arg) = arg;      

            &S f(&S param) {
                Wrap[&S] x = wrap(param);
                return id(x);
            }
        ");
                                               
                                   
test bool Issue1300c() =
    unexpectedTypeInModule("
        module Issue1300c
            data Wrap[&T] = wrap(&T val);
            &T id(&T arg) = arg;
        
            &T f(&T param) {
                Wrap[&T] x = wrap(param);
                return x;
            }
        ");
                   
test bool Issue1300d() = checkModuleOK("
    module Issue1300d
        data Wrap[&T] = wrap(&T val);
        &T id(&T arg) = arg;

        &T f(&T param) {
            Wrap[&T] x = wrap(param);
            return x.val;
        }
    ");
                   
                                   
test bool issue1386a() = checkOK("bool f(type(symbol,definitions)) = true;");
                                   
test bool issue1386b() = checkOK("bool f(type[&T] _: type(symbol,definitions)) = true;");

test bool issue1386c() = checkOK("bool f(type[&T] x: type(symbol,definitions)) = true;");
 
test bool MaybeOK1() = checkModuleOK("
    module MaybeOK1       
        data Maybe[&T] = none() | just(&T arg);
    ");
            
test bool MaybeOK2() = checkModuleOK("
    module MaybeOK2
        data Maybe[&T] = none() | just(&T arg);
        Maybe[value] nn() = none();
    ");        
               
test bool MaybeNotK2() = unexpectedTypeInModule("
    module MaybeNotK2
        data Maybe[&T] = none() | just(&T arg);  
        Maybe[&S] nn() = none();
    ");       
            
            
test bool MaybeOK3() = checkModuleOK("
    module MaybeOK3
        data Maybe[&T] = none() | just(&T arg);
        Maybe[value] mb() { if(3 \> 2) return just(3); return just(\"Abc\"); }
    ");       
          
test bool MaybeNotOK3() = unexpectedTypeInModule("
    module MaybeNotOK3
        data Maybe[&T] = none() | just(&T arg);  
        Maybe[&S] mb() { if(3 \> 2) return just(3); return just(\"Abc\"); }
    ");        
            
test bool MaybeBoundOK() = checkModuleOK("
    module MaybeBoundOK
        data Maybe[&T] = none() | just(&T arg);
        Maybe[num] mb() { if(3 \> 2) return just(3); return just(1.5); }
    ");        
           

test bool MaybeBoundNotOK() = unexpectedTypeInModule("
    module MaybeBoundNotOK
        data Maybe[&T] = none() | just(&T arg);
        Maybe[&S \<: num] mb() { if(3 \> 2) return just(3); return just(1.5); }
    ");    
                          
test bool MaybeBoundViolated() = unexpectedTypeInModule("
    module MaybeBoundViolated
        data Maybe[&T] = none() | just(&T arg);               
        Maybe[&S \<: num] mb() { if(3 \> 2) return just(3); return just(\"Abc\"); }
    ");         
         
test bool BoundViolatedInCall() = unexpectedTypeInModule("
    module BoundViolatedInCall
        bool strange(&L \<: num _, &R \<: &L _) = false;
        value main() = strange(3, \"abc\");
    ");
       
test bool BoundOKInCall() = checkModuleOK("
    module BoundOKInCall
        bool strange(&L \<: num _, &R \<: &L _) = false;
        value main() = strange(3, 1.5);
    ");
        
test bool boundViolatedInFormals()
    =  unexpectedType("bool strange(&L \<: num _, &R \<: &L \<: str _) = false;");

test bool boundViolatedInFormalsIndirect1()  
    = unexpectedType("bool strange(&A \<: num _, &B \<: &A _, &B \<: str _) = false;");
    
test bool boundViolatedInFormalsIndirect2()      
    = unexpectedType("bool strange(&A \<: num _, &B \<: &A _, &S \<: str _, &B \<: &S _) = false;");

test bool circular()   
    = checkOK("bool strange(&A \<: &B _, &B \<: &A _) = false;"); 
 
 test bool circularBoundsViolated()
    = unexpectedType("bool strange(&A \<: &B \<: str _, &B \<: &A \<: num _) = false;");