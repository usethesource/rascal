module Innerproduct

import List;

public int inner1(list[int] V1, list[int] V2){
    if ((size(V1) == 0) || (size(V2) == 0)){
       return 0;
    } else {
       return (V1[0] * V2[0]) + inner1(tail(V1), tail(V2));
    }
 }
 
 public int inner2(list[int] V1, list[int] V2){
    if(([int N1, list[int] R1] := V1) && ([int N2, list[int] R2] := V2)){
    	return N1 * N2 + inner2(R1, R2);
    } else {
       return 0;
    }
 }
 
 bool test(){
    return
    	inner1([], []) == 0 &&
    	inner1([1],[1]) == 1 &&
    	inner1([1,2], [3,4]) == 11 &&
    	inner1([1,2,3],[4,5,6]) == 32 &&
    	
    	inner2([], []) == 0 &&
    	inner2([1],[1]) == 1 &&
    	inner2([1,2], [3,4]) == 11 &&
    	inner2([1,2,3],[4,5,6]) == 32;
 }