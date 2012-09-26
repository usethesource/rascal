module openrecursion::C

import openrecursion::A;
import IO;

public int fib(int n) { 
	int res = prev(n) + (n-2)*fib(n-1) + (n-3)*fib(n-2); 
	println("fib(n) = (n-1)*fib(n-1) + (n-2)*fib(n-2) of <n> == <res>"); 
	return res; }