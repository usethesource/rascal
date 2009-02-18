package org.meta_environment.rascal.std;

import java.util.Iterator;
import java.util.Random;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.errors.EmptySetError;

public class Set {

	private static final ValueFactory values = ValueFactory.getInstance();
	private static final TypeFactory types = TypeFactory.getInstance();
	private static final Random random = new Random();
	

	public static IValue getOneFrom(ISet st)
	//@doc{getOneFrom -- pick a random element from a set}
	{
	   int i = 0;
	   int sz = st.size();
	   
	   if(sz == 0){
	   	  throw new EmptySetError("getOneFrom", null);
	   }
	   int k = random.nextInt(sz);
	   Iterator<IValue> iter = st.iterator();
	  
	   while(iter.hasNext()){
	      if(i == k){
	      	return (IValue) iter.next();
	      }
	      iter.next();
	      i++;
	   }
	   return null;
	}

	public static IValue size(ISet st)
	//@doc{size -- number of elements in a set}
	{
	   return values.integer(st.size());
	}
	
	public static IValue takeOneFrom(ISet st)
	//@doc{takeOneFrom -- remove an arbitrary element from a set, returns the element and the modified set}
	{
	   int n = st.size();
	   
	   if(n > 0){
	      int i = 0;
	   	  int k = random.nextInt(n);
	   	  IValue pick = null;
	   	  ISetWriter w = st.getType().writer(values);
	   	  Iterator<IValue> iter = st.iterator();
	  
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
	   		throw new EmptySetError("takeOneFrom", null);
	   	}
	}
	  
	public static IValue toList(ISet st)
	//@doc{toList -- convert a set to a list}
	{
	  Type resultType = types.listType(st.getElementType());
	  IListWriter w = resultType.writer(values);
	  Iterator<IValue> iter = st.iterator();
	  while (iter.hasNext()) {
	    w.insert((IValue) iter.next());
	  }
		
	  return w.done();
	}

	// TODO: multiple elements in map?

	public static IValue toMap(ISet st)
	//@doc{toMap -- convert a set of tuples to a map}
	{
	   Type tuple = st.getElementType();
	   Type resultType = types.mapType(tuple.getFieldType(0), tuple.getFieldType(1));
	  
	   IMapWriter w = resultType.writer(values);
	   Iterator<IValue> iter = st.iterator();
	   while (iter.hasNext()) {
	     ITuple t = (ITuple) iter.next();
	     w.put(t.get(0), t.get(1));
	   }
	   return w.done();
	}

	public static IValue toString(ISet st)
	//@doc{toString -- convert a set to a string}
	{
		return values.string(st.toString());
	}




	
	
}
