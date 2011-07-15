@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Map

@doc{ return the domain (keys) of a map}
@javaClass{org.rascalmpl.library.Map}
public java set[&K] domain(map[&K, &V] M);

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
@javaClass{org.rascalmpl.library.Map}
public java &K getOneFrom(map[&K, &V] M)  ;

@doc{ return map with key and value inverted; values are not unique and are collected in a set}
@javaClass{org.rascalmpl.library.Map}
public java map[&V, set[&K]] invert(map[&K, &V] M)  ;

@doc{ return map with key and value inverted; values are unique}
@javaClass{org.rascalmpl.library.Map}
public java map[&V, &K] invertUnique(map[&K, &V] M)  ;

@doc{Is map empty?}
@javaClass{org.rascalmpl.library.Map}
public java bool isEmpty(map[&K, &V] M);

@doc{Apply two functions to each key/value pair in a map.}
public map[&K, &V] mapper(map[&K, &V] M, &L (&K) F, &W (&V) G)
{
  return (F(key) : G(M[key]) | &K key <- M);
}

@doc{Return the range (values) of a map}
@javaClass{org.rascalmpl.library.Map}
public java set[&V] range(map[&K, &V] M);

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
@javaClass{org.rascalmpl.library.Map}
public java int size(map[&K, &V] M);

@doc{Convert a map to a list}
@javaClass{org.rascalmpl.library.Map}
public java list[tuple[&K, &V]] toList(map[&K, &V] M);

@doc{Convert a map to a relation}
@javaClass{org.rascalmpl.library.Map}
public java rel[&K, &V] toRel(map[&K, &V] M);
  
@doc{Convert a map to a string.}
@javaClass{org.rascalmpl.library.Map}
public java str toString(map[&K, &V] M);

@doc{Compute a distribution: count how many times events are mapped to which bucket}
public map[&T, int] distribution(map[&U event, &T bucket] input) {
  map[&T,int] result = ();
  for (&U event <- input) {
    result[input[event]]?0 += 1;
  }
  
  return result;
}

