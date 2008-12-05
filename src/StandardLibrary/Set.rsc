module Set

//TODO: public &T arb(set[&T] S)
public value java arb(set[value] s)
@java-imports{import java.util.Iterator;}
{
   int i = 0;
   int k = random.nextInt(s.size());
   Iterator iter = s.iterator();
  
   while(iter.hasNext()){
      if(i == k){
      	return (IValue) iter.next();
      }
      iter.next();
      i++;
   }
   return null;
}
  
//public &T average(set[&T] S, &T zero)
 //@doc{Average of elements of a set}
//{
//  return sum(S, zero)/size(R);
//
//

//public set[&T] mapper(set[&T] S, &T F (&T,&T)){
//  return {F(E) | &T E : S};
//}

//public &T min(set[&T] S)
//  @doc{Minimum of a set}
//{
//  &T result = arb(S);
//  for(&T E : S){
//    result = min(result, E);
 // }
 // return result;
//}

//public &T max(set[&T] R)
//  @doc{Maximum of a set}
//{
//  &T result = arb(R);
//  for(&T E : R){
//    result = max(result, E);
//  }
//  return result;
//}

// TODO: public &T min(set[&T] S)
public value min(set[value] S)
{
  value result = arb(S);
  for(value e : S){
   if(less(e, result)){
      result = min(result, e);
   }
  }
  return result;
}

//public &T multiply(set[&T] S, &T unity)
//  @doc{Multiply elements of a Set}
//{
//  return reducer(S, *, unity);
//}

//public &T reducer(set[&T] S, &T F (&T,&T), &T unit){
 // &T result = unit;
 // for(&T E : S){
 //   result = F(result, E);
//  }
//  return result;
//}


//TODO: public int java size(set[&T] S)
public int java size(set[value] S)
{
   return values.integer(S.size());
}
 
 //public &T sum(set[&T] S, &T zero)
 // @doc{Sum elements of a Set: sum}
//{
//  return reducer(S, +, zero);
//}
  
//TODO: public list[&T] java toList(set[&T] S)
public list[value] java toList(set[value] S)
@java-imports{import java.util.Iterator;}
{
  Type resultType = types.setType(S.getElementType());
  IListWriter w = resultType.writer(values);
  Iterator iter = S.iterator();
  while (iter.hasNext()) {
    w.insert((IValue) iter.next());
  }
	
  return w.done();
}

 // public map[&T, &U] toMap(set[tuple[&T, &U]] S)
 // throws non_unique_domain(str msg)
//  @primitive{"Set.toMap"}
  

//public rel[&T] toRel(set[&T] S)
//   @primitive{"Set.toRel"}

//TODO: public str toString(set[&T] S)
public str java toString(set[value] S)
{
	return values.string(S.toString());
}

// TODO

// Powerset: power0
//%%public set[set[&T]] power0(set[&T] R)
//%% throw unimplemented("power0")

//%% Powerset: power1
//%%public set[set[&T]] power1(set[&T] R)
//%%  throw unimplemented("power0")
