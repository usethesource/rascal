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
@bootstrapParser
module lang::rascalcore::check::tests::ComprehensionTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool NonVoidListComprehension() = nonVoidTypeInModule("
    module NonVoidListComprehension
        void f() { return; }
        void main() { [f() | _ := 1]; }
    ");

test bool NonVoidSetComprehension() = nonVoidTypeInModule("
    module NonVoidSetComprehension
        void f() { return; }
            void main() { {f() | _ := 1}; }
    ");

test bool Gen1() = unexpectedType("{x | 5};");

test bool VoidFunctionPredicate1() = unexpectedType("void f() { } { x | int x \<- {1,2,3}, f() };");
  	
test bool UndefinedValue1() = undeclaredVariable("{ y | int x \<- {1,2,3}};");

test bool WrongListType() = cannotMatch("str S \<- [1,2,3];");

test bool WrongSetType() = cannotMatch("str S \<- {1,2,3};");

test bool WrongMapType() = cannotMatch("str S \<- (1:10,2:20);");

test bool WrongStringType() = cannotMatch(" int N \<- \"abc\";");

test bool WrongADTType1() = cannotMatchInModule("
    module WrongADTType1
        data Bool = btrue() | bfalse() | band(Bool lhs, Bool rhs) | bor(Bool lhs, Bool rhs); 
        void main() { int N \<- [true, true, false]; }
    ");

test bool NodeGenerator() = checkModuleOK("
    module NodeGenerator
        data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);
        void main() { [N | int N \<- f(i(1),g(i(2),i(3)))]; }
    ");

test bool AnyError() = unexpectedType("any(x \<- [1,2,3], \"abc\");");

test bool AllError() = unexpectedType("all(x \<- [1,2,3], \"abc\");");
  
test bool NoLeaking() = undeclaredVariable("{ X | int X \<- [1,2,3] }; X == 3;");

test bool NoLeakFromNextGenerator1() = undeclaredVariable("[\<N,M\> | int N \<- [1 .. 3], ((N==1) ? true : M \> 0), int M \<- [10 .. 12]] == [\<1,10\>,\<1,11\>,\<2,10\>,\<2,11\>];");  	
 
test bool NoLeakFromNextGenerator2() = undeclaredVariable(" [\<N,M\> | int N \<- [1 .. 3], ((N==1) ? true : M \> 0), int M := N] == [\<1,1\>,\<2,2\>];");  	

test bool EmptyTupleGeneratorError1() = checkOK("{\<X,Y\> | \<int X, int Y\> \<- {}} == {};");  	
  	
test bool EmptyTupleGeneratorError2() = checkOK("{\<X,Y\> | \<int X, int Y\> \<- []} == {};");  	
  
test bool EmptyTupleGeneratorError3() = checkOK("{\<X,Y\> | int X \<- {}, int Y \<- {}} == {};");	
   
test bool EmptyTupleGeneratorError4() = checkOK("{\<X,Y\> | int X \<- [], int Y \<- []} == {};");	