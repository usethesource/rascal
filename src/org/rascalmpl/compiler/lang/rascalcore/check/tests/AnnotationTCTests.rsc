@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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
module lang::rascalcore::check::tests::AnnotationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
 
test bool AnnotationNotAllowed1() = unexpectedType("1 [@an=3];");

test bool AnnotationNotAllowed2() = unexpectedType("1@ann;");

test bool AnnotationNotAllowed31() = unexpectedTypeInModule("
    module AnnotationNotAllowed31
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        value main() = f()[@pos=true];
    ");
  	
test bool AnnotationNotAllowed41() = unexpectedTypeInModule("
    module AnnotationNotAllowed41
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        value main() = f() [@wrongpos=true];
    ");
 
test bool UndefinedValueError11() = uninitializedInModule("
    module UndefinedValueError11
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        void main(){
            F someF; 
            someF@pos;
    ");
 
test bool UndefinedValueError21() = uninitializedInModule("
    module UndefinedValueError21
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        void main(){
            F someF; s
            someF [@pos=3];
        }
    ");
test bool UninitializedVariableError1() = uninitializedInModule("
    module UninitializedVariableError1
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        value main(){
            F someF; 
            someF@pos = 3;
        }
    ");
  	