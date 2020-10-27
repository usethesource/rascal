module lang::rascalcore::compile::Examples::Tst1

public rel[&K,&V] toRel(map[&K, set[&V]] M)  = {<k,v> | &K k <- M, &V v <- M[k]};
public rel[&K,&V] toRel(map[&K, list[&V]] M) = {<k,v> | &K k <- M, &V v <- M[k]};
@javaClass{org.rascalmpl.library.Prelude}
public default java rel[&K, &V] toRel(map[&K, &V] M);