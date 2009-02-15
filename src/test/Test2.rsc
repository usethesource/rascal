module Test2
import IO;
import List;

public int inner1(list[int] V1, list[int] V2){
    if ((size(V1) == 0) || (size(V2) == 0)){
       return 0;
    } else {
       return (V1[0] * V2[0]) + inner1(tail(V1), tail(V2));
    }
 }
 
 public bool test(){
    	inner1([1,2], [3,4]) == 11 ;
    	return true;
 }