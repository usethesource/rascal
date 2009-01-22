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

public set[&K] java domain(map[&K, &V] M)
@doc{domain -- return the domain (keys) of a map}
@java-imports{
	import java.util.Iterator;
	import java.util.Map.Entry;
}
{
  Type keyType = M.getKeyType();
  
  Type resultType = types.setType(keyType);
  ISetWriter w = resultType.writer(values);
  Iterator iter = M.entryIterator();
  while (iter.hasNext()) {
    Entry entry = (Entry) iter.next();
    w.insert((IValue)entry.getKey());
  }
  return w.done();
}

public map[&K, &V] mapper(map[&K, &V] M, &K (&K) F, &V (&V) G){
  return (#F(key) : #G(M[key]) | &K key : M);
}

public set[&K] java range(map[&K, &V] M)
@doc{range -- return the range (values) of a map}
@java-imports{
	import java.util.Iterator;
	import java.util.Map.Entry;
}
{
  Type valueType = M.getValueType();
  
  Type resultType = types.setType(valueType);
  ISetWriter w = resultType.writer(values);
  Iterator iter = M.entryIterator();
  while (iter.hasNext()) {
    Entry entry = (Entry) iter.next();
    w.insert((IValue)entry.getValue());
  }
  return w.done();
}

public int java size(map[&K, &V] M)
{
	return values.integer(M.size());
}

public list[tuple[&K, &V]] java toList(map[&K, &V] M)
@doc{toList -- convert a map to a list}
@java-imports{
	import java.util.Iterator;
	import java.util.Map.Entry;
}
{
  Type keyType = M.getKeyType();
  Type valueType = M.getValueType();
  Type elementType = types.tupleType(keyType,valueType);
  
  Type resultType = types.listType(elementType);
  IListWriter w = resultType.writer(values);
  Iterator iter = M.entryIterator();
  while (iter.hasNext()) {
    Entry entry = (Entry) iter.next();
    w.insert(values.tuple((IValue)entry.getKey(), (IValue)entry.getValue()));
  }
  return w.done();
}

public rel[&K, &V] java toRel(map[&K, &V] M)
@doc{toRel -- convert a map to a relation}
@java-imports{
	import java.util.Iterator;
	import java.util.Map.Entry;
}
{
  Type keyType = M.getKeyType();
  Type valueType = M.getValueType();
  Type elementType = types.tupleType(keyType,valueType);
  
  Type resultType = types.relType(keyType, valueType);
  IRelationWriter w = resultType.writer(values);
  Iterator iter = M.entryIterator();
  while (iter.hasNext()) {
    Entry entry = (Entry) iter.next();
    w.insert(values.tuple((IValue)entry.getKey(), (IValue)entry.getValue()));
  }
  return w.done();
}
  
public str java toString(map[&K, &V] M)
{
  return values.string(M.toString());
}




