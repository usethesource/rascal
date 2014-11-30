module experiments::Compiler::Examples::Tst

import List;
import IO;

// sort4: using recursion instead of iteration, and splicing instead of concat
public list[int] sort4([*int nums1, int p, *int nums2, int q, *int nums3]) {
  println("[<nums1>, <p>, <nums2>, <q>, <nums3>");
  if (p > q) {
    println(" <p> \> <q>");
    return sort4([*nums1, q, *nums2, p, *nums3]); 
  } else {
    println("fail");
    fail sort4;
  }
}

public default list[int] sort4(list[int] x) = x;

//test bool sorted4(list[int] lst) = isSorted(sort4(lst));

value main(list[value] args) = 
    sort4([2052006116,-1309172962,-1260791556,-1621905146]);


