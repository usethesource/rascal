module lang::rascal::tests::imports::Imports1

import lang::rascal::tests::imports::M1;

test bool Test11() = lang::rascal::tests::imports::M1::f(3) == 6;

test bool Test12() = f(3) == 6;

 
test bool Test13() { 
	int f(int n) {return 3 * n;} 
	return f(3) == 9;
}
