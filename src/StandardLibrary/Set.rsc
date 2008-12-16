module Set

public set[&T] java add(&T elm, set[&T] st)
 {
    return st.insert(elm);
 }

public &T java getOneFrom(set[&T] st)
@doc{arb -- pick a random element from a set}
@java-imports{import java.util.Iterator;}
{
   int i = 0;
   int k = random.nextInt(st.size());
   Iterator iter = st.iterator();
  
   while(iter.hasNext()){
      if(i == k){
      	return (IValue) iter.next();
      }
      iter.next();
      i++;
   }
   return null;
}

public tuple[&T, list[&T]] java takeOneFrom(set[&T] st)
@doc{takeOneFrom -- take (and remove) an arbitrary element from a set}
@java-imports{import java.util.Iterator;}
{
   int n = st.size();
   
   if(n > 0){
      int i = 0;
   	  int k = random.nextInt(n);
   	  IValue pick = null;
   	  IListWriter w = st.getType().writer(values);
   	  Iterator iter = st.iterator();
  
      while(iter.hasNext()){
      	if(i == k){
      		pick = (IValue) iter.next();
      	} else {
      		w.insert((IValue) iter.next());
      	}
      i++;
   	  }
      return values.tuple(pick, w.done());
   	} else {
   		throw new RascalException(values, "empty_list");
   	}
}
  
public &T average(set[&T] st, &T zero)
@doc{average -- compute the average of the elements of a set}
{
  return sum(st, zero)/size(st);
}

public set[&T] mapper(set[&T] S, &T (&T,&T) F)
@doc{mapper -- apply a function to each element of a set}
{
  return {#F(E) | &T E : S};
}

public &T max(set[&T] R)
@doc{max -- largest element of a set}
{
  &T result = arb(R);
  for(&T e : R){
  	if(e > result){
    	result = e;
    }
  }
  return result;
}

public &T min(set[&T] S)
@doc{min -- smallest element of a set}
{
  &T result = arb(S);
  for(&T e : S){
   if(e < result){
      result = e;
   }
  }
  return result;
}

public &T multiply(set[&T] S, &T unity)
@doc{multiply -- multiply the elements of a set}
{
  return reducer(S, #*, unity);
}

public &T reducer(set[&T] S, &T (&T,&T) F, &T unit)
@doc{reducer -- apply function F to successive elements of a set}
{
  &T result = unit;
  for(&T E : S){
    result = F(result, E);
  }
  return result;
}

public int java size(set[&T] S)
@doc{size -- number of elements in a set}
{
   return values.integer(S.size());
}
 
public &T sum(set[&T] S, &T zero)
@doc{sum -- add the elements of a set}
{
  return reducer(S, #+, zero);
}
  
public list[&T] java toList(set[&T] S)
@doc{toList -- convert a set to a list}
@java-imports{import java.util.Iterator;}
{
  Type resultType = types.listType(S.getElementType());
  IListWriter w = resultType.writer(values);
  Iterator iter = S.iterator();
  while (iter.hasNext()) {
    w.insert((IValue) iter.next());
  }
	
  return w.done();
}

// TODO: multiple elements in map?
public map[&A,&B] java toMap(set[tuple[&A, &B]] l)
@doc{toMap -- convert a set of tuples to a map}
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

public str java toString(set[&T] S)
@doc{toString -- convert a set to a string}
{
	return values.string(S.toString());
}

public set[set[&T]] power(set[&T] S)
@doc{power -- return all subsets of S}
{
  set[set[&T]] result = {S};
  for(&T e : S){
  	set[set[&T]] pw = power(S - {e});
  	result = result + pw;
  	for(set[&T] sub : pw){
  		result = result + {sub + {e}};
  	}
  }
  return result;
}

