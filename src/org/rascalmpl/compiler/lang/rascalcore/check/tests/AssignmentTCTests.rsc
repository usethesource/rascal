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
module lang::rascalcore::check::tests::AssignmentTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool testUninit1() = undeclaredVariable("zzz;");

test bool AssignmentError1() = unexpectedType("int n = 3; n = true;");

test bool AssignmentError2() = unexpectedType("{int i = true;};");

test bool AssignmentError3() = unexpectedType("{int n = 3; n = true;}");

test bool IntAddAssign() = checkOK("{int n = 3; n += 3;}");
test bool IntAddAssignError() = unexpectedType("{int n = 3; n += true;}");

test bool StrAddAssign() = checkOK("{str s = \"a\"; s += \"b\";}");
test bool StrAddAssignError1() = unexpectedType("{str s = \"a\"; s += 3;}");

test bool SetStrAddAssign1() = checkOK("{set[str] s = {\"a\"}; s += {\"b\"};}");
test bool SetStrAddAssign2() = checkOK("{set[str] s = {\"a\"}; s += \"b\";}");
test bool SetStrAddAssignError1() = unexpectedType("{set[str] s = {\"a\"}; s += 3;}");
test bool SetStrAddAssignError2() = unexpectedType("{set[str] s = {\"a\"}; s += [\"b\"];}");

test bool ListStrAddAssign1() = checkOK("{list[str] s = [\"a\"]; s += [\"b\"];}");
test bool ListStrAddAssign2() = checkOK("{list[str] s = [\"a\"]; s += \"b\";}");
test bool ListStrAddAssignError1() = unexpectedType("{list[str] s = [\"a\"]; s += 3;}");
test bool ListStrAddAssignError2() = unexpectedType("{list[str] s = [\"a\"]; s += {\"b\"};}");

test bool IntMullAssign() = checkOK("{int n = 3; n *= 3;}");
test bool IntMullAssignError() = unexpectedType("{int n = 3; n *= true;}");

test bool IntSubAssign() = checkOK("{int n = 3; n -= 3;}");
test bool IntSubAssignError() = unexpectedType("{int n = 3; n -= true;}");

test bool IntDivAssign() = checkOK("{int n = 3; n /= 3;}");
test bool IntDivAssignError() = unexpectedType("{int n = 3; n /= true;}");

test bool IntMulAssign() = checkOK("{int n = 3; n *= 3;}");
test bool IntMulAssignError() = unexpectedType("{int n = 3; n *= true;}");

test bool IntInterAssignError() = unexpectedType("{int n = 3; n &= 3;}");
test bool SetStrInterAssign() = checkOK("{set[str] s = {\"a\", \"b\"}; s &= {\"b\"};}");
test bool SetStrInterAssignError() = unexpectedType("{set[str] s = {\"a\", \"b\"}; s &= {3};}");

test bool integerError1() = uninitialized("N += 2;");

test bool integerError2() = uninitialized("N -= 2;");

test bool integerError3() = uninitialized("N *= 2;");

test bool integerError4() = uninitialized("N /= 2;");

test bool errorList1() = unexpectedType("list[int] L = {1,2,3}; L *= [4];  L==[\<1,4\>,\<2,4\>,\<3,4\>];");

test bool errorMap1() = unexpectedType("
          map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]);
          M[0] *= [4];
          M==(0:[\<1,4\>,\<2,4\>,\<3,4\>],1:[10,20,30]);
     ");

test bool errorSet1() = unexpectedType("set[int] L = {1,2,3}; L *= {4}; L=={\<1,4\>,\<2,4\>,\<3,4\>};");

test bool AnnotationError1() = uninitialized(" X @pos = 1;");

test bool AnnotationError2() = uninitializedInModule("
     module AnnotationError2
          data F = f() | f(int n) | g(int n) | deep(F f);
          anno int F @pos;
          void main() { F X; X @pos += 1; }
     ");

test bool AnnotationError3() = uninitializedInModule("
     module AnnotationError3
          data F = f() | f(int n) | g(int n) | deep(F f);
          anno int F @pos;
          void main() { F X; X @pos -= 1; }
     ");

test bool AnnotationError4() = uninitializedInModule("
     module AnnotationError4
          data F = f() | f(int n) | g(int n) | deep(F f);
          anno int F @pos;
          void main() { F X; X @pos *= 1; }
     ");

test bool AnnotationError5() = uninitializedInModule("
     module AnnotationError5
          data F = f() | f(int n) | g(int n) | deep(F f);
          anno int F @pos;
          void main() { F X; X @pos /= 1; }
     ");

test bool testInteger6() = uninitialized("N ?= 2; N==2;");

test bool testList9() = uninitialized("L ?= [4]; L==[4];");

test bool testMap6() = uninitialized(" M ?= (3:30); M==(3:30);");

test bool testSet6() = uninitialized(" L ?= {4}; L=={4};");

//////////////////////////////////////////////////////

test bool SUB1() = checkOK("void main(){ lst = [0,1]; lst[0] = 10; }");

test bool SUB2() = checkOK("void main(){ lst = [0,1]; lst[0] += 10; }");

test bool SUB3() = unexpectedType("void main(){ lst = [0,1]; lst[\"a\"] = 10; }");

test bool SUB4() = checkOK("void main(){ mp = (\"a\":1); mp[\"a\"] = 10; }");
test bool SUB5() = checkOK("void main(){ mp = (\"a\":1); mp[\"a\"] += 10; }");

test bool SUB6() = checkOK("void main(){ nd = \"f\"(0,\"a\"); nd[0] = 10; }");
test bool SUB7() = unexpectedType("void main(){ nd = \"f\"(0,\"a\"); nd[0] += 10; }");

test bool SUB8() = checkOK("void main(){ tp = \<0,\"a\",true\>; tp[0] = 10; }");
test bool SUB9() = checkOK("void main(){ tp = \<0,\"a\",true\>; tp[0] += 10; }");
test bool SUB10() = unexpectedType("void main(){ tp = \<0,\"a\",true\>; tp[1] += 10; }");

test bool SUB11() = unexpectedType("void main(){ tp = \<0,\"a\",true\>; tp[3] += 10; }");

test bool SLICE1() = checkOK("void main(){ lst = [0,1,2,3,4,5]; lst[1..3] = 10; }");
test bool SLICE2() = checkOK("void main(){ lst = [0,1,2,3,4,5]; lst[1..3] = \"a\"; }");
test bool SLICE3() = checkOK("void main(){ lst = [0,1,2,3,4,5]; lst[1..3] += 10; }");

test bool SLICE4() = checkOK("void main(){ lst = [0,1,2,3,4,5]; lst[..3] = 10; }");
test bool SLICE5() = checkOK("void main(){ lst = [0,1,2,3,4,5]; lst[..] = 10; }");
test bool SLICE6() = unexpectedType("void main(){ lst = [0,1,2,3,4,5]; lst[..\"a\"] = 10; }");

test bool SLICE7() = checkOK("void main(){ lst = [0,1,2,3,4,5]; lst[1,2..3] = 10; }");
test bool SLICE8() = checkOK("void main(){ lst = [0,1,2,3,4,5]; lst[1,2..3] += 10; }");

test bool SLICE9() = unexpectedType("void main(){ lst = [0,1,2,3,4,5]; lst[1,\"a\"..3] = 10; }");

test bool SLICE10() = checkOK("void main(){ s = \"abcdefg\"; s[1..3] = \"x\"; }");
test bool SLICE11() = checkOK("void main(){ s = \"abcdefg\"; s[1..3] += \"x\"; }");
test bool SLICE12() = unexpectedType("void main(){ s = \"abcdefg\"; s[1..3] += 10; }");

test bool FIELD1() = checkModuleOK("
     module FIELD1
          data D = d1(int n);
          void main(){ x = d1(20); x.n = 30; }
     ");

test bool FIELD2() = checkModuleOK("
     module FIELD2
          data D = d1(int n);
          void main(){ x = d1(20); x.n += 30; }
     ");
test bool FIELD3() = unexpectedTypeInModule("
     module FIELD3
          data D = d1(int n);
          void main(){ x = d1(20); x.n += \"a\"; }
     ");

test bool FIELD4() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.s = \"A\"; }");
test bool FIELD5() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.s += \"A\"; }");
test bool FIELD6() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.n = 30; }");
test bool FIELD7() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.n += 30; }");
test bool FIELD8() = unexpectedType("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.n = true; }");

test bool FIELDSUB1() = checkModuleOK("
     module FIELDSUB1
          data D = d1(list[int] ns);
          void main(){ x = d1([0,1,2,3,4]); x.ns[1] = 10; }
     ");

test bool FIELDSUB2() = checkModuleOK("
     module FIELDSUB2
          data D = d1(list[int] ns);
          void main(){ x = d1([0,1,2,3,4]); x.ns[1] += 10; }
     ");

test bool FIELDSUB3() = unexpectedTypeInModule("
     module FIELDSUB3
          data D = d1(list[int] ns);
          void main(){ x = d1([0,1,2,3,4]); x.ns[1] = \"a\"; }
     ");

test bool TUPLE1() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; \<a, b\> = x; a == \"a\"; b == 1;}");

test bool TUPLE2() = checkOK("void main(){ \<a, b\> = \<\"a\", 1\>; a == \"a\"; b == 1;}");
test bool TUPLE3() = unexpectedType("void main(){ \<a, b\> = \<\"a\", 1\>; b == \"a\"; }");

test bool TUPLE4() = checkOK("
               int head(list[int] l) = l[0];
               list[int] tail(list[int] l) = l[1..];
               void main(){
                    l = [1,2,3];
                    \<lhead,ltail\> = \<head(l), tail(l)\>;
                    lhead == 1;
                    ltail == [2,3];
               }");

test bool E1() = checkOK("value zz = 1 + 2;");
test bool E2() = checkOK("value zz = 1 + 2.5; ");
test bool E3() = unexpectedType("value zz = 1 + true; ");

test bool And1() = checkOK("value zz = true && false; ");
test bool And2() = unexpectedType("value zz = 1 && false; ");
test bool And3() = unexpectedType("value zz = true && \"abc\"; ");

test bool Or1() = checkOK("value zz = true || false; ");
test bool Or2() = unexpectedType("value zz = 1 || false; ");
test bool Or3() = unexpectedType("value zz = true || \"abc\"; ");

test bool Eq1() = checkOK("value zz = 1 == 1; ");
test bool Eq2() = unexpectedType("value zz = 1 == \"a\"; ");

test bool Lst1() = checkOK("value zz = []; ");
test bool Lst2() = checkOK("value zz = [1,2] + 1; ");
test bool Lst3() = checkOK("value zz = 1 + [2,3]; ");
test bool Lst4() = checkOK("value zz = 1 + []; ");
test bool Lst5() = checkOK("value zz = [] + 1; ");
test bool Lst6() = checkOK("value zz = 1 + [1.5]; ");
test bool Lst7() = checkOK("value zz = 1 + [true]; ");

test bool Set1() = checkOK("value zz = {}; ");
test bool Set2() = checkOK("value zz = {1,2} + 1; ");
test bool Set3() = checkOK("value zz = 1 + {2,3}; ");
test bool Set4() = checkOK("value zz = 1 + {}; ");
test bool Set5() = checkOK("value zz = {} + 1; ");
test bool Set6() = checkOK("value zz = 1 + {1.5}; ");
test bool Set7() = checkOK("value zz = 1 + {true}; ");

test bool Stat1() = checkOK("value zz = 1 + {true, 2}; ");
test bool Stat2() = checkOK("{int n = 1;};");
@ignore{type inference fails}
test bool Stat3() = checkOK("{ n = 1; n = true; };");
@ignore{type inference fails}
test bool Stat4() = checkOK("{ n = 1; n = 1.5; n + 2;};");

test bool Stat5() = checkOK("value zz = { n = 1; m = n; n + 2;}; ");
@ignore{type inference fails}
test bool Stat6() = checkOK("{ n = 1; m = n;  m = 1.5; n + 2; n;};");
test bool Stat7() = checkOK("{ l = []; l = l + 1.5; l;}; ");
test bool Stat8() = checkOK("{ l = []; m = l; l = m + 1.5; l; };");
test bool Stat9() = checkOK("{ l = []; m = l; l = l + 1.5; l;};");
test bool Stat10() = checkOK("{ l = []; m = l; m = m + 1.5; m;};");
test bool Stat11() = checkOK("{ l = []; m = l; n = m; m = m + 1.5; n = n + 2r3; n;};");

test bool IfElse1() = checkOK("if(true) 10; else 11;; ");
test bool IfElse2() = unexpectedType("if(1) 10; else 11;");

test bool IfThen1() = illegalUse("value zz = { if(true) 10;}; ");
test bool IfThen2() = illegalUse("zz = { if(true) 10;}; ");

test bool IfThen3() = illegalUseInModule("
     module IfThen3
          value zz = { if(true) 10;};
     ");

test bool IfThen4() = unexpectedType("value zz = { if(1) 10; }; ");