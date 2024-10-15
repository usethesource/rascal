@bootstrapParser
module lang::rascalcore::check::tests::StaticTestingUtilsTests

import lang::rascalcore::check::tests::StaticTestingUtils;

// Sanity check on the testing utilities themselves

test bool TestUtils01() = checkOK("13;");

test bool TestUtils02() = checkModuleOK("
	module TestUtils02
		int main(){
			int x = 5;
			return x;
		}
	");

test bool TestUtils03() = checkModuleOK("
	module TestUtils03
		data D = d();
		D main() = d();
	");

test bool TestUtils04() = checkModuleOK("
	module TestUtils04
		data D = d() | d(int n);
		D main() = d();
	");

test bool TestUtils05() = checkModuleOK("
	module TestUtils05
		data D = d() | d(int n);
		D main() = d(3);
	");

test bool TestUtils06() = checkModuleOK("
	module TestUtils06
		data Bool = and(Bool, Bool) | t();
		Bool main() = t();
	");
	
test bool TestUtils07() = checkModuleOK("
	module TestUtils07
		data Bool = and(Bool, Bool) | t();
		Bool main() = and(t(),t());
	");

test bool TestUtils08() = checkModuleOK(
	module TestUtils08
		data Bool = and(Bool, Bool) | t();
		data Prop = or(Prop, Prop) | f();
		Prop main() {
			and(t(),t());
			return f();
		}
	");

test bool TestUtils09() = checkModuleOK("
	module TestUtils09
		data NODE = f(int a, str b, real c);
		NODE N = f(0, \"a\", 3.5);
	");

test bool TestUtils13(){
	writeModule("module MMM int x = 3;"); 
	return checkModuleOK(
		module TestUtils13
			import MMM;
			int main() = 13;
	");
}

test bool TestUtils14(){
	writeModule("module MMM int x = 3;"); 
	return undeclaredVariableInModule(
		module TestUtils14
			import MMM;
			int main() = x;
	");
}	

