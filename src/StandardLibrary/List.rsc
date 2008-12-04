module List

public value java arb(list[value] l)
@java-imports{import java.lang.Math;}
{
   double rnd = l.length() * Math.random();
   return l.get(values.dubble(rnd).floor().getValue());
}

public value average(list[value] l, value zero)
@doc{Average of elements of a list: average}
{
  return sum(l, zero)/size(l);
}

public value java first(list[value] l)
  throws empty_list()
  @doc{First element of list: first}
{
   if(l.length() > 0){
      return l.get(0);
   } else {
      throw new RascalException(values, "empty_list");
   }
}

public list[value] mapper(list[value] L, value (value x,value y) F) {
  return [#F(E) | value E : L];
}

public value max(list[value] l)
@doc{Maximum element of a list: max}
{
  value result = arb(l);
  for(value e : l) {
   if(result < e) {
      result = max(result, e);
   }
  }
  return result;
}

public value min(list[value] l)
{
  value result = arb(l);
  for(value e : l){
   if(less(e, result)){
      result = min(result, e);
   }
  }
  return result;
}

public value multiply(list[value] l, value unity)
@doc{Multiply elements of a list: multiply}
{
  return reducer(l, #*, unity);
}

public value reducer(list[value] l, value (value, value) F, value unit)
{
  value result = unit;
  for(value e : l){
     result = F(result, e);
  }
  return result;
}

public value java rest(list[value] l)
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

public list[value] java reverse(list[value] l)
{
	return l.reverse();
}

public int java size(list[value] l)
{
   return values.integer(l.length());
}

public list[value] sort(list[value] L, bool (value, value) compare)
  @doc{Sort elements of list: sort}
{
 // To be done.
}

public value sum(list[value] l, value zero)
@doc{Add elements of a List: sum}
{
  return reducer(l, #+, zero);
}

public set[value] java toSet(list[value] l)
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

public map[value,value] java toMap(list[tuple[value, value]] l)
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

public str java toString(list[value] l)
{
	return values.string(l.toString());
}
