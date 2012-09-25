module openrecursion::C

import openrecursion::A;
import IO;

public extend int fib(0) = 0;
public extend int fib(1) = 1;
public default extend int fib(int n) { 
	int res = prev(n) + fib(n-1) + fib(n-2); 
	println("fib(n) - fib(n-3) of <n> == <res>"); 
	return res; }