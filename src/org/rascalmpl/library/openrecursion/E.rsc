module openrecursion::E

import openrecursion::D;

import IO;

public int printedFib(int n) = (printIt o fib)(n);
public int printedFact(int n) = (printIt o fact)(n);

public int printedFibOpen(int n) = (printIt oo fib)(n);
public int printedFactOpen(int n) = (printIt oo fact)(n);

public int printedAnonymousFib(int n) = (printIt oo 
											int (int i) { switch(i) {
															case 0: return 0; 
															case 1: return 1;
															case int i: return it(i - 1) + it(i - 2); 
														}  } 
										 )(n);

public int f1FibonacciOpen(int n) = (f1 oo fib)(n);
public int f1AnonymousFibonacciOpen(int n) = (f1 oo 
										int (int i) { switch(i) {
															case 0: return 0; 
															case 1: return 1;
															case int i: return it(i - 1) + it(i - 2); 
														}  }
									 )(n);
// the resulting semantics
public int f1comp(0) = f1(0);
public int f1comp(1) = f1(1);
public default int f1comp(int n) = f1( f1comp(n - 1) + f1comp(n - 2) );

public test bool test1() {
	list[int] inputs = [0,1,2,3,4,5,6];
	list[int] outputs1 = [ f1comp(input) | int input <- inputs ];
	list[int] outputs2 = [ f1FibonacciOpen(input) | int input <- inputs ];
	list[int] outputs3 = [ f1AnonymousFibonacciOpen(input) | int input <- inputs ];
	return outputs1 == outputs2 && outputs1 == outputs3;
}

public int fibonacciF1Open(int n) = (fib oo f1)(n);
public int anonymousFibonacciF1Open(int n) = (int (int i) { switch(i) {
															case 0: return 0; 
															case 1: return 1;
															case int i: return it(i - 1) + it(i - 2); 
														}  } 
										oo f1)(n);
// the resulting semantics
public int fibonaccicomp(0) = fib(1);
public int fibonaccicomp(1) = fib(1);
public default int fibonaccicomp(int n) = fib( n + fibonaccicomp(n - 1) );

public test bool test2() {
	list[int] inputs = [0,1,2,3,4];
	list[int] outputs1 = [ fibonaccicomp(input) | int input <- inputs ];
	list[int] outputs2 = [ fibonacciF1Open(input) | int input <- inputs ];
	list[int] outputs3 = [ anonymousFibonacciF1Open(input) | int input <- inputs ];
	return outputs1 == outputs2 && outputs1 == outputs2;
}

public int printedF1Fib(int n) = (printIt o f1 o fib)(n);

public int printedF1FibOpen(int n) = (printIt oo f1 oo fib)(n);


