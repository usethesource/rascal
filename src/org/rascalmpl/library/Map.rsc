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




@doc{
Synopsis: Determine the domain (set of keys) of a map.

Description:
Returns the domain (set of keys) of map `M`.

Examples:
<screen>
import Map;
domain(("apple": 1, "pear": 2));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java set[&K] domain(map[&K, &V] M);

@doc{
Synopsis: Map restricted to certain keys.

Description:
Return the map `M` restricted to pairs with key in `S`.

Examples:
<screen>
import Map;
domainR(("apple": 1, "pear": 2, "orange": 3), {"apple", "pear"});
</screen>
}
public map[&K, &V] domainR(map[&K, &V] M, set[&K] S) {
	map[&K, &V] result = ();
	for(&K key <- M) {
		if(key in S) {
			result += (key: M[key]);
		}
	}

	return result;
}

@doc{
Synopsis: Map with certain keys excluded.

Description:
Return the map `M` restricted to pairs with key not in `S`.

Examples:
<screen>
import Map;
domainX(("apple": 1, "pear": 2, "orange": 3), {"apple", "pear"});
</screen>
}
public map[&K, &V] domainX(map[&K, &V] M, set[&K] S) {
	map[&K, &V] result = ();
	for(&K key <- M) {
		if(key notin S) {
			result += (key: M[key]);
		}
	}

	return result;
}

@doc{
Synopsis: Get a n arbitrary key from a map.

Description:
Returns an arbitrary key of map `M`.

Examples:
<screen>
import Map;
getOneFrom(("apple": 1, "pear": 2, "pineapple": 3));
getOneFrom(("apple": 1, "pear": 2, "pineapple": 3));
getOneFrom(("apple": 1, "pear": 2, "pineapple": 3));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java &K getOneFrom(map[&K, &V] M)  ;

@doc{
Synopsis: Invert the (key,value) pairs in a map.

Description:
Returns inverted map in which each value in the old map `M` is associated with a set of key values from the old map.
Also see [invertUnique].

Examples:
<screen>
import Map;
invert(("apple": 1, "pear": 2, "orange": 1));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&V, set[&K]] invert(map[&K, &V] M)  ;

@doc{
Synopsis: Invert the (key,value) pairs in a map.

Description:
Returns a map with key and value inverted; the result should be a map.
If the initial map contains duplicate values,
the `MultipleKey` exception is raised since
an attempt is made to create a map where more than one 
value would be associated with the same key.

Also see [Map/invert] and [$Prelude/Exception].

Examples:
<screen errors>
import Map;
invertUnique(("apple": 1, "pear": 2, "orange": 3));
//Here is an examples that generates an exception:
invertUnique(("apple": 1, "pear": 2, "orange": 1));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&V, &K] invertUnique(map[&K, &V] M)  ;

@doc{
Synopsis: Test whether a map is empty.

Description:
Returns `true` if map `M` is empty, and `false` otherwise.

Examples:
<screen>
import Map;
isEmpty(());
isEmpty(("apple": 1, "pear": 2, "orange": 3));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isEmpty(map[&K, &V] M);

@doc{
Synopsis: Apply a function to all (key, value) pairs in a map.

Description:
Apply the functions `F` and `G` to each key/value pair in a map and return the transformed map.

Examples:
<screen>
import Map;
str prefix(str s) { return "X" + s; }
int incr(int x) { return x + 1; }
mapper(("apple": 1, "pear": 2, "orange": 3), prefix, incr);
</screen>
}
public map[&K, &V] mapper(map[&K, &V] M, &L (&K) F, &W (&V) G)
{
  return (F(key) : G(M[key]) | &K key <- M);
}

@doc{
Synopsis: The range (set of values that correspond to its keys) of a map.

Description:
Returns the range (set of values) of map `M`.

Examples:
<screen>
import Map;
range(("apple": 1, "pear": 2));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java set[&V] range(map[&K, &V] M);

@doc{
Synopsis: Map restricted to certain values in (key,values) pairs.

Description:
Returns the map restricted to pairs with values in `S`.

Examples:
<screen>
import Map;
rangeR(("apple": 1, "pear": 2, "orange": 3), {2, 3});
</screen>
}
public map[&K, &V] rangeR(map[&K, &V] M, set[&V] S) {
	map[&K, &V] result = ();
	for(&K key <- M) {
		if(M[key] in S) {
			result += (key: M[key]);
		}
	}

	return result;
}

@doc{
Synopsis: Map with certain values in (key,value) pairs excluded.

Description:
Returns the map restricted to pairs with values not in `S`.

Examples:
<screen>
import Map;
rangeX(("apple": 1, "pear": 2, "orange": 3), {2, 3});
</screen>
}
public map[&K, &V] rangeX(map[&K, &V] M, set[&V] S) {
	map[&K, &V] result = ();
	for(&K key <- M) {
		if(M[key] notin S) {
			result += (key: M[key]);
		}
	}

	return result;
}

@doc{
Synopsis: Number of (key, value) pairs in a map.

Description:
Returns the number of pairs in map `M`.

Examples:
<screen>
import Map;
size(("apple": 1, "pear": 2, "orange": 3));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java int size(map[&K, &V] M);


@doc{
Synopsis: Convert a map to a list of tuples.

Examples:
<screen>
import Map;
toList(("apple": 1, "pear": 2, "orange": 3));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[tuple[&K, &V]] toList(map[&K, &V] M);

@doc{
Synopsis: Convert a map to a relation.

Examples:
<screen>
import Map;
toRel(("apple": 1, "pear": 2, "orange": 3));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java rel[&K, &V] toRel(map[&K, &V] M);

public rel[&K,&V] toRel(map[&K,set[&V]] M) = {<k,v> | &K k <- M, &V v <- M[k]};
public rel[&K,&V] toRel(map[&K,list[&V]] M) = {<k,v> | &K k <- M, &V v <- M[k]};
  
@doc{
Synopsis: Convert a map to a string.

Examples:
<screen>
import Map;
toString(("apple": 1, "pear": 2, "orange": 3));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toString(map[&K, &V] M);


