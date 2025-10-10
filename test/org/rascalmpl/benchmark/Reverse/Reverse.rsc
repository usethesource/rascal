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
module Reverse::Reverse

import util::Math;
import util::Benchmark;
import IO;

public list[int] rev (list[int] L)
{
    if([int X, *int L1] := L && L1 != [])
		return rev(L1) + X;
    else
        return L;
}

int SIZE = 100;
int ITER = 10000;

public void measure(){
    L = for(int i <- [ 0 .. SIZE ]) append arbInt();
    
    begin = realTime();
	for(int i <- [1 .. ITER])
	    rev(L);
	    
	used = (realTime() - begin);
		
	println("<ITER> x rev list <SIZE> elements <used> (msec)");
}

void main() {
    measure();
}
	
	
	
