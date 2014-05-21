module tests::functionality::AssignmentTCTests

import StaticTestingUtils;
 
public test bool testUninit1() = undeclaredVariable("zzz;");

public test bool AssignmentError1() = unexpectedType("int n = 3; n = true;");
	
public test bool AssignmentError2() = unexpectedType("{int i = true;};");

public test bool AssignmentError3() = unexpectedType("{int n = 3; n = true;}");

public test bool integerError1() = uninitialized("N += 2;");
  
public test bool integerError2() = uninitialized("N -= 2;");
  	
public test bool integerError3() = uninitialized("N *= 2;");
 
public test bool integerError4() = uninitialized("N /= 2;");
 
public test bool errorList1() = unexpectedType("list[int] L = {1,2,3}; L *= [4];  L==[\<1,4\>,\<2,4\>,\<3,4\>];");

public test bool errorMap1() = unexpectedType("map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] *= [4]; M==(0:[\<1,4\>,\<2,4\>,\<3,4\>],1:[10,20,30]);");

public test bool errorSet1() = unexpectedType("set[int] L = {1,2,3}; L *= {4}; L=={\<1,4\>,\<2,4\>,\<3,4\>};");

public test bool annotationError1() = uninitialized(" X @ pos = 1;");

public test bool annotationError2() = uninitialized(" F X; X @ pos += 1;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]);

public test bool annotationError3() = uninitialized(" F X; X @ pos -= 1;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]);

public test bool annotationError4() = uninitialized(" F X; X @ pos *= 1;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]);

public test bool annotationError5() = uninitialized(" F X; X @ pos /= 1;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]);
  
public test bool testInteger6() = uninitialized("N ?= 2; N==2;"); 

public test bool testList9() = uninitialized("L ?= [4]; L==[4];"); 

public test bool testMap6() = uninitialized(" M ?= (3:30); M==(3:30);"); 

public test bool testSet6() = uninitialized(" L ?= {4}; L=={4};"); 	
   