module tests::functionality::CallTCTests

import StaticTestingUtils;

public test bool testUninit1() = undeclaredVariable("zap(1,2);");

public test bool callError2() = argumentMismatch("f(\"abc\");", initialDecls = ["int f(int n) {return 2*n;}"]);

public test bool callError3() = undeclaredVariable("zip::zap(1,2);");

public test bool callError4() = unexpectedType("zap = 10; zap(1,2);"); 
 
public test bool callError5() = unexpectedType("int f() {return \"a\";}");	

public test bool callError6() = unexpectedType("int f(){ }"); // TODO? missing return
  
public test bool callError8() = undeclaredVariable("f(undef);", initialDecls = ["int f(int n) {return n;}"]);

public test bool keywordError1() = argumentMismatch("incr(delta=3);", initialDecls = ["int incr(int x, int delta=1) = x + delta;"]);  	
  	
public test bool keywordError2() = argumentMismatch("incr(1,3);", initialDecls = ["int incr(int x, int delta=1) = x + delta;"]);  	

public test bool keywordError3() = argumentMismatch("incr(1,delta=\"a\");", initialDecls = ["int incr(int x, int delta=1) = x + delta;"]);  // TODO

public test bool keywordError4() = argumentMismatch("incr(3,d=5);", initialDecls = ["int incr(int x, int delta=1) = x + delta;"]);  	   // TODO
  
public test bool keywordError5() = argumentMismatch("add1(3,delta=5);", initialDecls = ["int add1(int x) = x + 1;"]);  	                    // TODO
  	data D = d(int x, int y = 3);
  	
public test bool keywordInConstructorError1() = argumentMismatch("d1();", initialDecls = ["data D = d(int x, int y = 3);", "data D1 = d1(int x);"]);  	
  	
public test bool keywordInConstructorError2() = argumentMismatch("d(y=4);", initialDecls = ["data D = d(int x, int y = 3);", "data D1 = d1(int x);"]);  	

public test bool keywordInConstructorError3() = argumentMismatch("d(1,4);", initialDecls = ["data D = d(int x, int y = 3);", "data D1 = d1(int x);"]);  	
  	
public test bool keywordInConstructorError4() = argumentMismatch("d(1,y=\"a\");", initialDecls = ["data D = d(int x, int y = 3);", "data D1 = d1(int x);"]);  // TODO	

public test bool keywordInConstructorError5() = argumentMismatch("d(1,z=4);", initialDecls = ["data D = d(int x, int y = 3);", "data D1 = d1(int x);"]);  	// TODO
  	
public test bool keywordInConstructorError6() = argumentMismatch("d1(1,y=4);", initialDecls = ["data D = d(int x, int y = 3);", "data D1 = d1(int x);"]);  	// TODO

public test bool functionParameter() = 
	checkOK("testSimp(SET(\"a\"), simp);",
		initialDecls=["data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);",
					  "TYPESET simp(TYPESET  ts) = ts;",
					  "bool testSimp(TYPESET ats, TYPESET (TYPESET  ts) aSimp) = ats == aSimp(ats);"
		]);