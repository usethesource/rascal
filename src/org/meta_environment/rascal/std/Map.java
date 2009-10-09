package org.meta_environment.rascal.std;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;


public class Map {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();
	private static final Random random = new Random();
	

	public static IValue domain(IMap M)
	//@doc{domain -- return the domain (keys) of a map}

	{
	  Type keyType = M.getKeyType();
	  
	  Type resultType = types.setType(keyType);
	  ISetWriter w = resultType.writer(values);
	  Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
	  while (iter.hasNext()) {
	    Entry<IValue,IValue> entry = iter.next();
	    w.insert(entry.getKey());
	  }
	  return w.done();
	}

	public static IValue getOneFrom(IMap m)  
	//@doc{getOneFrom -- return arbitrary key of a map}
	{
	   int i = 0;
	   int sz = m.size();
	   if(sz == 0){
	      throw RuntimeExceptionFactory.emptyMap(null, null);
	   }
	   int k = random.nextInt(sz);
	   Iterator<Entry<IValue,IValue>> iter = m.entryIterator();
	  
	   while(iter.hasNext()){
	      if(i == k){
	      	return (iter.next()).getKey();
	      }
	      iter.next();
	      i++;
	   }
	   return null;
	}
	
	public static IValue invertUnique(IMap M)
	//@doc{invertUnique -- return map with key and value inverted; values are unique}
	{
		Type keyType = M.getKeyType();
		Type valueType = M.getValueType();
		Type resultType = types.mapType(valueType, keyType);
		IMapWriter w = resultType.writer(values);
		HashSet<IValue> seenValues = new HashSet<IValue>();
		Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			IValue key = entry.getKey();
			IValue val = entry.getValue();
			if(seenValues.contains(val)) 
					throw RuntimeExceptionFactory.MultipleKey(val, null, null);
			seenValues.add(val);
			w.put(val, key);
		}
		return w.done();
	}
	
	public static IValue invert(IMap M)
	//@doc{invert -- return map with key and value inverted; values are not unique and are collected in a set}
	{
		Type keyType = M.getKeyType();
		Type valueType = M.getValueType();
		Type keySetType = types.setType(keyType);
	
		HashMap<IValue,ISetWriter> hm = new HashMap<IValue,ISetWriter>();
		Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			IValue key = entry.getKey();
			IValue val = entry.getValue();
			ISetWriter wKeySet = hm.get(val);
			if(wKeySet == null){
				wKeySet = keySetType.writer(values);
				hm.put(val, wKeySet);
			}
			wKeySet.insert(key);
		}
		
		Type resultType = types.mapType(valueType, keySetType);
		IMapWriter w = resultType.writer(values);
		
		iter = M.entryIterator();
		for(IValue v : hm.keySet()){
			w.put(v, hm.get(v).done());
		}
		return w.done();
	}
	
	public static IValue isEmpty(IMap M)
	//@doc{isEmpty -- is map empty?}
	{
		return values.bool(M.size() == 0);
	}

	public static  IValue range(IMap M)
	//@doc{range -- return the range (values) of a map}
	{
	  Type valueType = M.getValueType();
	  
	  Type resultType = types.setType(valueType);
	  ISetWriter w = resultType.writer(values);
	  Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
	  while (iter.hasNext()) {
	    Entry<IValue,IValue> entry = iter.next();
	    w.insert(entry.getValue());
	  }
	  return w.done();
	}

	public static IValue size(IMap M)
	{
		return values.integer(M.size());
	}

	public static IValue toList(IMap M)
	//@doc{toList -- convert a map to a list}
	{
	  Type keyType = M.getKeyType();
	  Type valueType = M.getValueType();
	  Type elementType = types.tupleType(keyType,valueType);
	  
	  Type resultType = types.listType(elementType);
	  IListWriter w = resultType.writer(values);
	  Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
	  while (iter.hasNext()) {
	    Entry<IValue,IValue> entry = iter.next();
	    w.insert(values.tuple(entry.getKey(), entry.getValue()));
	  }
	  return w.done();
	}

	public static IValue toRel(IMap M)
	//@doc{toRel -- convert a map to a relation}
	{
	  Type keyType = M.getKeyType();
	  Type valueType = M.getValueType();
	  
	  Type resultType = types.relType(keyType, valueType);
	  IRelationWriter w = resultType.writer(values);
	  Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
	  while (iter.hasNext()) {
	    Entry<IValue,IValue> entry = iter.next();
	    w.insert(values.tuple(entry.getKey(), entry.getValue()));
	  }
	  return w.done();
	}
	  
	public static IValue toString(IMap M)
	{
	  return values.string(M.toString());
	}

}
