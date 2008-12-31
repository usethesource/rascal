module Bubble

import List;

// sort1: uses list indexing and for-loop

public list[int] sort1(list[int] Numbers){
  for(int I : [0, ... , size(Numbers) - 2 ]){
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
  list[int] Nums1, Nums2;
  int P, Q;

  return innermost visit(Numbers){
    case [Nums1, P, Q, Nums2]:
       if(P > Q){
          insert [Nums1, Q, P, Nums2];
       } else {
          fail;
       }
     default: Numbers;
    };
}

