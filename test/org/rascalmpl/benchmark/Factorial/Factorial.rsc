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
module Factorial::Factorial

import IO;
import util::Benchmark;

/*
 * fac -- function definition for factorial
 */

public int fac(int N)
{
	if(N <= 0)
		return 1;
	else
		return N * fac(N - 1);
}


data DA = A();
data DB = B();
data DC = C();
data DD = D();
data DE = E();
data DF = F();
data DG = G();
data DH = H();
data DI = I();
data DJ = J();
          

public int fac(bool N) = 0;
public int fac(real N) = 0;
public int fac(str N) = 0;
public int fac(loc N) = 0;
public int fac(datetime N) = 0;
public int fac(node N) = 0;
public int fac(list[int] N) = 0;
public int fac(set[int] N) = 0;
public int fac(map[int,int] N) = 0;
public int fac(rel[int,int] N) = 0;


public int fac(DA N) = 0;
public int fac(DB N) = 0;
public int fac(DC N) = 0;
public int fac(DD N) = 0;
public int fac(DE N) = 0;
public int fac(DF N) = 0;
public int fac(DG N) = 0;
public int fac(DH N) = 0;
public int fac(DI N) = 0;
public int fac(DJ N) = 0;







/*
public int fac(int N) {
     int res = 1;
     while( N > 0){
       res *= N;
       N -= 1;
     }
	return res;
}
*/

public void measure()
{
    int n = 20;
	begin = realTimeNow();
	for(int i <- [1 .. 10000])
	    fac(n);
	used = (realTimeNow() - begin)/1000;
		
	println("10000 x fac(<n>) <used> seconds)");
}

