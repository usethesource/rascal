@bootstrapParser
module lang::rascalcore::check::tests::AssignmentTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
 
test bool testUninit1() = undeclaredVariable("zzz;");

test bool AssignmentError1() = unexpectedType("int n = 3; n = true;");
	
test bool AssignmentError2() = unexpectedType("{int i = true;};");

test bool AssignmentError3() = unexpectedType("{int n = 3; n = true;}");

test bool integerError1() = uninitialized("N += 2;");
  
test bool integerError2() = uninitialized("N -= 2;");
  	
test bool integerError3() = uninitialized("N *= 2;");
 
test bool integerError4() = uninitialized("N /= 2;");
 
test bool errorList1() = unexpectedType("list[int] L = {1,2,3}; L *= [4];  L==[\<1,4\>,\<2,4\>,\<3,4\>];");

test bool errorMap1() = checkOK("map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] *= [4]; M==(0:[\<1,4\>,\<2,4\>,\<3,4\>],1:[10,20,30]);");

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
test bool Stat2() = checkOK("value zz = {int n = 1;}; ");
@ignore{type inference}
test bool Stat3() = checkOK("value zz = { n = 1; n = true; }; ");
@ignore{type inference}
test bool Stat4() = checkOK("value zz = { n = 1; n = 1.5; n + 2;}; ");

test bool Stat5() = checkOK("value zz = { n = 1; m = n; n + 2;}; ");
@ignore{type inference}
test bool Stat6() = checkOK("value zz = { n = 1; m = n;  m = 1.5; n + 2;}; ");
test bool Stat7() = checkOK("value zz = { l = []; l = l + 1.5; }; ");
test bool Stat8() = checkOK("value zz = { l = []; m = l; l = m + 1.5; }; ");
test bool Stat9() = checkOK("value zz = { l = []; m = l; l = l + 1.5; }; ");
test bool Stat10() = checkOK("value zz = { l = []; m = l; m = m + 1.5; }; ");
test bool Stat11() = checkOK("value zz = { l = []; m = l; n = m; m = m + 1.5; n = n + 2r3; }; ");

test bool IfElse1() = checkOK("value zz = { if(true) 10; else 11; }; ");
test bool IfElse2() = unexpectedType("value zz = { if(1) 10; else 11; }; ");

test bool IfThen1() = checkOK("value zz = { if(true) 10;}; ");      // TODO: check no value
test bool IfThen2() = unexpectedType("value zz = { if(1) 10; }; ");  // TODO: check no value