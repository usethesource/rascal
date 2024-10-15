@bootstrapParser
module lang::rascalcore::check::tests::CallTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool testUninit1() = undeclaredVariable("zap(1,2);");

test bool CallError2() = argumentMismatchInModule("
	module CallError2
		int f(int n) {return 2*n;}
		int main() = f(\"abc\");
	");
test bool CallError3() = undeclaredVariable("zip::zap(1,2);");

test bool CallError4() = unexpectedType("zap = 10; zap(1,2);"); 
 
test bool CallError5() = unexpectedType("int f() {return \"a\";}");	

test bool CallError6() = unexpectedType("int f(){ }");
  
test bool CallError8() = undeclaredVariableInModule("
	module CallError8
		int f(int n) {return n;}
		void main() { f(undef); }
	");

test bool KeywordError1() = argumentMismatchInModule("
	module KeywordError1
		int incr(int x, int delta=1) = x + delta;	
		int main() = incr(delta=3);
	");
  	
test bool KeywordError2() = argumentMismatchInModule("
	module KeywordError2
		int incr(int x, int delta=1) = x + delta;
		int main() = incr(1,3);
	");

test bool KeywordError3() = argumentMismatchInModule("
	module KeywordError3
		int incr(int x, int delta=1) = x + delta;
		int main() = incr(1,delta=\"a\");
	");

test bool KeywordError4() = argumentMismatchInModule("
	module KeywordError4
		int incr(int x, int delta=1) = x + delta;
		int main() = incr(3,d=5);
	");
  
test bool KeywordError5() = argumentMismatchInModule("
	module KeywordError5
		int add1(int x) = x + 1;
		int main() = add1(3,delta=5);
	");
  	
test bool KeywordInConstructorError1() = argumentMismatchInModule("
	module KeywordInConstructorError1
		data D = d(int x, int y = 3);
		data D1 = d1(int x);
		D1 main() = d1();
	");
  	
test bool KeywordInConstructorError2() = argumentMismatchInModule("
	module KeywordInConstructorError2
		data D = d(int x, int y = 3);
		data D1 = d1(int x);
		D main() = d(y=4);
	");

test bool KeywordInConstructorError3() = argumentMismatchInModule("
	module KeywordInConstructorError3
		data D = d(int x, int y = 3);
		data D1 = d1(int x);
		D main() = d(1,4);
	");
  	
test bool KeywordInConstructorError4() = argumentMismatchInModule("
	module KeywordInConstructorError4
		data D = d(int x, int y = 3);
		data D1 = d1(int x);
		D main() = d(1,y=\"a\");
	");

test bool KeywordInConstructorError5() = argumentMismatchInModule("
	module KeywordInConstructorError5
		data D = d(int x, int y = 3);
		data D1 = d1(int x);
		D main() = d(1,z=4);
	");
  	
test bool KeywordInConstructorError6() = argumentMismatchInModule("
	module KeywordInConstructorError6
		data D = d(int x, int y = 3);
		data D1 = d1(int x);
		D1 main() = d1(1,y=4);
	");

test bool FunctionParameter() = checkModuleOK("
	module FunctionParameter
		data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);
		TYPESET simp(TYPESET  ts) = ts;
		bool testSimp(TYPESET ats, TYPESET (TYPESET  ts) aSimp) = ats == aSimp(ats);
		bool main() = testSimp(SET(\"a\"), simp);
	");