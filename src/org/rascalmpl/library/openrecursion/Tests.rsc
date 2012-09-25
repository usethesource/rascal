module openrecursion::Tests

import openrecursion::B;
import openrecursion::C;

public test bool testExtendedFib() {
	list[int] input = [0,1,2,3,4,5,6,7,8];
	list[int] output = [ openrecursion::B::fib(n) | int n <- input];
	list[int] expectedOutput = [0,1,1,2,3,5,8,13,21];
	return output == expectedOutput;
}

public test bool testAnonymousFib() {
	list[int] input = [0,1,2,3,4,5,6,7,8];
	list[int] output = [ openrecursion::B::anonymousFib(n) | int n <- input];
	list[int] expectedOutput = [0,1,1,2,3,5,8,13,21];
	return output == expectedOutput;
}

public test bool testFibWeird() {
	list[int] input = [0,1,2,3,4,5,6];
	list[int] output = [ openrecursion::C::fib(n) | int n <- input];
	list[int] expectedOutput = [0,1,2,6,16,44,120];
	return output == expectedOutput;
}
