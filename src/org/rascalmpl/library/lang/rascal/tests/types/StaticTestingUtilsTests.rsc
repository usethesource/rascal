module lang::rascal::tests::types::StaticTestingUtilsTests

import lang::rascal::tests::types::StaticTestingUtils;

// Sanity check on the testing utilities themselves

test bool testUtils01() = checkOK("13;");

test bool testUtils02() = checkOK("x;", initialDecls=["int x = 5;"]);

test bool testUtils03() = checkOK("d();", initialDecls=["data D = d();"]);

test bool testUtils04() = checkOK("d();", initialDecls=["data D = d() | d(int n);"]);

test bool testUtils05() = checkOK("d(3);", initialDecls=["data D = d() | d(int n);"]);

test bool testUtils06() = checkOK("t();", initialDecls=["data Bool = and(Bool, Bool) | t();"]);
	
test bool testUtils07() = checkOK("and(t(),t());", initialDecls=["data Bool = and(Bool, Bool) | t();"]);

test bool testUtils08() =  checkOK("and(t(),t());f();", initialDecls=["data Bool = and(Bool, Bool) | t();", "data Prop = or(Prop, Prop) | f();"]);

test bool testUtils09() = checkOK("NODE N = f(0, \"a\", 3.5);", initialDecls = ["data NODE = f(int a, str b, real c);"]);

test bool testUtils10() = checkOK("13;", importedModules = ["util::Math"]);
	
test bool testUtils11() = checkOK("max(3, 4);", importedModules = ["util::Math"]);

test bool testUtils12() = checkOK("size([1,2,3]);", importedModules=["Exception", "List"]);

