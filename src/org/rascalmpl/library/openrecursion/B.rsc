module openrecursion::B

import openrecursion::A;
import Prelude;

public int fib(int n) { int res = prev(n); println("fib of <n> == <res>"); return res; }

public MyData mydata(str s) { println("<s>"); MyData res = prev(s); return res; }

public int anonymousFib(int n) {
	return int (int n) { switch(n) { 
					case 0: return 0; 
					case 1: return 1; 
					default: return it(n-1) + it(n-2);} 
				}(n);
}
