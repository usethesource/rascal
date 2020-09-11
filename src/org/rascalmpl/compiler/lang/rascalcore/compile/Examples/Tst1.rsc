module lang::rascalcore::compile::Examples::Tst1


import List;

public bool isSorted(list[int] L) = !any(int i <- index(L), int j <- index(L), i < j && elementAt(L,i) > elementAt(L,j));

value main() = isSorted([1, 2, 3]);

