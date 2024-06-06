module lang::rascal::tests::imports::Imports2

import lang::rascal::tests::imports::M2;

test bool Test21() = lang::rascal::tests::imports::M2::n == 3;

test bool Test22() = n == 3;

test bool Test23() { 
	int n = 4;
	return n == 4;
}
