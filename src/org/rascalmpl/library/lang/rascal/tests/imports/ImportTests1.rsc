module lang::rascal::tests::imports::ImportTests1

import lang::rascal::tests::imports::M1;

test bool Test1() = lang::rascal::tests::imports::M1::f(3) == 6;

test bool Test2() = f(3) == 6;

test bool Test3() { 
	int f(int n) {return 3 * n;} 
	return f(3) == 9;
}