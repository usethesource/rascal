@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{Variations on Bubble Sort}
@description{
Rascal allows for very different programming styles, ranging 
from classical procedural style with structured programming 
constructor to pure function programming and algebraic specification.

In this demo we show how the "Bubble Sort" algorithm can 
be implemented in Rascal in different ways. None is a priori
"better" than the other. Just different. Pick your poison!
}
module demo::basic::Bubble

import List;

@synopsis{sort1: uses list indexing, a for-loop and a (complex) assignment}
list[int] sort1(list[int] numbers) { 
  if (size(numbers) > 0) {
     for (int i <- [0 .. size(numbers)-1]) {
       if (numbers[i] > numbers[i+1]) {
         // interesting destructuring bind:
         <numbers[i], numbers[i+1]> = <numbers[i+1], numbers[i]>;
         return sort1(numbers);
       }
    }
  }  
  return numbers;
}

@synopsis{sort2 uses list matching, a switch and recursion instead of assignment}
list[int] sort2(list[int] numbers) {
  switch(numbers){
    case [*int nums1, int p, int q, *int nums2]:
       if (p > q) {
          return sort2(nums1 + [q, p] + nums2);
       } else {
       	  fail;
       }
     default: return numbers;
   }
}

@synopsis{sort3: uses list matching, while and an assignment}
list[int] sort3(list[int] numbers) {
  while ([*int nums1, int p, *int nums2, int q, *int nums3] := numbers && p > q)
        numbers = nums1 + [q] + nums2 + [p] + nums3;
  return numbers;
}

@synopsis{sort4: uses list matching, solve, list concatentation, and assignment}
list[int] sort4(list[int] numbers) {
  solve (numbers) {
    if ([*int nums1, int p, *int nums2, int q, *int nums3] := numbers && p > q)
      numbers = nums1 + [q] + nums2 + [p] + nums3;
  }
  return numbers;
}

@synopsis{sort5: using recursion instead of iteration, and splicing instead of concat}
list[int] sort5([*int nums1, int p, *int nums2, int q, *int nums3]) {
  if (p > q) 
    return sort5([*nums1, q, *nums2, p, *nums3]); 
  else 
    fail sort5;
}

default list[int] sort5(list[int] x) = x;

@synopsis{sort6: inlines the condition into a when, and uses overloading with a default function.}
list[int] sort6([*int nums1, int p, *int nums2, int q, *int nums3]) 
  = sort5([*nums1, q, *nums2, p, *nums3])
  when p > q; 

default list[int] sort6(list[int] x) = x;


bool isSorted(list[int] lst) = !any(int i <- index(lst), int j <- index(lst), (i < j) && (lst[i] > lst[j]));

test bool sorted1a() = isSorted([]);
test bool sorted1b() = isSorted([10]);
test bool sorted1c() = isSorted([10, 20]);
test bool sorted1d() = isSorted([-10, 20, 30]);
test bool sorted1e() = !isSorted([10, 20, -30]);

test bool sorted2(list[int] lst) = isSorted(sort2(lst));
test bool sorted3(list[int] lst) = isSorted(sort3(lst));
test bool sorted4(list[int] lst) = isSorted(sort4(lst));
test bool sorted5(list[int] lst) = isSorted(sort5(lst));
test bool sorted6(list[int] lst) = isSorted(sort6(lst));


