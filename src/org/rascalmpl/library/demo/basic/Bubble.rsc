@license{
  Copyright (c) 2009-2015 CWI
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

// Variations on Bubble sort

// sort1: uses list indexing and a for-loop

public list[int] sort1(list[int] numbers){
  if(size(numbers) > 0){
     for(int i <- [0 .. size(numbers)-1]){
       if(numbers[i] > numbers[i+1]){
         <numbers[i], numbers[i+1]> = <numbers[i+1], numbers[i]>;
         return sort1(numbers);
       }
    }
  }  
  return numbers;
}

bool isSorted(list[int] lst) = !any(int i <- index(lst), int j <- index(lst), (i < j) && (lst[i] > lst[j]));

test bool sorted1a() = isSorted([]);
test bool sorted1b() = isSorted([10]);
test bool sorted1c() = isSorted([10, 20]);
test bool sorted1d() = isSorted([-10, 20, 30]);
test bool sorted1e() = !isSorted([10, 20, -30]);

// sort2: uses list matching and switch

public list[int] sort2(list[int] numbers){
  switch(numbers){
    case [*int nums1, int p, int q, *int nums2]:
       if(p > q){
          return sort2(nums1 + [q, p] + nums2);
       } else {
       	  fail;
       }
     default: return numbers;
   }
}

test bool sorted2(list[int] lst) = isSorted(sort2(lst));

// sort3: uses list matching and while

public list[int] sort3(list[int] numbers){
  while([*int nums1, int p, *int nums2, int q, *int nums3] := numbers && p > q)
        numbers = nums1 + [q] + nums2 + [p] + nums3;
  return numbers;
}

test bool sorted3(list[int] lst) = isSorted(sort3(lst));

// sort4: using recursion instead of iteration, and splicing instead of concat
public list[int] sort4([*int nums1, int p, *int nums2, int q, *int nums3]) {
  if (p > q) 
    return sort4([*nums1, q, *nums2, p, *nums3]); 
  else 
    fail sort4;
}

public default list[int] sort4(list[int] x) = x;

test bool sorted4(list[int] lst) = isSorted(sort4(lst));

// finally, sort 6 inlines the condition into a when:
public list[int] sort5([*int nums1, int p, *int nums2, int q, *int nums3]) 
  = sort5([*nums1, q, *nums2, p, *nums3])
  when p > q; 

public default list[int] sort5(list[int] x) = x;

test bool sorted5(list[int] lst) = isSorted(sort5(lst));


