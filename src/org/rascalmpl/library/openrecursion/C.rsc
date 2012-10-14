module openrecursion::C

import openrecursion::A;
import IO;

public extend int fib(0) = 0;
public extend int fib(1) = 1;
public extend default int fib(int n) { 
	int res = prev(n) + (n-2)*fib(n-1) + (n-3)*fib(n-2); 
	println("fib extended (n) of <n> == <res>"); 
	return res; 
}