module Map

/*
 * Functions on maps:
 * - domain
 * - domainR
 * - domainX
 * - getOneFrom
 * - invert
 * - invertUnique
 * - isEmpty
 * - range
 * - rangeR
 * - rangeX
 * - size 
 * - toList
 * - toRel
 * - toString
 */
 
@doc{ return the domain (keys) of a map}
@javaClass{org.meta_environment.rascal.library.Map}
public set[&K] java domain(map[&K, &V] M);

@doc{Restrict the map to elements with keys in S}
public map[&K, &V] domainR(map[&K, &V] M, set[&K] S) {
	map[&K, &V] result = ();
	for(&K key <- M) {
		if(key in S) {
			result += (key: M[key]);
		}
	}

	return result;
}

@doc{Restrict the map to elements with keys not in S}
public map[&K, &V] domainX(map[&K, &V] M, set[&K] S) {
	map[&K, &V] result = ();
	for(&K key <- M) {
		if(key notin S) {
			result += (key: M[key]);
		}
	}

	return result;
}

@doc{ return arbitrary key of a map}
@javaClass{org.meta_environment.rascal.library.Map}
public &K java getOneFrom(map[&K, &V] M)  ;

@doc{ return map with key and value inverted; values are not unique and are collected in a set}
@javaClass{org.meta_environment.rascal.library.Map}
public map[&V, set[&K]] java invert(map[&K, &V] M)  ;

@doc{ return map with key and value inverted; values are unique}
@javaClass{org.meta_environment.rascal.library.Map}
public map[&V, &K] java invertUnique(map[&K, &V] M)  ;

@doc{Is map empty?}
@javaClass{org.meta_environment.rascal.library.Map}
public bool java isEmpty(map[&K, &V] M);

@doc{Apply two functions to each key/value pair in a map.}
public map[&K, &V] mapper(map[&K, &V] M, &L (&K) F, &W (&V) G)
{
  return (F(key) : G(M[key]) | &K key <- M);
}

@doc{Return the range (values) of a map}
@javaClass{org.meta_environment.rascal.library.Map}
public set[&V] java range(map[&K, &V] M);

@doc{Restrict the map to elements with value in S} 
public map[&K, &V] rangeR(map[&K, &V] M, set[&V] S) {
	map[&K, &V] result = ();
	for(&K key <- M) {
		if(M[key] in S) {
			result += (key: M[key]);
		}
	}

	return result;
}

@doc{Restrict the map to elements with value not in S}
public map[&K, &V] rangeX(map[&K, &V] M, set[&V] S) {
	map[&K, &V] result = ();
	for(&K key <- M) {
		if(M[key] notin S) {
			result += (key: M[key]);
		}
	}

	return result;
}

@doc{Number of elements in a map.}
@javaClass{org.meta_environment.rascal.library.Map}
public int java size(map[&K, &V] M);

@doc{Convert a map to a list}
@javaClass{org.meta_environment.rascal.library.Map}
public list[tuple[&K, &V]] java toList(map[&K, &V] M);

@doc{Convert a map to a relation}
@javaClass{org.meta_environment.rascal.library.Map}
public rel[&K, &V] java toRel(map[&K, &V] M);
  
@doc{Convert a list to a string.}
@javaClass{org.meta_environment.rascal.library.Map}
public str java toString(map[&K, &V] M);




