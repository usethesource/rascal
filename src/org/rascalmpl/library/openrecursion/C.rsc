module openrecursion::C

import openrecursion::A;

import IO;


public int fFibOpen(int n) = (f o fib)(n);
public int fAnonymFibOpen(int n) = (f o int (int i) { switch(i) {
														case 0: return 0; 
														case 1: return 1;
														case int i: return it(i - 1) + it(i - 2); 
													} }
									 )(n);
public int fAndFibComposed(0) = f(0);
public int fAndFibComposed(1) = f(1);
public default int fAndFibComposed(int n) = f( fAndFibComposed(n-1) + fAndFibComposed(n-2) );

public test bool test1() {
	list[int] inputs = [0,1,2,3,4,5,6];
	list[int] outputs1 = [ fAndFibComposed(input) | int input <- inputs ];
	list[int] outputs2 = [ fFibOpen(input) | int input <- inputs ];
	list[int] outputs3 = [ fAnonymFibOpen(input) | int input <- inputs ];
	return outputs1 == outputs2 && outputs1 == outputs3;
}

public int fibFOpen(int n) = (fib o f)(n);
public int anonymFibFOpen(int n) = (int (int i) { switch(i) {
													case 0: return 0; 
													case 1: return 1;
													case int i: return it(i-1) + it(i-2); 
												}  } 
										o f)(n);
public int fibonaccicomp(0) = fib(1);
public int fibonaccicomp(1) = fib(1);
public default int fibonaccicomp(int n) = fib( n + fibonaccicomp(n-1) );

public test bool test2() {
	list[int] inputs = [0,1,2,3,4];
	list[int] outputs1 = [ fibonaccicomp(input) | int input <- inputs ];
	list[int] outputs2 = [ fibFOpen(input) | int input <- inputs ];
	list[int] outputs3 = [ anonymFibFOpen(input) | int input <- inputs ];
	return outputs1 == outputs2 && outputs1 == outputs3;
}

public int printIt(int n) { println(n); return n; }

public int printedFFib(int n) = (printIt o. f o. fib)(n);
public int printedFFibOpen(int n) = (printIt o f o fib)(n);
