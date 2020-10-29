module lang::rascalcore::compile::Examples::Tst1

import List;
data Symbol = s();

bool subtype(Symbol a, Symbol b) = true;
//public bool subtype(list[Symbol] l, list[Symbol] r) = all(i <- index(l), subtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
public default bool subtype(list[Symbol] l, list[Symbol] r) = size(l) == 0 && size(r) == 0;


value main() = subtype([], []);