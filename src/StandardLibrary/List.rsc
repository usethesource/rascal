module List

public &T java arb(list[&T] l)
JavaImports{java.util.Random;}
{
  java.util.Random generator = new java.util.Random(System.currentTimeMillis());
  return l.get(generator.nextInteger(l.size()));
}

public &T average(list[&T] l, &T zero)
@doc{Average of elements of a list: average}
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
      throw new RascalException("empty_list");
   }
 }



public list[&T] mapper(list[&T] L, &T F (&T,&T)){
  return [F(E) | &T E : L];
}

public &T max(list[&T] l)
@doc{Maximum element of a list: max}
{
  &T result = arb(l);
  for(&T e : l){
   if(result < e)){
      result = max(result, e);
   }
  }
  return result;
}

public &T min(&list[&T] l)
{
  &T result = arb(l);
  for(&T e : l){
   if(less(e, result)){
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

public &T reducer(list[&T] l, &T F(&T, &T), &T unit)
{
  &T result = unit;
  for(&T e : l){
     result = F(result, e);
  }
  return result;
}

public &T java rest(list[&T] l)
  throws empty_list()
 @doc{First element of list: first}
 { ILiostWriter w = l.getType().writer(values);
 
   if(l.length() > 0){
      for(int i : 1; i < l.length(); i++){
      w.put(l.get(i);
      }
      return w.done();
   } else {
      throw new RascalException("empty_list");
   }
 }

public list[&T] java reverse(list[&T] l)
{
	return l.reverse();
}

public int java size(list[&T] l)
{
   return l.size();
}

public list[&T] sort(list[&T] L, bool #<(&T, &T))
  @doc{Sort elements of list: sort}
{
 // To be done.
}

public &T sum(list[&T] l, &T zero)
@doc{Add elements of a List: sum}
{
  return reducer(l, #+, zero);
}

public set[&T] java toSet(list[&T] l)
{
   Type resultType = types.set(l.getElementType());
   ISetWriter w = resultType.writer(values);
   for(value v : l){
   w.insert(v);
   }
	
   return w.done();
}

//public set[&T] java toMap(list[tuple[&K, &V]] l)
//{
//  Type resultType = types.set(l.getElementType());
//  ISetWriter w = resultType.writer(values);
//   for(value v : l){
//   		w.insert(v);
//   }
//  return w.done();
//}

public str java toString(list[&T] l)
{
	return l.toString();
}
