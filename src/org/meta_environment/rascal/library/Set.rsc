module Set

/*
 * Library functions for sets:
 * - getOneFrom
 * - isEmpty
 * - mapper
 * - max
 * - min
 * - power
 * - power1
 * - reducer
 * - size
 * - takeOneFrom
 * - toList
 * - toMap
 * - toMapUnique
 * - toString
 */
 
@doc{Pick a random element from a set}
@javaClass{org.meta_environment.rascal.library.Set}
public &T java getOneFrom(set[&T] st);

@doc{is set empty?}
@javaClass{org.meta_environment.rascal.library.Set}
public bool java isEmpty(set[&T] st);

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
@javaClass{org.meta_environment.rascal.library.Set}
public int java size(set[&T] st);

@doc{Remove an arbitrary element from a set, returns the element and the modified set}
@javaClass{org.meta_environment.rascal.library.Set}
public tuple[&T, set[&T]] java takeOneFrom(set[&T] st);
  
@doc{Convert a set to a list}
@javaClass{org.meta_environment.rascal.library.Set}
public list[&T] java toList(set[&T] st);

@doc{Convert a set of tuples to a map; values should be unique}
@javaClass{org.meta_environment.rascal.library.Set}
public map[&A,set[&B]] java toMap(rel[&A, &B] st);

@doc{Convert a set of tuples to a map; values in relation are associated with a set of keys}
@javaClass{org.meta_environment.rascal.library.Set}
public map[&A,&B] java toMapUnique(rel[&A, &B] st);

@doc{Convert a set to a string}
@javaClass{org.meta_environment.rascal.library.Set}
public str java toString(set[&T] st);
