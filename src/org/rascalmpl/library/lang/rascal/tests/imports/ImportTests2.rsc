module lang::rascal::tests::imports::ImportTests2

import lang::rascal::tests::imports::M2;

test bool Test1() = lang::rascal::tests::imports::M2::n == 3;

test bool Test2() = n == 3;

test bool Test3() { 
	int n = 4;
	return n == 4;
}