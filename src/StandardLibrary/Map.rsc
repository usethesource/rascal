module Map

public &K java arb(map[&K, &V] m)  
@java-imports{import java.util.Iterator;import java.util.Map.Entry; }
{
   int i = 0;
   int k = random.nextInt(m.size());
   Iterator iter = m.entryIterator();
  
   while(iter.hasNext()){
      if(i == k){
      	return (IValue) ((Entry) iter.next()).getKey();
      }
      iter.next();
      i++;
   }
   return null;
} 

public map[&K, &V] mapper(map[&K, &V] M, &T (&T,&T) F){
  return {#F(E) | &T E : M};
}

public int java size(map[&K, &V] M)
{
	return values.integer(M.size());
}

public list[tuple[&K, &V]] toList(map[&K, &V] M)
{
}
  
public rel[tuple[&K, &V]] toRel(map[&K, &V] M)
{
}

public set[tuple[&K, &V]] toSet(map[&K, &V] M)
{
}
  
public str java toString(map[&K, &V] M)
{
  return values.string(M.toString());
}




