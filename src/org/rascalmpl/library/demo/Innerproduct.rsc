@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::Innerproduct

import List;

// Compute innerproduct of two vectors that are represented as a list of integers

// inner1: standard recursive version that accesses the head element
// using an index and then fetches the tail

public int inner1(list[int] V1, list[int] V2){
    if ((size(V1) == 0) || (size(V2) == 0)){
       return 0;
    } else {
       return (V1[0] * V2[0]) + inner1(tail(V1), tail(V2));
    }
}
 
// inner2: decomposes each vector in head and tail using list matching
 
public int inner2(list[int] V1, list[int] V2){
    if(([int N1, list[int] R1] := V1) && ([int N2, list[int] R2] := V2)){
    	return N1 * N2 + inner2(R1, R2);
    } else {
       return 0;
    }
}

// Tests
 
public test bool t1() = inner1([], []) == 0; 
public test bool t2() = inner1([1],[1]) == 1;
public test bool t3() = inner1([1,2], [3,4])  == 11;
public test bool t4() = inner1([1,2,3],[4,5,6])  == 32;
   	
public test bool t5() = inner2([], [])  == 0;
public test bool t6() = inner2([1],[1]) == 1;
public test bool t7() = inner2([1,2], [3,4]) == 11;
public test bool t8() = inner2([1,2,3],[4,5,6]) == 32;
