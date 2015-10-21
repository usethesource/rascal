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

import java.util.Iterator;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashSet;
import org.rascalmpl.value.impl.util.collections.ShareableValuesList;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.util.RotatingQueue;
import org.rascalmpl.value.util.ShareableHashMap;
import org.rascalmpl.value.util.ValueIndexedHashMap;

public class RelationalFunctionsOnSet {
		
	protected final static TypeFactory typeFactory = TypeFactory.getInstance();
	protected final static Type voidType = typeFactory.voidType();	
	
	public static int arity(ISet rel) {
		return rel.getElementType().getArity();
	}

//	// TODO: Currently untested in PDB.
//	public static ISet union(ISet rel1, ISet rel2){
//		ShareableValuesHashSet newData;
//		Iterator<IValue> setIterator;
//		
//		Set thisSet  = (Set) rel1;
//		Set otherSet = (Set) rel2;
//		
//		if(otherSet.size() <= rel1.size()){
//			newData = new ShareableValuesHashSet(thisSet.data);
//			setIterator = otherSet.iterator();
//		}else{
//			newData = new ShareableValuesHashSet(otherSet.data);
//			setIterator = rel1.iterator();
//		}
//		
//		while(setIterator.hasNext()){
//			newData.add(setIterator.next());
//		}
//		
//		Type newElementType = thisSet.elementType.lub(otherSet.elementType);
//		return new SetWriter(newElementType, newData).done();
//	}	

//	// TODO: Currently untested in PDB.
//	public static ISet intersect(ISet rel1, ISet rel2){
//		ShareableValuesHashSet commonData = new ShareableValuesHashSet();
//		Iterator<IValue> setIterator;
//		
//		ISet theOtherSet;
//		
//		if(rel2.size() <= rel1.size()){
//			setIterator = rel2.iterator();
//			theOtherSet = rel1;
//		}else{
//			setIterator = rel1.iterator();
//			theOtherSet = rel2;
//		}
//		
//		Type newElementType = TypeFactory.getInstance().voidType();
//		while(setIterator.hasNext()){
//			IValue value = setIterator.next();
//			if(theOtherSet.contains(value)){
//				newElementType = newElementType.lub(value.getType());
//				commonData.add(value);
//			}
//		}
//		
//		return new SetWriter(newElementType, commonData).done();
//	}	

//	// TODO: Currently untested in PDB.
//	public static ISet subtract(ISet rel1, ISet rel2){
//		ShareableValuesHashSet newData = new ShareableValuesHashSet(((Set)rel1).data);
//		
//		Iterator<IValue> setIterator = rel2.iterator();
//		while(setIterator.hasNext()){
//			newData.remove(setIterator.next());
//		}
//		
//		Type newElementType = TypeFactory.getInstance().voidType();
//		for(IValue el : newData)
//			newElementType = newElementType.lub(el.getType());
//		
//		return new SetWriter(newElementType, newData).done();
//	}
	
	private static ShareableValuesHashSet computeCarrier(ISet rel1) {
		ShareableValuesHashSet newData = new ShareableValuesHashSet();
		
		Iterator<IValue> relationIterator = ((Set)rel1).data.iterator();
		while(relationIterator.hasNext()){
			ITuple tuple = (ITuple) relationIterator.next();
			
			Iterator<IValue> tupleIterator = tuple.iterator();
			while(tupleIterator.hasNext()){
				newData.add(tupleIterator.next());
			}
		}
		
		return newData;
	}
	
	public static ISet carrier(ISet rel1) {
		ShareableValuesHashSet newData = computeCarrier(rel1);
		
		Type type = determainMostGenericTypeInTuple(rel1);
		return new SetWriter(type, newData).done();
	}

	// TODO: Currently untested in PDB.
	public static ISet domain(ISet rel1){
		ShareableValuesHashSet newData = new ShareableValuesHashSet();
		
		Iterator<IValue> relationIterator = ((Set)rel1).data.iterator();
		while(relationIterator.hasNext()){
			ITuple tuple = (ITuple) relationIterator.next();
			
			newData.add(tuple.get(0));
		}
		
		Type type = rel1.getElementType().getFieldType(0);
		return new SetWriter(type, newData).done();
	}
	
	// TODO: Currently untested in PDB.
	public static ISet range(ISet rel1){
		ShareableValuesHashSet newData = new ShareableValuesHashSet();
		
		int last = rel1.getElementType().getArity() - 1;
		
		Iterator<IValue> relationIterator = ((Set)rel1).data.iterator();
		while(relationIterator.hasNext()){
			ITuple tuple = (ITuple) relationIterator.next();
			
			newData.add(tuple.get(last));
		}
		
		Type type = rel1.getElementType().getFieldType(last);
		return new SetWriter(type, newData).done();
	}
	
	public static ISet compose(ISet rel1, ISet rel2){
		Type otherTupleType = rel2.getElementType();
		
		if (rel1.getElementType() == voidType) {
		  return rel1;
		}
		if(otherTupleType == voidType) {
		  return rel2;
		}
		
		if(rel1.getElementType().getArity() != 2 || otherTupleType.getArity() != 2) {
		  throw new IllegalOperationException("compose", rel1.getElementType(), otherTupleType);
		}
		if(!rel1.getElementType().getFieldType(1).comparable(otherTupleType.getFieldType(0))) {
		  return new SetWriter().done();
		}
		
		// Index
		ShareableHashMap<IValue, ShareableValuesList> rightSides = new ShareableHashMap<>();
		
		Iterator<IValue> otherRelationIterator = rel2.iterator();
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
		ShareableValuesHashSet newData = new ShareableValuesHashSet();
		
		Type[] newTupleFieldTypes = new Type[]{rel1.getElementType().getFieldType(0), otherTupleType.getFieldType(1)};
		Type tupleType = typeFactory.tupleType(newTupleFieldTypes);
		
		Iterator<IValue> relationIterator = ((Set)rel1).data.iterator();
		while(relationIterator.hasNext()){
			ITuple thisTuple = (ITuple) relationIterator.next();
			
			IValue key = thisTuple.get(1);
			ShareableValuesList values = rightSides.get(key);
			if(values != null){
				Iterator<IValue> valuesIterator = values.iterator();
				do{
					IValue value = valuesIterator.next();
					IValue[] newTupleData = new IValue[]{thisTuple.get(0), value};
					newData.add(Tuple.newTuple(tupleType, newTupleData));
				}while(valuesIterator.hasNext());
			}
		}
		
		return new SetWriter(tupleType, newData).done();
	}
	
	private static ShareableValuesHashSet computeClosure(ISet rel1, Type tupleType){
		ShareableValuesHashSet allData = new ShareableValuesHashSet(((Set)rel1).data);
		
		RotatingQueue<IValue> iLeftKeys = new RotatingQueue<>();
		RotatingQueue<RotatingQueue<IValue>> iLefts = new RotatingQueue<>();
		
		ValueIndexedHashMap<RotatingQueue<IValue>> interestingLeftSides = new ValueIndexedHashMap<>();
		ValueIndexedHashMap<ShareableValuesHashSet> potentialRightSides = new ValueIndexedHashMap<>();
		
		// Index
		Iterator<IValue> allDataIterator = allData.iterator();
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
							if(allData.add(Tuple.newTuple(tupleType, new IValue[]{leftKey, rightValue}))){
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
		
		return allData;
	}
	
	public static ISet closure(ISet rel1) {
		if(rel1.getElementType() == voidType) return rel1;
		if(!isBinary(rel1)) {
			throw new IllegalOperationException("closure", rel1.getType());
		}
		
		Type tupleElementType = rel1.getElementType().getFieldType(0).lub(rel1.getElementType().getFieldType(1));
		Type tupleType = typeFactory.tupleType(tupleElementType, tupleElementType);
		
		return new SetWriter(rel1.getElementType(), computeClosure(rel1, tupleType)).done();
	}

	// TODO: Currently untested in PDB.
	public static ISet closureStar(ISet rel1) {
		if (rel1.getElementType() == voidType) {
		  return rel1;
		}
		if (!isBinary(rel1)) {
		  throw new IllegalOperationException("closureStar", rel1.getType());
		}
		
		Type tupleElementType = rel1.getElementType().getFieldType(0).lub(rel1.getElementType().getFieldType(1));
		Type tupleType = typeFactory.tupleType(tupleElementType, tupleElementType);
		
		ShareableValuesHashSet closure = computeClosure(rel1, tupleType);
		ShareableValuesHashSet carrier = computeCarrier(rel1);
		
		Iterator<IValue> carrierIterator = carrier.iterator();
		while(carrierIterator.hasNext()){
			IValue element = carrierIterator.next();
			closure.add(Tuple.newTuple(tupleType, new IValue[]{element, element}));
		}
		
		return new SetWriter(rel1.getElementType(), closure).done();
	}
	
	// TODO: Currently untested in PDB.
	public static ISet project(ISet rel1, int... indexes){
		ShareableValuesHashSet newData = new ShareableValuesHashSet();
		
		Iterator<IValue> dataIterator = ((Set)rel1).data.iterator();
		while(dataIterator.hasNext()){
			ITuple tuple = (ITuple) dataIterator.next();
			
			newData.add(tuple.select(indexes));
		}
		
		Type type = rel1.getElementType().select(indexes);
		return new SetWriter(type, newData).done();
	}
	
	// TODO: Currently untested in PDB.
	public static ISet projectByFieldNames(ISet rel1, String... fields){
		if(!rel1.getElementType().hasFieldNames()) throw new IllegalOperationException("select with field names", rel1.getType());
		
		ShareableValuesHashSet newData = new ShareableValuesHashSet();
		
		Iterator<IValue> dataIterator = ((Set)rel1).data.iterator();
		while(dataIterator.hasNext()){
			ITuple tuple = (ITuple) dataIterator.next();
			
			newData.add(tuple.selectByFieldNames(fields));
		}
		
		Type type = rel1.getElementType().select(fields);
		return new SetWriter(type, newData).done();
	}
		
	private static Type determainMostGenericTypeInTuple(ISet rel1){
		Type result = rel1.getElementType().getFieldType(0);
		for(int i = rel1.getElementType().getArity() - 1; i > 0; i--){
			result = result.lub(rel1.getElementType().getFieldType(i));
		}
		
		return result;
	}
	
	private static boolean isBinary(ISet rel1){
		return rel1.getElementType().getArity() == 2;
	}
}
