@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::Bubble

import List;
import IO;

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

// Tests

public list[int] unsorted = [10,9,8,7,6,5,4,3,2,1];
public list[int] sorted = [1,2,3,4,5,6,7,8,9,10];   
public test bool t1() = sort1(unsorted) == sorted;
public test bool t2() = sort2(unsorted) == sorted;
public test bool t3() = sort3(unsorted) == sorted;

