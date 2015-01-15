module lang::rascal::tests::types::StaticTestingUtilsTests

import lang::rascal::tests::types::StaticTestingUtils;

// Sanity check on the testing utilities themselves

test bool testUtils1() = checkOK("x;", initialDecls=["int x = 5;"]);

test bool testUtils2() = checkOK("d();", initialDecls=["data D = d();"]);

test bool testUtils3() = checkOK("d();", initialDecls=["data D = d() | d(int n);"]);

test bool testUtils4() = checkOK("d(3);", initialDecls=["data D = d() | d(int n);"]);

test bool testUtils5() = checkOK("t();", initialDecls=["data Bool = and(Bool, Bool) | t();"]);
	
test bool testUtils6() = checkOK("and(t(),t());", initialDecls=["data Bool = and(Bool, Bool) | t();"]);

test bool testUtils7() = 	checkOK("and(t(),t());f();", initialDecls=["data Bool = and(Bool, Bool) | t();", "data Prop = or(Prop, Prop) | f();"]);

test bool testUtils8() = checkOK("NODE N = f(0, \"a\", 3.5);", initialDecls = ["data NODE = f(int a, str b, real c);"]);
	
test bool testUtils8() = checkOK("max(3, 4);", importedModules = ["util::Math"]);

test bool testUtils9() = checkOK("size([1,2,3]);", importedModules=["Exception", "List"]);

