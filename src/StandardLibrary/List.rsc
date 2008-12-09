module List

public &T java arb(list[&T] l)
@doc(arb -- arbitrary element from list}
{
   return l.get(random.nextInt(l.length()));
}

public &T average(list[&T] l, &T zero)
@doc{average -- average of elements of a list}
{
  return sum(l, zero)/size(l);
}

public &T java first(list[&T] l)
  throws empty_list()
  @doc{First element of list: first}
{
   if(l.length() > 0){
      return l.get(0);
   } else {
      throw new RascalException(values, "empty_list");
   }
}

list[&T] mapper(list[&T] L, &T (&T x,&T y) F) {
  return [#F(E) | value E : L];
}

public &T max(list[&T] l)
@doc{Maximum element of a list: max}
{
  &T result = List::arb(l);
  for(&T e : l) {
   if(result < e) {
      result = max(result, e);
   }
  }
  return result;
}

public &T min(list[&T] l)
{
  &T result = List::arb(l);
  for(&T e : l){
   if(e < result){
      result = min(result, e);
   }
  }
  return result;
}

public &T multiply(list[&T] l, &T unity)
@doc{Multiply elements of a list: multiply}
{
  return reducer(l, #*, unity);
}

public &T reducer(list[&T] l, &T (&T, &T) F, &T unit)
{
  &T result = unit;
  for(&T e : l){
     result = #F(result, e);
  }
  return result;
}

public &T java rest(list[&T] l)
  throws empty_list()
 @doc{First element of list: first}
 { IListWriter w = l.getType().writer(values);
 
   if(l.length() > 0){
      for(int i = 1; i < l.length(); i++) {
        w.insert(l.get(i));
      }
      return w.done();
   } else {
      throw new RascalException(values, "empty_list");
   }
 }

public list[&T] java reverse(list[&T] l)
{
	return l.reverse();
}

public int java size(list[&T] l)
{
   return values.integer(l.length());
}

public list[&T] sort(list[&T] L, bool (&T, &T) compare)
  @doc{Sort elements of list: sort}
{
 // To be done.
}

public &T sum(list[&T] l, &T zero)
@doc{Add elements of a List: sum}
{
  return reducer(l, #+, zero);
}

public map[&A,&B] java toMap(list[tuple[&A, &B]] l)
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
{
	return values.string(l.toString());
}
