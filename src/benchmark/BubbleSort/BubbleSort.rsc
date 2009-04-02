module benchmark::BubbleSort::BubbleSort

import Benchmark;
import IO;

/*
rule L1 [list[int] Nums1, int P, int Q, list[int] Nums2]:
       if(P > Q){
          insert Nums1 + [Q, P] + Nums2;
       } else {
          fail;
       };
*/       
       
       
rule Bubble [list[int] Nums1, int P, int Q, list[int] Nums2] => [Nums1, Q, P, Nums2]
       		when P > Q;
       
       
 public bool measure(){
		start = currentTimeMillis();
		result = [10,9,8,7,6,5,4,3,2,1];
		used = currentTimeMillis() - start;
		println("bubble = <result>  (<used> millis)");
		return true;
 }
       
