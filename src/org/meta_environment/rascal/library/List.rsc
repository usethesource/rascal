module List

@doc{Delete nth element from list}
@javaClass{org.meta_environment.rascal.library.List}
public list[&T] java delete(list[&T] lst, int n);

@doc{A list of all legal index values for a list}
@javaClass{org.meta_environment.rascal.library.List}
public set[int] java domain(list[&T] lst);

@doc{Get the first element of a list}
@javaClass{org.meta_environment.rascal.library.List}
public &T java head(list[&T] lst) throws EmptyListError;

@doc{Get the first n elements of a list}
@javaClass{org.meta_environment.rascal.library.List}
public list[&T] java head(list[&T] lst, int n) throws IndexOutOfBoundsError;

@doc{Get an arbitrary element from a list}
@javaClass{org.meta_environment.rascal.library.List}
public &T java getOneFrom(list[&T] lst);

@doc{Add an element at a specific position in a list}
@javaClass{org.meta_environment.rascal.library.List}
public list[&T] java insertAt(list[&T] lst, int n, &T elm) throws IndexOutOfBoundsError;
 
@doc{Is list empty?}
@javaClass{org.meta_environment.rascal.library.List}
public bool java isEmpty(list[&T] lst);

@doc{Apply a function to each element of a list}
public list[&U] mapper(list[&T] lst, &U (&T) fn)
{
  return [fn(elm) | &T elm <- lst];
}

@doc{Largest element of a list}
public &T max(list[&T] lst)
{
  &T result = getOneFrom(lst);
  for(&T elm <- lst) {
   if(result < elm) {
      result = elm;
   }
  }
  return result;
}

@doc{Smallest element of a list}
public &T min(list[&T] lst)
{
  &T result = getOneFrom(lst);
  for(&T elm <- lst){
   if(elm < result){
      result = elm;
   }
  }
  return result;
}

@doc{Return all permutations of a list}
public set[list[&T]] permutations(list[&T] lst)
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

@doc{Apply function F to successive elements of a list}
public &T reducer(list[&T] lst, &T (&T, &T) fn, &T unit)
{
  &T result = unit;
  for(&T elm <- lst){
     result = fn(result, elm);
  }
  return result;
}

@doc{Elements of a list in reverse order}
@javaClass{org.meta_environment.rascal.library.List}
public list[&T] java reverse(list[&T] lst);

@doc{Number of elements in a list}
@javaClass{org.meta_environment.rascal.library.List}
public int java size(list[&T] lst);

@doc{Sublist from start of length len}
@javaClass{org.meta_environment.rascal.library.List}
public list[&T] java slice(list[&T] lst, int start, int len);

@doc{Sort the elements of a list}
public list[&T] sort(list[&T] lst)
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

@doc{Sort the elements of a list}
public list[&T] sort(list[&T] lst, bool (&T a, &T b) lessThanOrEqual)
{
  if(size(lst) <= 1){
  	return lst;
  }
  
  list[&T] less = [];
  list[&T] greater = [];
  &T pivot = lst[0];
  
  <pivot, lst> = takeOneFrom(lst);
  
  for(&T elm <- lst){
     if(lessThanOrEqual(elm,pivot)){
       less = elm + less;
     } else {
       greater = elm + greater;
     }
  }
  
  return sort(less, lessThanOrEqual) + pivot + sort(greater, lessThanOrEqual);
}

@doc{All but the first element of a list}
@javaClass{org.meta_environment.rascal.library.List}
public list[&T] java tail(list[&T] lst);
 
@doc{Last n elements of a list}
@javaClass{org.meta_environment.rascal.library.List}
public list[&T] java tail(list[&T] lst, int len) throws IndexOutOfBoundsError;
 
@doc{Remove an arbitrary element from a list, returns the element and the modified list}
@javaClass{org.meta_environment.rascal.library.List}
public tuple[&T, list[&T]] java takeOneFrom(list[&T] lst);

@doc{Convert a list of tuples to a map; first elements are associated with a set of second elements}
@javaClass{org.meta_environment.rascal.library.List}
public map[&A,set[&B]] java toMap(list[tuple[&A, &B]] lst) throws DuplicateKey;

@doc{Convert a list of tuples to a map; result must be a map}
@javaClass{org.meta_environment.rascal.library.List}
public map[&A,&B] java toMapUnique(list[tuple[&A, &B]] lst) throws DuplicateKey;

@doc{Convert a list to a set}
@javaClass{org.meta_environment.rascal.library.List}
public set[&T] java toSet(list[&T] lst);

@doc{Convert a list to a string}
@javaClass{org.meta_environment.rascal.library.List}
public str java toString(list[&T] lst);
