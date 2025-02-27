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
module lang::rascalcore::check::tests::InferenceTests

import lang::rascalcore::check::tests::StaticTestingUtils;
    
test bool nestedLocalVarDoesNotLeak1() = undeclaredVariable("int f(){ { x = true; } int n = x + 1;}");

test bool nestedLocalVarDoesNotLeak2() = undeclaredVariable("void f(){ { x = 1; } x += 1;}");

test bool parallelLocalVarsDoNotLeak() = checkOK("void f(){ { x = true; bool b = x;} { x = 1;  int n = x; } }");

test bool switchCasesDoNotLeak1()  = checkOK("void f(value v){ switch(v) { case str x: {str s = x;} case int x: {int n = x;}} }");

test bool switchCasesDoNotLeak2()  = undeclaredVariable("void f(value v){ switch(v) { case str x: {str s = x;} case int x: {int n = x;}} x;}");

test bool parallelIfDoesNotLeak() = checkOK("void f(value v){ if(str x := v) {str s = x;} if(int x := v) {int n = x;} }");

test bool parallelIfElseDoesNotLeak() = checkOK("void f(value v){ if(str x := v) {str s = x;} else {;} if(int x := v) {int n = x;} else {;} }");