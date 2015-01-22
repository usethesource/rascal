module experiments::Compiler::Examples::Tst1

import lang::rascal::tests::imports::M1;

//test bool Test11() = lang::rascal::tests::imports::M1::f(3) == 6;
//
//@ignore{}
//test bool Test12() = f(3) == 6;

 
value main(list[value] args) { 
	int f(int n) {return 3 * n;} 
	return f(3) == 9;
}