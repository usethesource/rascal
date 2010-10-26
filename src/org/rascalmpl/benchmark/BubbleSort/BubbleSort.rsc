module BubbleSort::BubbleSort

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
       
data Bubble = bubble(list[int] elements);

rule Bubble bubble([list[int] Nums1, int P, int Q, list[int] Nums2]) => bubble([Nums1, Q, P, Nums2])
       		when P > Q;
       
 public bool measure(){
		start = realTime();
		result = bubble([10,9,8,7,6,5,4,3,2,1]);
		used = realTime() - start;
		println("bubble = <result>  (<used> millis)");
		return true;
 }
       
