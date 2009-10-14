module demo::Bubble

import List;
import IO;
import UnitTest;

// Three variations on Bubble sort

// sort1: uses list indexing and a for-loop

public list[int] sort1(list[int] Numbers){
  for(int I <- [0 .. size(Numbers) - 2 ]){
     if(Numbers[I] > Numbers[I+1]){
       <Numbers[I], Numbers[I+1]> = <Numbers[I+1], Numbers[I]>;
       return sort1(Numbers);
     }
  }
  return Numbers;
}

// sort2: uses list matching and switch

public list[int] sort2(list[int] Numbers){
  switch(Numbers){
    case [list[int] Nums1, int P, int Q, list[int] Nums2]:
       if(P > Q){
          return sort2(Nums1 + [Q, P] + Nums2);
       } else {
       	  fail;
       }
     default: return Numbers;
   }
  
}

// sort3: uses list matching and visit

public list[int] sort3(list[int] Numbers){
  return innermost visit(Numbers){
    case [list[int] Nums1, int P, int Q, list[int] Nums2]:
       if(P > Q){
          insert Nums1 + [Q, P] + Nums2;
       } else {
          fail;
       }
    };
}

public bool test(){
    unsorted = [10,9,8,7,6,5,4,3,2,1];
    sorted = [1,2,3,4,5,6,7,8,9,10];
    
    assertEqual(sort1(unsorted), sorted);
	assertEqual(sort2(unsorted), sorted);
	assertEqual(sort3(unsorted), sorted);
	return report("Bubble");
}

