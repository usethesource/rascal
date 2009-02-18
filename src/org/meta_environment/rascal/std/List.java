package org.meta_environment.rascal.std;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.SubList;
import org.meta_environment.rascal.interpreter.errors.EmptyListError;
import org.meta_environment.rascal.interpreter.errors.IndexOutOfBoundsError;
import java.util.Random;

public class List {

	private static final ValueFactory values = ValueFactory.getInstance();
	private static final TypeFactory types = TypeFactory.getInstance();
	private static final Random random = new Random();
	
	public static IValue head(IList lst)
	  throws EmptyListError
	// @doc{head -- get the first element of a list}
	{
	   if(lst.length() > 0){
	      return lst.get(0);
	   } else {
	      throw new EmptyListError("head", null);
	   }
	}

	public static IValue head(IList lst, IInteger n)
	  throws IndexOutOfBoundsError
	// @doc{head -- get the first n elements of a list}
	{
	   if(n.getValue() <= lst.length()){
	      return new SubList((IValue)lst, 0, n.getValue());
	   } else {
	      throw new IndexOutOfBoundsError("head", null);
	   }
	}

	public static IValue getOneFrom(IList lst)
	//@doc{getOneFrom -- get an arbitrary element from a list}
	{
	   int n = lst.length();
	   if(n > 0){
	   	return lst.get(random.nextInt(n));
	   	} else {
	   		throw new EmptyListError("getOneFrom", null);
	   	}
	}

	public static IValue insertAt(IList lst, IInteger n, IValue elm)
	  throws IndexOutOfBoundsError
	 //@doc{insertAt -- add an element at a specific position in a list}
	 {
	 	IListWriter w = values.listWriter(elm.getType().lub(lst.getElementType()));
	 	
	 	int k = n.getValue();
	    if(k >= 0 && k <= lst.length()){
	      if(k == lst.length()){
	      	w.insert(elm);
	      }
	      for(int i = lst.length()-1; i >= 0; i--) {
	        w.insert(lst.get(i));
	        if(i == k){
	        	w.insert(elm);
	        }
	      }
	      return w.done();
	    } else {
	    	throw new IndexOutOfBoundsError("insertAt", null);
	    }
	 }

	public static IValue reverse(IList lst)
	//@doc{reverse -- elements of a list in reverse order}
	{
		return lst.reverse();
	}

	public static IValue size(IList lst)
	//@doc{size -- number of elements in a list}
	{
	   return values.integer(lst.length());
	}

	 public static IValue slice(IList lst, IInteger start, IInteger len)
	 //@doc{slice -- sublist from start of length len}
	 {
	 	return new SubList((IValue)lst, start.getValue(), len.getValue());
	 }

	 public static IValue tail(IList lst)
	 //@doc{tail -- all but the first element of a list}
	 {
	 	return new SubList((IValue)lst, 1, lst.length()-1);
	 }
	 
	  public static IValue tail(IList lst, IInteger len)
	  throws IndexOutOfBoundsError
	 //@doc{tail -- last n elements ofelement of a list}
	 {
	 	int lenVal = len.getValue();
	 	int lstLen = lst.length();
	 	
	 	if(lenVal > lstLen)
	 		throw new IndexOutOfBoundsError("tail", null);
	 	return new SubList((IValue)lst, lstLen - lenVal, lenVal);
	 }
	 
	public static IValue takeOneFrom(IList lst)
	//@doc{takeOneFrom -- remove an arbitrary element from a list, returns the element and the modified list}
	{
	   int n = lst.length();
	   
	   if(n > 0){
	   	  int k = random.nextInt(n);
	   	  IValue pick = lst.get(0);
	   	  IListWriter w = lst.getType().writer(values);
	  
	      for(int i = n - 1; i >= 0; i--) {
	         if(i == k){
	         	pick = lst.get(i);
	         } else {
	            w.insert(lst.get(i));
	         }
	      }
	      return values.tuple(pick, w.done());
	   	} else {
	   		throw new EmptyListError("takeOneFrom", null);
	   	}
	}

	public static IValue toMap(IList lst)
	//@doc{toMap -- convert a list of tuples to a map}
	//@javaImports{import java.util.Iterator;}
	{
	   if(lst.length() == 0){
	      return values.map(types.voidType(), types.voidType());
	   }
	   Type tuple = lst.getElementType();
	   Type resultType = types.mapType(tuple.getFieldType(0), tuple.getFieldType(1));
	  
	   IMapWriter w = resultType.writer(values);
	   Iterator iter = lst.iterator();
	   while (iter.hasNext()) {
	     ITuple t = (ITuple) iter.next();
	     w.put(t.get(0), t.get(1));
	   }
	   return w.done();
	}

	public static IValue toSet(IList lst)
	//@doc{toSet -- convert a list to a set}
	{
	  Type resultType = types.setType(lst.getElementType());
	  ISetWriter w = resultType.writer(values);
	  Iterator iter = lst.iterator();
	  while (iter.hasNext()) {
	    w.insert((IValue) iter.next());
	  }
		
	  return w.done();
	}

	public static IValue toString(IList lst)
	//@doc{toString -- convert a list to a string}
	{
		return values.string(lst.toString());
	}

	
	
	
	
	
}
