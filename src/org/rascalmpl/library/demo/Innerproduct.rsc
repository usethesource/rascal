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
 
test inner1([], []) == 0;
test inner1([1],[1]) == 1;
test inner1([1,2], [3,4])  == 11;
test inner1([1,2,3],[4,5,6])  == 32;
   	
test inner2([], [])  == 0;
test inner2([1],[1]) == 1;
test inner2([1,2], [3,4]) == 11;
test inner2([1,2,3],[4,5,6]) == 32;
