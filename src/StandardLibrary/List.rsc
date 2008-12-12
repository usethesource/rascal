module List

public &T java arb(list[&T] l)
@doc{arb -- pick an arbitrary element from a list}
{
   int n = l.length();
   if(n > 0){
   	return l.get(random.nextInt(n));
   	} else {
   		throw new RascalException(values, "empty_list");
   	}
}

public &T average(list[&T] l, &T zero)
@doc{average -- average of elements of a list}
{
  return sum(l, zero)/size(l);
}

public &T java first(list[&T] l)
  throws empty_list()
  @doc{first -- take first element of a list}
{
   if(l.length() > 0){
      return l.get(0);
   } else {
      throw new RascalException(values, "empty_list");
   }
}

list[&T] mapper(list[&T] L, &T (&T x,&T y) F)
@doc{mapper -- apply a function to each element of a list}
{
  return [#F(E) | value E : L];
}

public &T max(list[&T] l)
@doc{max -- largest element of a list}
{
  &T result = List::arb(l);
  for(&T e : l) {
   if(result < e) {
      result = e;
   }
  }
  return result;
}

public &T min(list[&T] l)
@doc{min -- smallest element of a list}
{
  &T result = List::arb(l);
  for(&T e : l){
   if(e < result){
      result = e;
   }
  }
  return result;
}

public &T multiply(list[&T] l, &T unity)
@doc{multiply -- multiply the elements of a list}
{
  return reducer(l, #*, unity);
}

public &T reducer(list[&T] l, &T (&T, &T) F, &T unit)
@doc{reducer -- apply function F to successive elements of a list}
{
  &T result = unit;
  for(&T e : l){
     result = #F(result, e);
  }
  return result;
}

public list[&T] java rest(list[&T] l)
  throws empty_list()
 @doc{rest -- all but the first element of a list}
 { IListWriter w = l.getType().writer(values);
 
   if(l.length() > 0){
      for(int i = l.length()-1; i > 0; i--) {
        w.insert(l.get(i));
      }
      return w.done();
   } else {
      throw new RascalException(values, "empty_list");
   }
 }

public list[&T] java reverse(list[&T] l)
@doc{reverse -- elements of a list in reverse order}
{
	return l.reverse();
}

public int java size(list[&T] l)
@doc{size -- number of elements in a list}
{
   return values.integer(l.length());
}

public list[&T] sort(list[&T] L, bool (&T, &T) compare)
  @doc{Sort elements of list: sort}
{
 // To be done.
}

public &T sum(list[&T] l, &T zero)
@doc{sum -- add elements of a List}
{
  return reducer(l, #+, zero);
}

public map[&A,&B] java toMap(list[tuple[&A, &B]] l)
@doc{toMap -- convert a list of tuples to a map}
@java-imports{import java.util.Iterator;}
{
   TupleType tuple = (TupleType) l.getElementType();
   Type resultType = types.mapType(tuple.getFieldType(0), tuple.getFieldType(1));
  
   IMapWriter w = resultType.writer(values);
   Iterator iter = l.iterator();
   while (iter.hasNext()) {
     ITuple t = (ITuple) iter.next();
     w.put(t.get(0), t.get(1));
   }
   return w.done();
}

public set[&T] java toSet(list[&T] l)
@doc{toSet -- convert a list to a set}
@java-imports{import java.util.Iterator;}
{
  Type resultType = types.setType(l.getElementType());
  ISetWriter w = resultType.writer(values);
  Iterator iter = l.iterator();
  while (iter.hasNext()) {
    w.insert((IValue) iter.next());
  }
	
  return w.done();
}

public str java toString(list[&T] l)
@doc{toString -- convert a list to a string}
{
	return values.string(l.toString());
}
