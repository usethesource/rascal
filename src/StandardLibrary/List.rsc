module List

public &T average(list[&T] lst, &T zero)
@doc{average -- average of elements of a list}
{
  return size(lst) > 0 ? sum(lst, zero)/size(lst) : zero;
}

public list[&T] java delete(list[&T] lst, int n)
@doc{delete -- delete nth element from list}
@javaClass{org.meta_environment.rascal.std.List};

public set[int] java domain(list[&T] lst)
@doc{domain -- a list of all legal index values for a list}
@javaClass{org.meta_environment.rascal.std.List};

public &T java head(list[&T] lst) throws EmptyListError
@doc{head -- get the first element of a list}
@javaClass{org.meta_environment.rascal.std.List};

public list[&T] java head(list[&T] lst, int n) throws IndexOutOfBoundsError
@doc{head -- get the first n elements of a list}
@javaClass{org.meta_environment.rascal.std.List};

public &T java getOneFrom(list[&T] lst)
@doc{getOneFrom -- get an arbitrary element from a list}
@javaClass{org.meta_environment.rascal.std.List};

public list[&T] java insertAt(list[&T] lst, int n, &T elm) throws IndexOutOfBoundsError
@doc{insertAt -- add an element at a specific position in a list}
@javaClass{org.meta_environment.rascal.std.List};
 
public bool java isEmpty(list[&T] lst)
@doc{isEmpty -- is list empty?}
@javaClass{org.meta_environment.rascal.std.List};

public list[&T] mapper(list[&T] lst, &T (&T) fn)
@doc{mapper -- apply a function to each element of a list}
{
  return [fn(elm) | &T elm <- lst];
}

public &T max(list[&T] lst)
@doc{max -- largest element of a list}
{
  &T result = getOneFrom(lst);
  for(&T elm <- lst) {
   if(result < elm) {
      result = elm;
   }
  }
  return result;
}

public &T min(list[&T] lst)
@doc{min -- smallest element of a list}
{
  &T result = getOneFrom(lst);
  for(&T elm <- lst){
   if(elm < result){
      result = elm;
   }
  }
  return result;
}

public set[list[&T]] permutations(list[&T] lst)
@doc{permutations -- return all permutations of a list}
{
  int N = size(lst);
  if(N <= 1)
  	return {[lst]};
  	
  set[list[&T]] result = {};
  
  for(int i <- domain(lst)){
   
  	set[list[&T]] perm = permutations(head(lst, i) + tail(lst, N - i -1));
  	
  	for(list[&T] sub <- perm){
  		result = result + {[lst[i], sub]};
  	}
  }
  return result;
}

public &T reducer(list[&T] lst, &T (&T, &T) fn, &T unit)
@doc{reducer -- apply function F to successive elements of a list}
{
  &T result = unit;
  for(&T elm <- lst){
     result = fn(result, elm);
  }
  return result;
}

public list[&T] java reverse(list[&T] lst)
@doc{reverse -- elements of a list in reverse order}
@javaClass{org.meta_environment.rascal.std.List};

public int java size(list[&T] lst)
@doc{size -- number of elements in a list}
@javaClass{org.meta_environment.rascal.std.List};

public list[&T] java slice(list[&T] lst, int start, int len)
@doc{slice -- sublist from start of length len}
@javaClass{org.meta_environment.rascal.std.List};

public list[&T] sort(list[&T] lst)
@doc{sort -- sort the elements of a list}
{
  if(size(lst) <= 1){
  	return lst;
  }
  
  list[&T] less = [];
  list[&T] greater = [];
  &T pivot = lst[0];
  
  <pivot, lst> = takeOneFrom(lst);
  
  for(&T elm <- lst){
     if(elm <= pivot){
       less = elm + less;
     } else {
       greater = elm + greater;
     }
  }
  
  return sort(less) + pivot + sort(greater);
}

public list[&T] java tail(list[&T] lst)
@doc{tail -- all but the first element of a list}
@javaClass{org.meta_environment.rascal.std.List};
 
public list[&T] java tail(list[&T] lst, int len)throws IndexOutOfBoundsError
@doc{tail -- last n elements of a list}
@javaClass{org.meta_environment.rascal.std.List};
 
public tuple[&T, list[&T]] java takeOneFrom(list[&T] lst)
@doc{takeOneFrom -- remove an arbitrary element from a list, returns the element and the modified list}
@javaClass{org.meta_environment.rascal.std.List};

public map[&A,set[&B]] java toMap(list[tuple[&A, &B]] lst) throws DuplicateKey
@doc{toMap -- convert a list of tuples to a map; first elements are associated with a set of second elements}
@javaClass{org.meta_environment.rascal.std.List};

public map[&A,&B] java toMapUnique(list[tuple[&A, &B]] lst) throws DuplicateKey
@doc{toMapUnique -- convert a list of tuples to a map; result must be a map}
@javaClass{org.meta_environment.rascal.std.List};

public set[&T] java toSet(list[&T] lst)
@doc{toSet -- convert a list to a set}
@javaClass{org.meta_environment.rascal.std.List};

public str java toString(list[&T] lst)
@doc{toString -- convert a list to a string}
@javaClass{org.meta_environment.rascal.std.List};
