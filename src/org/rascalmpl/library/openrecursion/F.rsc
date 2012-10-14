module openrecursion::F

import IO;
import List;
import Set;

public int fib(0) = 0;
public int fib(1) = 1;
public default int fib(int n) = (n%2 == 0) ? fib(n-1) + fib(n-2) : { fail; };

public int fact(0) = 1;
public int fact(1) = 1;
public default int fact(int n) = (n%2 == 0) ? { fail; } : n*fact(n-1);

public int fibonacciOrFactorial(int n) = (fib ++ fact)(n);
// the semantics is equivalent to
public int fibOrFact(0) = getOneFrom({0,1});
public int fibOrFact(1) = 1;
public default int fibOrFact(int n) = (n%2 == 0) ? fibOrFact(n-1) + fibOrFact(n-2) : n*fibOrFact(n-1);

public test bool test3() {
	list[int] inputs = [0,1,2,3,4,5,6,7];
	list[set[int]] outputs = [{0,1}, {1}, {1,2}, {3,6}, {4,7,5,8}, {20,35,25,40}, {24,39,29,44,27,42,32,47,25,40,30,45,28,43,33,48}, { i*7 | int i <- {24,39,29,44,27,42,32,47,25,40,30,45,28,43,33,48} } ];
	list[int] outputs1 = [ fibOrFact(input) | int input <- inputs ];
	list[int] outputs2 = [ fibonacciOrFactorial(input) | int input <- inputs ];
	list[int] outputs3 = [ (fact ++ fib)(input) | int input <- inputs ];
	bool result = true;
	for(int i <- [ 0 .. size(inputs) - 1 ]){
		if(!( (outputs1[i] in outputs[i]) && (outputs2[i] in outputs[i]) && (outputs3[i] in outputs[i]) )) 
			result = false;
	}
	return result;
}