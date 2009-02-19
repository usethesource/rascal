module Set
 
public &T average(set[&T] st, &T zero)
@doc{average -- compute the average of the elements of a set}
{
  return size(st) > 0 ? sum(st, zero)/size(st) : zero;
}

public &T java getOneFrom(set[&T] st)
@doc{getOneFrom -- pick a random element from a set}
@javaClass{org.meta_environment.rascal.std.Set};

public set[&T] mapper(set[&T] st, &T (&T,&T) fn)
@doc{mapper -- apply a function to each element of a set}
{
  return {#fn(elm) | &T elm <- st};
}

public &T max(set[&T] st)
@doc{max -- largest element of a set}
{
  &T result = getOneFrom(st);
  for(&T elm <- st){
  	if(elm > result){
    	result = elm;
    }
  }
  return result;
}

public &T min(set[&T] st)
@doc{min -- smallest element of a set}
{
  &T result = getOneFrom(st);
  for(&T elm <- st){
   if(elm < result){
      result = elm;
   }
  }
  return result;
}

// TODO: auxiliary function needed as long as #* function names do not work.

&T mul(&T x, &T y)
{
	return x * y;
}

public &T multiply(set[&T] st, &T unity)
@doc{multiply -- multiply the elements of a set}
{
  return reducer(st, #mul, unity);
}

public set[set[&T]] power(set[&T] st)
@doc{power -- return all subsets of a set}
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

public set[set[&T]] power1(set[&T] st)
@doc{power1-- return all subsets (excluding empty set) of a set}
{
	return power(st) - {{}};
}

public &T reducer(set[&T] st, &T (&T,&T) fn, &T unit)
@doc{reducer -- apply function F to successive elements of a set}
{
  &T result = unit;
  for(&T elm <- st){
    result = #fn(result, elm);
  }
  return result;
}

public int java size(set[&T] st)
@doc{size -- number of elements in a set}
@javaClass{org.meta_environment.rascal.std.Set};

// TODO: auxiliary function needed as long as #+ function names do not work.

&T add(&T x, &T y)
{
	return x + y;
}
 
public &T sum(set[&T] st, &T zero)
@doc{sum -- add the elements of a set}
{
  return reducer(st, #add, zero);
}

public tuple[&T, set[&T]] java takeOneFrom(set[&T] st)
@doc{takeOneFrom -- remove an arbitrary element from a set, returns the element and the modified set}
@javaClass{org.meta_environment.rascal.std.Set};
  
public list[&T] java toList(set[&T] st)
@doc{toList -- convert a set to a list}
@javaClass{org.meta_environment.rascal.std.Set};

// TODO: multiple elements in map?

public map[&A,&B] java toMap(rel[&A, &B] st)
@doc{toMap -- convert a set of tuples to a map}
@javaClass{org.meta_environment.rascal.std.Set};

public str java toString(set[&T] st)
@doc{toString -- convert a set to a string}
@javaClass{org.meta_environment.rascal.std.Set};



