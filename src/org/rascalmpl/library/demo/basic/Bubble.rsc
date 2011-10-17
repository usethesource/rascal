@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
//START
module demo::basic::Bubble

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

// sort3: uses list matching and while

public list[int] sort3(list[int] Numbers){
  while([list[int] Nums1, int P, list[int] Nums2, int Q, list[int] Nums3] := Numbers && P > Q)
        Numbers = Nums1 + [Q] + Nums2 + [P] + Nums3;
  return Numbers;
}

// sort4: similar to sort3, but shorter.

public list[int] sort4(list[int] Numbers){
  while([Nums1*, P, Nums2*, Q, Nums3*] := Numbers && P > Q)
        Numbers = Nums1 + [Q] + Nums2 + [P] + Nums3;
  return Numbers;

}

