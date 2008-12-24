module List
 
 
public &T average(list[&T] lst, &T zero)
@doc{average -- average of elements of a list}
{
  return size(lst) > 0 ? sum(lst, zero)/size(lst) : zero;
}

public &T java first(list[&T] lst)
  throws empty_list()
 @doc{first -- get the first element of a list}
{
   if(lst.length() > 0){
      return lst.get(0);
   } else {
      throw new RascalException(values, "empty_list");
   }
}

public &T java getOneFrom(list[&T] lst)
@doc{getOneFrom -- get an arbitrary element from a list}
{
   int n = lst.length();
   if(n > 0){
   	return lst.get(random.nextInt(n));
   	} else {
   		throw new RascalException(values, "empty_list");
   	}
}

public list[&T] java insertAt(&T elm, int n, list[&T] lst)
  throws list_index()
 @doc{insertAt -- add an element at a specific position in a list}
 {
 	IListWriter w = lst.getType().writer(values);
 	int k = n.getValue();
    if(k >= 0 && k <= lst.length()){
      if(k == lst.length()){
      	w.insert(elm);
      }
      for(int i = lst.length()-1; i >= 0; i--) {
        w.insert(lst.get(i));
        if(i == k){
        	w.insert(elm);
        }
      }
      return w.done();
    } else {
    	throw new RascalException(values, "list_index");
    }
 }


public list[&T] mapper(list[&T] lst, &T (&T) fn)
@doc{mapper -- apply a function to each element of a list}
{
  return [#fn(elm) | &T elm : lst];
}

public &T max(list[&T] lst)
@doc{max -- largest element of a list}
{
  &T result = getOneFrom(lst);
  for(&T elm : lst) {
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
  for(&T elm : lst){
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

public &T multiply(list[&T] lst, &T unity)
@doc{multiply -- multiply the elements of a list}
{
  return reducer(lst, #mul, unity);
}

public &T reducer(list[&T] lst, &T (&T, &T) fn, &T unit)
@doc{reducer -- apply function F to successive elements of a list}
{
  &T result = unit;
  for(&T elm : lst){
     result = #fn(result, elm);
  }
  return result;
}

public list[&T] java rest(list[&T] lst)
  throws empty_list()
 @doc{rest -- all but the first element of a list}
 { IListWriter w = lst.getType().writer(values);
 
   if(lst.length() > 0){
      for(int i = lst.length()-1; i > 0; i--) {
        w.insert(lst.get(i));
      }
      return w.done();
   } else {
      throw new RascalException(values, "empty_list");
   }
 }
 

public list[&T] java reverse(list[&T] lst)
@doc{reverse -- elements of a list in reverse order}
{
	return lst.reverse();
}

public int java size(list[&T] lst)
@doc{size -- number of elements in a list}
{
   return values.integer(lst.length());
}

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
  
  for(&T elm : lst){
     if(elm <= pivot){
       less = elm + less;
     } else {
       greater = elm + greater;
     }
  }
 
  return sort(less) + pivot + sort(greater);
}

// TODO: auxiliary function needed as long as #+ function names do not work.

&T add(&T x, &T y)
{
	return x + y;
}

public &T sum(list[&T] lst, &T zero)
@doc{sum -- add elements of a List}
{
  return reducer(lst, #add, zero);
}

public tuple[&T, list[&T]] java takeOneFrom(list[&T] lst)
@doc{takeOneFrom -- remove an arbitrary element from a list, returns the element and the modified list}
{
   int n = lst.length();
   
   if(n > 0){
   	  int k = random.nextInt(n);
   	  IValue pick = lst.get(0);
   	  IListWriter w = lst.getType().writer(values);
  
      for(int i = n - 1; i >= 0; i--) {
         if(i == k){
         	pick = lst.get(i);
         } else {
            w.insert(lst.get(i));
         }
      }
      return values.tuple(pick, w.done());
   	} else {
   		throw new RascalException(values, "empty_list");
   	}
}

public map[&A,&B] java toMap(list[tuple[&A, &B]] lst)
@doc{toMap -- convert a list of tuples to a map}
@java-imports{import java.util.Iterator;}
{
   if(lst.length() == 0){
      return values.map(types.voidType(), types.voidType());
   }
   Type tuple = lst.getElementType();
   Type resultType = types.mapType(tuple.getFieldType(0), tuple.getFieldType(1));
  
   IMapWriter w = resultType.writer(values);
   Iterator iter = lst.iterator();
   while (iter.hasNext()) {
     ITuple t = (ITuple) iter.next();
     w.put(t.get(0), t.get(1));
   }
   return w.done();
}

public set[&T] java toSet(list[&T] lst)
@doc{toSet -- convert a list to a set}
@java-imports{import java.util.Iterator;}
{
  Type resultType = types.setType(lst.getElementType());
  ISetWriter w = resultType.writer(values);
  Iterator iter = lst.iterator();
  while (iter.hasNext()) {
    w.insert((IValue) iter.next());
  }
	
  return w.done();
}

public str java toString(list[&T] lst)
@doc{toString -- convert a list to a string}
{
	return values.string(lst.toString());
}
