module lang::rascal::tests::imports::ImportTests3

import lang::rascal::tests::imports::Mbase;

test bool Test1() = lang::rascal::tests::imports::Mbase::n == 2;

test bool Test2() = n == 2;

test bool Test1() = lang::rascal::tests::imports::Mbase::f(3) == 6;

test bool Test2() = f(3) == 6;	

test bool Test3() { 
	int n = 3;
	return n == 3;
}
