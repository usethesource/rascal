module Set

public &T java arb(set[&T] s)
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
  
public &T average(set[&T] S, &T zero)
@doc{Average of elements of a set}
{
  return sum(S, zero)/size(R);
}

public set[&T] mapper(set[&T] S, &T (&T,&T) F){
  return {#F(E) | &T E : S};
}

public &T max(set[&T] R)
@doc{Maximum of a set}
{
  &T result = arb(R);
  for(&T E : R){
    result = max(result, E);
  }
  return result;
}

public &T min(set[&T] S)
{
  &T result = arb(S);
  for(&T e : S){
   if(e < result){
      result = min(result, e);
   }
  }
  return result;
}

public &T multiply(set[&T] S, &T unity)
@doc{Multiply elements of a Set}
{
  return reducer(S, #*, unity);
}

public &T reducer(set[&T] S, &T (&T,&T) F, &T unit)
{
  &T result = unit;
  for(&T E : S){
    result = F(result, E);
  }
  return result;
}

public int java size(set[&T] S)
{
   return values.integer(S.size());
}
 
public &T sum(set[&T] S, &T zero)
@doc{Sum elements of a Set: sum}
{
  return reducer(S, #+, zero);
}
  
public list[&T] java toList(set[&T] S)
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

public str java toString(set[&T] S)
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
