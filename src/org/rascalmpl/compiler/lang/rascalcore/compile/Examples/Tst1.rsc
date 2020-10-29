module lang::rascalcore::compile::Examples::Tst1

import Map;

//public rel[&K,&V] toRel(map[&K, set[&V]] M)  = {<k,v> | &K k <- M, &V v <- M[k]};
//public rel[&K,&V] toRel(map[&K, list[&V]] M) = {<k,v> | &K k <- M, &V v <- M[k]};
//@javaClass{org.rascalmpl.library.Prelude}
//public default java rel[&K, &V] toRel(map[&K, &V] M);

test bool toRel_l2() = toRel((1:[10], 2:[20])) == {<1,10>,<2,20>};

//import List;
//data Symbol = s();
//
//bool subtype(Symbol a, Symbol b) = true;
////public bool subtype(list[Symbol] l, list[Symbol] r) = all(i <- index(l), subtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
//public default bool subtype(list[Symbol] l, list[Symbol] r) = size(l) == 0 && size(r) == 0;
//
//
//value main() = subtype([], []);

