/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*    Michael Steindorfer - performance improvements
*******************************************************************************/
package org.rascalmpl.value.impl.fast;

import java.util.HashSet;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashSet;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

public class RelationalFunctionsOnList {
		
	protected final static TypeFactory typeFactory = TypeFactory.getInstance();
	protected final static Type voidType = typeFactory.voidType();	
	
	public static int arity(IList rel) {
		return rel.getElementType().getArity();
	}
	
	public static IList carrier(IList rel1) {
		Type newType = rel1.getType().carrier();
		IListWriter w = List.createListWriter(newType.getElementType());
		HashSet<IValue> cache = new HashSet<>();
		
		for (IValue v : rel1) {
			ITuple t = (ITuple) v;
			for(IValue e : t){
				if(!cache.contains(e)){
					cache.add(e);
					w.append(e);
				}
			}
		}
		
		return w.done();
	}
	
	public static IList domain(IList rel1) {
		Type lrelType = rel1.getType();
		IListWriter w = List.createListWriter(lrelType.getFieldType(0));
		HashSet<IValue> cache = new HashSet<>();
		
		for (IValue elem : rel1) {
			ITuple tuple = (ITuple) elem;
			IValue e = tuple.get(0);
			if(!cache.contains(e)){
				cache.add(e);
				w.append(e);
			}
		}
		return w.done();
	}
	
	public static IList range(IList rel1) {
		Type lrelType = rel1.getType();
		int last = lrelType.getArity() - 1;
		IListWriter w = List.createListWriter(lrelType.getFieldType(last));
		HashSet<IValue> cache = new HashSet<>();
		
		for (IValue elem : rel1) {
			ITuple tuple = (ITuple) elem;
			IValue e = tuple.get(last);
			if(!cache.contains(e)){
				cache.add(e);
				w.append(e);
			}
		}
		
		return w.done();
	}
		
	public static IList compose(IList rel1, IList rel2) {
		
		Type otherTupleType = rel2.getType().getFieldTypes();
		
		if(rel1.getElementType() == voidType) return rel1;
		if(otherTupleType == voidType) return rel2;
		
		if(rel1.getElementType().getArity() != 2 || otherTupleType.getArity() != 2) throw new IllegalOperationException("compose", rel1.getElementType(), otherTupleType);
		
		// Relaxed type constraint:
	    if(!rel1.getElementType().getFieldType(1).comparable(otherTupleType.getFieldType(0))) throw new IllegalOperationException("compose", rel1.getElementType(), otherTupleType);

		Type[] newTupleFieldTypes = new Type[]{rel1.getElementType().getFieldType(0), otherTupleType.getFieldType(1)};
		Type tupleType = typeFactory.tupleType(newTupleFieldTypes);

		IListWriter w = new ListWriter(tupleType);

		for (IValue v1 : rel1) {
			ITuple tuple1 = (ITuple) v1;
			for (IValue t2 : rel2) {
				ITuple tuple2 = (ITuple) t2;
				
				if (tuple1.get(1).isEqual(tuple2.get(0))) {
						w.append(Tuple.newTuple(tuple1.get(0), tuple2.get(1)));
				}
			}
		}
		return w.done();
	}
	
	public static IList closure(IList rel1) {
		Type resultType = rel1.getType().closure(); // will throw exception if not binary and reflexive
		IList tmp = rel1;

		int prevCount = 0;

		ShareableValuesHashSet addedTuples = new ShareableValuesHashSet();
		while (prevCount != tmp.length()) {
			prevCount = tmp.length();
			IList tcomp = compose(tmp, tmp);
			IListWriter w = List.createListWriter(resultType.getElementType());
			for(IValue t1 : tcomp){
				if(!tmp.contains(t1)){
					if(!addedTuples.contains(t1)){
						addedTuples.add(t1);
						w.append(t1);
					}
				}
			}
			tmp = tmp.concat(w.done());
			addedTuples.clear();
		}
		return tmp;
	}
	
	public static IList closureStar(IList rel1) {
		Type resultType = rel1.getType().closure();
		// an exception will have been thrown if the type is not acceptable

		IListWriter reflex = List.createListWriter(resultType.getElementType());

		for (IValue e: carrier(rel1)) {
			reflex.insert(Tuple.newTuple(new IValue[] {e, e}));
		}
		
		return closure(rel1).concat(reflex.done());
	}
	
	public static IList project(IList rel1, int... fields) {
		IListWriter w = ValueFactory.getInstance().listWriter();
		
		for (IValue v : rel1) {
			w.append(((ITuple) v).select(fields));
		}
		
		return w.done();
	}
	
	public static IList projectByFieldNames(IList rel1, String... fields) {
		int[] indexes = new int[fields.length];
		int i = 0;
		
		if (rel1.getType().getFieldTypes().hasFieldNames()) {
			for (String field : fields) {
				indexes[i++] = rel1.getType().getFieldTypes().getFieldIndex(field);
			}
			
			return project(rel1, indexes);
		}
		
		throw new IllegalOperationException("select with field names", rel1.getType());
	}
		
}
