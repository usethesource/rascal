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
module lang::rascalcore::check::tests::ScopeTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool noEscapeFromToplevelMatch() = undeclaredVariable("bool a := true; a;");	

test bool localReunexpectedDeclaration1() = redeclaredVariable("int n; int n;");	

test bool localReunexpectedDeclaration2() = redeclaredVariable("int n = 1; int n;");	
	
test bool localReunexpectedDeclaration3() = redeclaredVariable("int n = 1; int n = 2;");	

test bool ifNoLeak1() = undeclaredVariable("if (int n := 3) {n == 3;} else  {n != 3;} n == 3;");	

test bool ifNoLeak2() = undeclaredVariable("if(int n \<- [1 .. 3], n\>=3){n == 3;}else{n != 3;} n == 3;");	

test bool blockNoLeak1() = undeclaredVariable("int n = 1; {int m = 2;} n == 1 && m == 2;");	

test bool innerImplicitlyDeclared() = undeclaredVariable("int n = 1; {m = 2;}; return (n == 1 && m == 2);");	

test bool varsInEnumeratorExpressionsShouldNotLeak() = undeclaredVariable("int n \<- [1,2]; n == 1;");	
	
test bool S1() = checkOK("
           void f(){
                x = 10; 
            }
");

test bool S2() = checkOK("
           void f(){
                int x = 10; 
            }
");
test bool S3() = unexpectedType("
           void f(){
                str x = 10; 
            }
");

test bool S4() = checkOK("
           void f(){
                x = 10; 
                y = x;
            }
");

test bool S5() = checkOK("
           void f(){
                x = 10; 
                y = x + 1;
            }
");

test bool S6() = checkOK("
           void f(){
                x = 10; 
                y = \"a\";
            }
");

test bool S7() = checkOK("
           void f(){
                x = 10; 
                y = \"a\";
                x = x + 1;
                y = y + \"b\";
            }
");

test bool S8() = unexpectedDeclaration("
           void f(){
                x = y; 
            }
");

test bool I1() = checkOK("
           void f(){
                x = 10;
                if(true){ x = 1; }
                x + 1; 
            }
");

test bool I2() = checkOK("
           void f(){
                x = 10;
                if(true){ x = x + 1; }
                x + 1; 
            }
");

test bool I3() = unexpectedDeclaration("
           void f(){
                if(true){ x = 1; }
                x + 1; 
            }
");

test bool I4() = checkOK("
           void f(){
                int x = 10;
                if(true){ x = x + 1; }
                x + 1; 
            }
");

test bool I5() = unexpectedType("
           void f(){
                int x = 10;
                if(true){ x = \"a\"; }
                x + 1; 
            }
");

test bool B1() = checkOK("
           void f(){
                if(x := 1){ x = x + 1; }
            }
");

test bool B2() = unexpectedDeclaration("
           void f(){
                if(x := 1){ x = x + 1; }
                x + 2;
            }
");
test bool B3() = checkOK("
           void f(){
                [x | x \<- {1,2,3} ];
            }
");

test bool B4() = checkOK("
           void f(){
                [x | x \<- {1,2,3}, x \> 0];
            }
");

test bool B5() = unexpectedDeclaration("
           void f(){
                [x | x \<- {1,2,3}, x \> 0];
                x + 1;
            }
");

test bool B6() = checkOK("
           void f(){
                (10 | it + x | x \<- {1,2,3});
            }
");

test bool W1() = unexpectedType("
           void f(){
                do { x=0; } while(x \> 0);
            }
");

test bool W2() = checkOK("
           void f(){
                while(x := 0) { x+1; };
            }
");

test bool N1() = checkOK("
           void f(){
                if(true){ x = 10; x = x + 1; }
                if(true){ y = \"a\"; y = y + \"b\"; } 
            }
");

test bool N2() = unexpectedDeclaration("
           void f(){
                if(true){ x = 10; x = x + 1; }
                if(true){ y = \"a\"; y = x + \"b\"; } 
            }
");

test bool N3() = unexpectedDeclaration("
           void f(){
                if(true){ x = 10; x = x + 1; }
                if(true){ y = \"a\"; y = y + \"b\"; } 
                x + 1;
            }
"); 
test bool N4() = checkOK("
           void f(){
                if(true){ x = 10; x = x + 1; }
                if(true){ x = \"a\"; x = x + \"b\"; } 
            }
");

test bool N5() = checkOK("
           void f(){
                if(true){ int x = 10; x = x + 1; }
                if(true){ x = \"a\"; x = x + \"b\"; } 
            }
");

test bool N6() = checkOK("
           void f(){
                if(true){ x = 10; x = x + 1; }
                if(true){ str x = \"a\"; x = x + \"b\"; } 
            }
");

test bool N7() = checkOK("
           void f(){
                if(true){ int x = 10; x = x + 1; }
                if(true){ str x = \"a\"; x = x + \"b\"; } 
            }
");

test bool N8() = checkOK("
           bool x = true;
           void f(){
                if(true){ int x = 10; x = x + 1; }
                if(true){ str x = \"a\"; x = x + \"b\"; } 
            }
");

test bool N9() = checkOK("
            void expand() {
              while (true) {
                instances = {};
                while (true) {
                   instances = instances ;
                }
              }
            }
");