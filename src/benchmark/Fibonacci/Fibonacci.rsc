module benchmark::Fibonacci::Fibonacci

import Benchmark;
import IO;

public int fib(int n)
{
   if(n == 0)
   		return 0;
   if(n == 1)
    	return 1;
   return fib(n - 1) + fib(n - 2);
}

public bool testFibonacci()
{
	return fib(20) == 6765;
}

public bool measure()
{
	start = currentTimeMillis();
	n = 15;
	result = fib(n);
	used = currentTimeMillis() - start;
		
	println("fib(<n>) = <result>  (<used> millis)");
	return true;

}