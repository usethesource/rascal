module lang::rascal::tests::imports::ImportTests3

import lang::rascal::tests::imports::Mbase;

test bool Test31() = lang::rascal::tests::imports::Mbase::n == 2;

test bool Test32() = n == 2;

test bool Test33() = lang::rascal::tests::imports::Mbase::f(3) == 6;

test bool Test34() = f(3) == 6;	

test bool Test35() { 
	int n = 3;
	return n == 3;
}
