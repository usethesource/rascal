module openrecursion::A

import String;

public int fib(0) = 0;
public int fib(1) = 1;
public default int fib(int n) = fib(n-1) + fib(n-2);

data MyData = mydata(str s) 
			| mydata(MyData d);
			
public MyData mydata(str s) {  
	if(size(s) > 1) return mydata(substring(s,1)); 
	else return fail; 
}
