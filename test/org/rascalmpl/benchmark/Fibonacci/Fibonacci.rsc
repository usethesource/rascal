@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Fibonacci::Fibonacci

import util::Benchmark;
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
	begin = realTime();
	n = 15;
	result = fib(n);
	used = realTime() - begin;
		
	println("fib(<n>) = <result>  (<used> millis)");
	return true;

}
