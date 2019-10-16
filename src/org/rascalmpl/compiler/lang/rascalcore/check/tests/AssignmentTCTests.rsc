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
   