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

// Was: unexpectedtType
test bool errorMap1() = checkOK("map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] *= [4]; M==(0:[\<1,4\>,\<2,4\>,\<3,4\>],1:[10,20,30]);");

test bool errorSet1() = unexpectedType("set[int] L = {1,2,3}; L *= {4}; L=={\<1,4\>,\<2,4\>,\<3,4\>};");

test bool annotationError1() = uninitialized(" X @pos = 1;");

test bool annotationError2() = uninitialized(" F X; X @pos += 1;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @pos;"]);

test bool annotationError3() = uninitialized(" F X; X @pos -= 1;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @pos;"]);

test bool annotationError4() = uninitialized(" F X; X @pos *= 1;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @pos;"]);

test bool annotationError5() = uninitialized(" F X; X @pos /= 1;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @pos;"]);
  
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
test bool SLICE11() = unexpectedType("void main(){ s = \"abcdefg\"; s[1..3] += 10; }");

test bool FIELD1() = checkOK("void main(){ x = d1(20); x.n = 30; }", initialDecls=["data D = d1(int n);"]);
test bool FIELD2() = checkOK("void main(){ x = d1(20); x.n += 30; }", initialDecls=["data D = d1(int n); "]);
test bool FIELD3() = unexpectedType("void main(){ x = d1(20); x.n += \"a\"; }", initialDecls=["data D = d1(int n);"]);

test bool FIELD4() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.s = \"A\"; }");
test bool FIELD5() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.s += \"A\"; }");
test bool FIELD6() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.n = 30; }");
test bool FIELD7() = checkOK("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.n += 30; }");
test bool FIELD8() = unexpectedType("void main(){ tuple[str s, int n] x = \<\"a\", 1\>; x.n = true; }");

test bool FIELDSUB1() = checkOK("void main(){ x = d1([0,1,2,3,4]); x.ns[1] = 10; }", initialDecls=["data D = d1(list[int] ns);"]);
test bool FIELDSUB2() = checkOK("void main(){ x = d1([0,1,2,3,4]); x.ns[1] += 10; }", initialDecls=["data D = d1(list[int] ns);"]);
test bool FIELDSUB3() = unexpectedType("void main(){ x = d1([0,1,2,3,4]); x.ns[1] = \"a\"; }", initialDecls=["data D = d1(list[int] ns);"]);

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