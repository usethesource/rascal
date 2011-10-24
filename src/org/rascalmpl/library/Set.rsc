@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module Set

@doc{Pick a random element from a set}
@javaClass{org.rascalmpl.library.Set}
public java &T getOneFrom(set[&T] st) throws EmptySet;

/*
public &T getOneFrom(set[&T] s) {
  for (x <- s) return x;
}
*/

@doc{Return a mapping of elements to indices}
public map[&T,int] index(set[&T] s) {
  result = ();
  i = 0;
  for (x <- s) {
    result[x] = i;
    i += 1;
  }
  return result;
}

@doc{is set empty?}
@javaClass{org.rascalmpl.library.Set}
public java bool isEmpty(set[&T] st);

@doc{Apply a function to each element of a set}
public set[&U] mapper(set[&T] st, &U (&T) fn)
{
  return {fn(elm) | &T elm <- st};
}

@doc{Largest element of a set}
public &T max(set[&T] st)
{
  &T result = getOneFrom(st);
  for(&T elm <- st){
  	if(elm > result){
    	result = elm;
    }
  }
  return result;
}

@doc{Smallest element of a set}
public &T min(set[&T] st)
{
  &T result = getOneFrom(st);
  for(&T elm <- st){
   if(elm < result){
      result = elm;
   }
  }
  return result;
}

@doc{Return all subsets of a set}
public set[set[&T]] power(set[&T] st)
{

  set[set[&T]] result = {{st}};
  for(&T elm <- st){
  	set[set[&T]] pw = power(st - elm);
  	result = result + pw;
  	for(set[&T] sub <- pw){
  		result = result + {{sub + elm}};
  	}
  }
  return result;
}

@doc{Return all subsets (excluding empty set) of a set}
public set[set[&T]] power1(set[&T] st)
{
	return power(st) - {{}};
}

@doc{Apply function F to successive elements of a set}
public &T reducer(set[&T] st, &T (&T,&T) fn, &T unit)
{
  &T result = unit;
  for(&T elm <- st){
    result = fn(result, elm);
  }
  return result;
}

@doc{Number of elements in a set}
@javaClass{org.rascalmpl.library.Set}
public java int size(set[&T] st);

@doc{Remove an arbitrary element from a set, returns the element and the modified set}
@javaClass{org.rascalmpl.library.Set}
public java tuple[&T, set[&T]] takeOneFrom(set[&T] st) throws EmptySet;
  
@doc{Convert a set to a list}
@javaClass{org.rascalmpl.library.Set}
public java list[&T] toList(set[&T] st);

@doc{Convert a set of tuples to a map; values should be unique}
@javaClass{org.rascalmpl.library.Set}
public java map[&A,set[&B]] toMap(rel[&A, &B] st);

@doc{Convert a set of tuples to a map; values in relation are associated with a set of keys}
@javaClass{org.rascalmpl.library.Set}
public java map[&A,&B] toMapUnique(rel[&A, &B] st);

@doc{Convert a set to a string}
@javaClass{org.rascalmpl.library.Set}
public java str toString(set[&T] st);

@doc{Group elements in a set given an equivalence function}
public set[set[&T]] group(set[&T] input, bool (&T a, &T b) similar) {
  set[set[&T]] result = {};
  while (input != {}) {
    <e, input> = takeOneFrom(input);
    set[&T] same = { f | f <- input, similar(e, f) };
    input -= same;
    result = result + {{same, e}};
  }
  return result;
}

@doc{indexes elements in a set into a map}
public map[&K,set[&V]] index(set[&V] input, &K (&V) getKey) {
  map[&K,set[&V]] result = ();
  set[&V] empty = {};
  for (elem <- input) {
    &K key = getKey(elem);
    result[key]?empty += {elem};
  }
  return result;
}
