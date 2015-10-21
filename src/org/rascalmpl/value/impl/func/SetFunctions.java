/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse public static License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *
 * Based on code by:
 *
 *   * Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *******************************************************************************/
package org.rascalmpl.value.impl.func;

import java.util.Iterator;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashSet;
import org.rascalmpl.value.impl.util.collections.ShareableValuesList;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.util.RotatingQueue;
import org.rascalmpl.value.util.ShareableHashMap;
import org.rascalmpl.value.util.ValueIndexedHashMap;

public final class SetFunctions {

	private final static TypeFactory TF = TypeFactory.getInstance();

	public static boolean contains(IValueFactory vf, ISet set1, IValue e) {
		for (IValue v : set1) {
			if (v.isEqual(e)) {
				return true;
			}
		}
		return false;
	}

	public static ISet insert(IValueFactory vf, ISet set1, IValue e) {
		ISetWriter sw = vf.setWriter();
		sw.insertAll(set1);
		sw.insert(e);
		return sw.done();
	}
	
	public static ISet intersect(IValueFactory vf, ISet set1, ISet set2) {
		if (set1 == set2)
			return set1;
		
		ISetWriter w = vf.setWriter();
		
		for (IValue v : set1) {
			if (set2.contains(v)) {
				w.insert(v);
			}
		}
		
		return w.done();
	}
	
	public static ISet union(IValueFactory vf, ISet set1, ISet set2) {
		if (set1 == set2)
			return set1;
		
		ISetWriter w = vf.setWriter();
		w.insertAll(set1);
		w.insertAll(set2);
		return w.done();
	}
	
	public static ISet subtract(IValueFactory vf, ISet set1, ISet set2) {
		if (set1 == set2)
			return vf.set();
		
		ISetWriter sw = vf.setWriter();
		for (IValue a : set1) {
			if (!set2.contains(a)) {
				sw.insert(a);
			}
		}
		return sw.done();
	}

	public static ISet delete(IValueFactory vf, ISet set1, IValue v) {
		ISetWriter w = vf.setWriter();

		boolean deleted = false;
		for (Iterator<IValue> iterator = set1.iterator(); iterator.hasNext();) {
			IValue e = iterator.next();

			if (!deleted && e.isEqual(v)) {
				deleted = true; // skip first occurrence
			} else {
				w.insert(e);
			}
		}
		return w.done();
	}

	public static boolean isSubsetOf(IValueFactory vf, ISet set1, ISet set2) {
		for (IValue elem : set1) {
			if (!set2.contains(elem)) {
				return false;
			}
		}
		return true;
	}

	public static int hashCode(IValueFactory vf, ISet set1) {
		int hash = 0;

		Iterator<IValue> iterator = set1.iterator();
		while (iterator.hasNext()) {
			IValue element = iterator.next();
			hash ^= element.hashCode();
		}

		return hash;
	}

	public static boolean equals(IValueFactory vf, ISet set1, Object other) {
		if (other == set1)
			return true;
		if (other == null)
			return false;

		if (other instanceof ISet) {
			ISet set2 = (ISet) other;

			if (set1.getType() != set2.getType())
				return false;

			if (hashCode(vf, set1) != hashCode(vf, set2))
				return false;

			if (set1.size() == set2.size()) {

				for (IValue v1 : set1) {
					if (set2.contains(v1) == false)
						return false;
				}

				return true;
			}
		}

		return false;
	}

	/*
	 * NOTE: it's actually difficult to support isEqual semantics for sets if
	 * it is not supported by the underlying container.
	 */
	public static boolean isEqual(IValueFactory vf, ISet set1, IValue other) {
//		return equals(vf, set1, other);		
		if (other == set1)
			return true;
		if (other == null)
			return false;
			
		if (other instanceof ISet) {
			ISet set2 = (ISet) other;

			if (set1.size() == set2.size()) {

				for (IValue v1 : set1) {
					// function contains() calls isEqual() but used O(n) time
					if (contains(vf, set2, v1) == false)
						return false;
				}

				return true;
			}
		}

		return false;
	}

	public static ISet product(IValueFactory vf, ISet set1, ISet set2) {
		ISetWriter w = vf.setWriter();

		for (IValue t1 : set1) {
			for (IValue t2 : set2) {
				ITuple t3 = vf.tuple(t1, t2);
				w.insert(t3);
			}
		}

		return w.done();
	}

//	public static ISet compose(IValueFactory vf, ISet set1, ISet set2)
//			throws FactTypeUseException {
//		if (set1.getElementType() == TF.voidType())
//			return set1;
//		if (set2.getElementType() == TF.voidType())
//			return set2;
//
//		if (set1.getElementType().getArity() != 2
//				|| set2.getElementType().getArity() != 2) {
//			throw new IllegalOperationException(
//					"Incompatible types for composition.",
//					set1.getElementType(), set2.getElementType());
//		}
//
//		if (!set1.getElementType().getFieldType(1)
//				.comparable(set2.getElementType().getFieldType(0)))
//			return vf.set();
//		
//		ISetWriter w = vf.setWriter();
//
//		if (set1.getElementType().getFieldType(1)
//				.comparable(set2.getElementType().getFieldType(0))) {
//			for (IValue v1 : set1) {
//				ITuple tuple1 = (ITuple) v1;
//				for (IValue t2 : set2) {
//					ITuple tuple2 = (ITuple) t2;
//
//					if (tuple1.get(1).isEqual(tuple2.get(0))) {
//						w.insert(vf.tuple(tuple1.get(0), tuple2.get(1)));
//					}
//				}
//			}
//		}
//		return w.done();
//	}
	
	public static ISet compose(IValueFactory vf, ISet set1, ISet set2) throws FactTypeUseException {
		if (set1.getElementType() == TF.voidType())
			return set1;
		if (set2.getElementType() == TF.voidType())
			return set2;

		if (set1.getElementType().getArity() != 2
				|| set2.getElementType().getArity() != 2) {
			throw new IllegalOperationException(
					"Incompatible types for composition.",
					set1.getElementType(), set2.getElementType());
		}
		
		if (!set1.getElementType().getFieldType(1)
				.comparable(set2.getElementType().getFieldType(0)))
			return vf.set();
		
		// Index
		ShareableHashMap<IValue, ShareableValuesList> rightSides = new ShareableHashMap<>();
		
		Iterator<IValue> otherRelationIterator = set2.iterator();
		while(otherRelationIterator.hasNext()){
			ITuple tuple = (ITuple) otherRelationIterator.next();
			
			IValue key = tuple.get(0);
			ShareableValuesList values = rightSides.get(key);
			if(values == null){
				values = new ShareableValuesList();
				rightSides.put(key, values);
			}
			
			values.append(tuple.get(1));
		}
		
		// Compute		
		Type[] newTupleFieldTypes = new Type[]{set1.getElementType().getFieldType(0), set2.getElementType().getFieldType(1)};
		Type tupleType = TF.tupleType(newTupleFieldTypes);
		
		ISetWriter resultWriter = vf.setWriter(tupleType);
		
		Iterator<IValue> relationIterator = set1.iterator();
		while(relationIterator.hasNext()){
			ITuple thisTuple = (ITuple) relationIterator.next();
			
			IValue key = thisTuple.get(1);
			ShareableValuesList values = rightSides.get(key);
			if(values != null){
				Iterator<IValue> valuesIterator = values.iterator();
				do{
					IValue value = valuesIterator.next();
					IValue[] newTupleData = new IValue[]{thisTuple.get(0), value};
					resultWriter.insert(vf.tuple(tupleType, newTupleData));
				}while(valuesIterator.hasNext());
			}
		}
		
		return resultWriter.done();
	}	

	public static ISet carrier(IValueFactory vf, ISet set1) {
		ISetWriter w = vf.setWriter();

		for (IValue t : set1) {
			w.insertAll((ITuple) t);
		}

		return w.done();
	}

	private static ShareableValuesHashSet computeClosureDelta(IValueFactory vf, ISet rel1, Type tupleType) {
		RotatingQueue<IValue> iLeftKeys = new RotatingQueue<>();
		RotatingQueue<RotatingQueue<IValue>> iLefts = new RotatingQueue<>();
		
		ValueIndexedHashMap<RotatingQueue<IValue>> interestingLeftSides = new ValueIndexedHashMap<>();
		ValueIndexedHashMap<ShareableValuesHashSet> potentialRightSides = new ValueIndexedHashMap<>();
		
		// Index
		Iterator<IValue> allDataIterator = rel1.iterator();
		while(allDataIterator.hasNext()){
			ITuple tuple = (ITuple) allDataIterator.next();

			IValue key = tuple.get(0);
			IValue value = tuple.get(1);
			RotatingQueue<IValue> leftValues = interestingLeftSides.get(key);
			ShareableValuesHashSet rightValues;
			if(leftValues != null){
				rightValues = potentialRightSides.get(key);
			}else{
				leftValues = new RotatingQueue<>();
				iLeftKeys.put(key);
				iLefts.put(leftValues);
				interestingLeftSides.put(key, leftValues);
				
				rightValues = new ShareableValuesHashSet();
				potentialRightSides.put(key, rightValues);
			}
			leftValues.put(value);
			rightValues.add(value);
		}
		
		int size = potentialRightSides.size();
		int nextSize = 0;
		
		// Compute
		final ShareableValuesHashSet newTuples = new ShareableValuesHashSet();
		do{
			ValueIndexedHashMap<ShareableValuesHashSet> rightSides = potentialRightSides;
			potentialRightSides = new ValueIndexedHashMap<>();
			
			for(; size > 0; size--){
				IValue leftKey = iLeftKeys.get();
				RotatingQueue<IValue> leftValues = iLefts.get();
				
				RotatingQueue<IValue> interestingLeftValues = null;
				
				IValue rightKey;
				while((rightKey = leftValues.get()) != null){
					ShareableValuesHashSet rightValues = rightSides.get(rightKey);
					if(rightValues != null){
						Iterator<IValue> rightValuesIterator = rightValues.iterator();
						while(rightValuesIterator.hasNext()){
							IValue rightValue = rightValuesIterator.next();
							if(newTuples.add(vf.tuple(tupleType, leftKey, rightValue))){
								if(interestingLeftValues == null){
									nextSize++;
									
									iLeftKeys.put(leftKey);
									interestingLeftValues = new RotatingQueue<>();
									iLefts.put(interestingLeftValues);
								}
								interestingLeftValues.put(rightValue);
								
								ShareableValuesHashSet potentialRightValues = potentialRightSides.get(rightKey);
								if(potentialRightValues == null){
									potentialRightValues = new ShareableValuesHashSet();
									potentialRightSides.put(rightKey, potentialRightValues);
								}
								potentialRightValues.add(rightValue);
							}
						}
					}
				}
			}
			size = nextSize;
			nextSize = 0;
		}while(size > 0);
		
		return newTuples;
	}
	
//	public static ISet closure(IValueFactory vf, ISet set1)
//			throws FactTypeUseException {
//		// will throw exception if not binary and reflexive
//		set1.getType().closure();
//
//		ISet tmp = set1;
//
//		int prevCount = 0;
//
//		while (prevCount != tmp.size()) {
//			prevCount = tmp.size();
//			tmp = tmp.union(compose(vf, tmp, tmp));
//		}
//
//		return tmp;
//	}
	
	public static ISet closure(IValueFactory vf, ISet rel1) {
		if (rel1.getElementType() == TF.voidType())
			return rel1;
		if (!isBinary(rel1))
			throw new IllegalOperationException("closure", rel1.getType());

		Type tupleElementType = rel1.getElementType().getFieldType(0).lub(rel1.getElementType().getFieldType(1));
		Type tupleType = TF.tupleType(tupleElementType, tupleElementType);

		java.util.Set<IValue> closureDelta = computeClosureDelta(vf, rel1, tupleType);

		// NOTE: type is already known, thus, using a SetWriter degrades performance
		ISetWriter resultWriter = vf.setWriter(tupleType);
		resultWriter.insertAll(rel1);
		resultWriter.insertAll(closureDelta);

		return resultWriter.done();
	}

//	public static ISet closureStar(IValueFactory vf, ISet set1)
//			throws FactTypeUseException {
//		set1.getType().closure();
//		// an exception will have been thrown if the type is not acceptable
//
//		ISetWriter reflex = vf.setWriter();
//
//		for (IValue e : carrier(vf, set1)) {
//			reflex.insert(vf.tuple(new IValue[] { e, e }));
//		}
//
//		return closure(vf, set1).union(reflex.done());
//	}
	
	// TODO: Currently untested in PDB.
	public static ISet closureStar(IValueFactory vf, ISet rel1) {
		if (rel1.getElementType() == TF.voidType())
			return rel1;
		if (!isBinary(rel1))
			throw new IllegalOperationException("closureStar", rel1.getType());

		Type tupleElementType = rel1.getElementType().getFieldType(0).lub(rel1.getElementType().getFieldType(1));
		Type tupleType = TF.tupleType(tupleElementType, tupleElementType);

		// calculate
		ShareableValuesHashSet closureDelta = computeClosureDelta(vf, rel1, tupleType);
		ISet carrier = carrier(vf, rel1);

		// aggregate result
		// NOTE: type is already known, thus, using a SetWriter degrades performance
		ISetWriter resultWriter = vf.setWriter(rel1.getElementType());
		resultWriter.insertAll(rel1);
		resultWriter.insertAll(closureDelta);
		
		Iterator<IValue> carrierIterator = carrier.iterator();
		while (carrierIterator.hasNext()) {
			IValue element = carrierIterator.next();
			resultWriter.insert(vf.tuple(tupleType, element, element));
		}

		return resultWriter.done();
	}
	
	private static boolean isBinary(ISet rel1){
		return rel1.getElementType().getArity() == 2;
	}
	
	public static ISet domain(IValueFactory vf, ISet set1) {
		int columnIndex = 0;
		ISetWriter w = vf.setWriter();

		for (IValue elem : set1) {
			ITuple tuple = (ITuple) elem;
			w.insert(tuple.get(columnIndex));
		}
		return w.done();
	}

	public static ISet range(IValueFactory vf, ISet set1) {
		int columnIndex = set1.getType().getArity() - 1;
		ISetWriter w = vf.setWriter();

		for (IValue elem : set1) {
			ITuple tuple = (ITuple) elem;
			w.insert(tuple.get(columnIndex));
		}

		return w.done();
	}

	public static ISet project(IValueFactory vf, ISet set1, int... fields) {
		ISetWriter w = vf.setWriter();

		for (IValue v : set1) {
			w.insert(((ITuple) v).select(fields));
		}

		return w.done();
	}

	public static ISet projectByFieldNames(IValueFactory vf, ISet set1,
			String... fields) {
		int[] indexes = new int[fields.length];
		int i = 0;

		if (set1.getType().getFieldTypes().hasFieldNames()) {
			for (String field : fields) {
				indexes[i++] = set1.getType().getFieldTypes()
						.getFieldIndex(field);
			}

			return project(vf, set1, indexes);
		}

		throw new IllegalOperationException("select with field names",
				set1.getType());
	}

}
