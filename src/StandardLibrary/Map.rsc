module Map

public set[&K] java domain(map[&K, &V] M)
@doc{domain -- return the domain (keys) of a map}
@javaClass{org.meta_environment.rascal.std.Map};

public &K java getOneFrom(map[&K, &V] M)  
@doc{getOneFrom -- return arbitrary key of a map}
@javaClass{org.meta_environment.rascal.std.Map};

public map[&K, &V] java invert(map[&K, &V] M)  
@doc{invert -- return map with key and value inverted}
@javaClass{org.meta_environment.rascal.std.Map};

public map[&K, &V] mapper(map[&K, &V] M, &K (&K) F, &V (&V) G){
  return (#F(key) : #G(M[key]) | &K key <- M);
}

public set[&V] java range(map[&K, &V] M)
@doc{range -- return the range (values) of a map}
@javaClass{org.meta_environment.rascal.std.Map};

public int java size(map[&K, &V] M)
@javaClass{org.meta_environment.rascal.std.Map};

public list[tuple[&K, &V]] java toList(map[&K, &V] M)
@doc{toList -- convert a map to a list}
@javaClass{org.meta_environment.rascal.std.Map};

public rel[&K, &V] java toRel(map[&K, &V] M)
@doc{toRel -- convert a map to a relation}
@javaClass{org.meta_environment.rascal.std.Map};
  
public str java toString(map[&K, &V] M)
@javaClass{org.meta_environment.rascal.std.Map};




