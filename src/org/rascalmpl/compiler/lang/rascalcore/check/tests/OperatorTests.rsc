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
module lang::rascalcore::check::tests::OperatorTests

import lang::rascalcore::check::tests::StaticTestingUtils;


// ---- is

// ---- has

// ---- transitive closure


test bool TC1() = checkOK("rel[int,int] r = {\<1,2\>}+;");

test bool TC2() = unexpectedType("rel[int,int] r = {\<1,\"a\"\>}+;");

// ---- reflexive transitive closure

test bool RTC1() = checkOK("rel[int,int] r = {\<1,2\>}*;");

test bool RTC2() = unexpectedType("rel[int,int] r = {\<1,\"a\"\>}*;");

test bool ID1() = unexpectedType("bool b = 1?;");

test bool NOT1() = checkOK("bool b = !true;");

test bool NEG1() = checkOK("int n = - 10;");

test bool SPL1() = checkOK("list[int] lst = [1, *[2, 3], 4];");

//-- composition

//-- product

test bool PRODN1() = checkOK("int x = 2 * 3;");
test bool PRODN2() = checkOK("num x = 2 * 3.5;");

test bool PRODR1() = checkOK("rel[int,int] x = {1, 2} * {3, 4};");
test bool PRODR2() = checkOK("rel[value,value] x = {1, \"a\"} * {3, \"a\"};");

test bool PRODLR1() = checkOK("lrel[int,int] x = [1, 2] * [3, 4];");
test bool PRODLR2() = checkOK("lrel[value,value] x = [1, \"a\"] * [3, \"a\"];");

test bool PRODE1() = unexpectedType("lrel[int,int] x = [1, 2] * 3;");

//---- join

test bool JOINR1() = checkOK(" rel[int,int,int,int] r = {\<1,10\>} join {\<2,20\>};");

test bool JOINR2() = unexpectedType(" rel[int,int] r = {\<1,10\>} join {\<2,20\>};");

test bool JOINR3() = checkOK(" rel[int a, int b,int c, str d] r = {\<1,10\>} join {\<2,\"a\"\>}; set[str] s = r.d;");

test bool JOINR4() = undefinedField(" rel[int a, int b,int, str d] r = {\<1,10\>} join {\<2,\"a\"\>}; set[str] s = r.d;");

test bool JOINLR1() = checkOK(" lrel[int,int,int,int] r = [\<1,10\>] join [\<2,20\>];");

test bool JOINLR2() = unexpectedDeclaration(" lrel[int,int] r = [\<1,10\>] join [\<2,20\>];");

test bool JOINLR3() = checkOK("lrel[int a, int b,int c, str d] r = [\<1,10\>] join [\<2,\"a\"\>]; list[str] s = r.d;");

test bool JOINLR4() = unexpectedDeclaration("lrel[int a, int b,int, str d] r = [\<1,10\>] join [\<2,\"a\"\>]; list[str] s = r.d;");

test bool JOINSR1() = checkOK("rel[int,int,bool] r = {\<1,10\>} join {true};");

test bool JOINSR2() = checkOK("rel[bool,int,int] r = {true} join {\<1,10\>};");

test bool JOINSR3() = unexpectedType(" rel[int,int,bool] r = {\<1,10\>} join true;");

// //-- addition

test bool ADD1() = checkOK("int n = 1 + 2;");
test bool ADD2() = checkOK("num n = 1 + 2.5;");
test bool ADD3() = checkOK("str s = \"a\" + \"b\";");
test bool ADD4() = checkOK("list[int] lst = [1,2] + [3,4];");
test bool ADD5() = checkOK("list[int] lst = 1 + [3,4];");
test bool ADD6() = checkOK("list[int] lst = [1,2] + 3;");
test bool ADD7() = checkOK("set[int] st = {1,2} + {3,4};");
test bool ADD8() = checkOK("set[int] st = 1 + {3,4};");
test bool ADD9() = checkOK("set[int] st = {1,2} + 3;");

test bool ATP1() = checkOK("tuple[int,str,int,bool] tp = \<1,\"a\"\> + \<2, true\>;");
test bool ATP2() = checkOK("tuple[int a, str b, int c, bool d] tp = \<1,\"a\"\> + \<2, true\>;  int n = tp.a; bool x = tp. d;");

test bool emptyRel1() = unexpectedType("{}\<0\> ;");

test bool emptyRel2() = unexpectedType("{}\<1\> ;");

test bool tupleOutOfBounds() = unexpectedType("{\<1,2\>}\<2\> == {2};"); 

