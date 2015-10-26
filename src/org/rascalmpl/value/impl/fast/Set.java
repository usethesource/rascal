/*******************************************************************************
* Copyright (c) 2009-2013 Centrum Wiskunde en Informatica (CWI)
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
import org.rascalmpl.value.ISetRelation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.AbstractValue;
import org.rascalmpl.value.impl.func.SetFunctions;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashSet;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

/**
 * Implementation of ISet.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class Set extends AbstractValue implements ISet {
	protected final static TypeFactory typeFactory = TypeFactory.getInstance();
	protected final static Type voidType = typeFactory.voidType();
	
	protected final Type setType;
	protected final Type elementType;
	
	protected final ShareableValuesHashSet data;
	
	/*package*/ static ISet newSet(Type elementType, ShareableValuesHashSet data) {
		return new Set(elementType, data);
	}
		
	private Set(Type elementType, ShareableValuesHashSet data) {
		super();

		if (data.isEmpty())
			this.elementType = voidType;
		else
			this.elementType = elementType;
		
		this.setType = typeFactory.setType(this.elementType);
				
		this.data = data;
	}
	
	public Type getType(){
		return setType;
	}
	
	public Type getElementType(){
		return elementType;
	}

	public int size(){
		return data.size();
	}
	
	public boolean isEmpty(){
		return data.isEmpty();
	}
	
	public Iterator<IValue> iterator(){
		return data.iterator();
	}
	
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E{
		if (getElementType().isFixedWidth()) {
			return v.visitRelation(this);
		} else {
			return v.visitSet(this);
		}
	}
	
	public boolean contains(IValue element){
		return data.contains(element);
	}
	
	public boolean isSubsetOf(ISet other){
		Set otherSet = (Set) other;
		
		Iterator<IValue> iterator = iterator();
		while(iterator.hasNext()){
			if(!otherSet.data.contains(iterator.next())) return false;
		}
		
		return true;
	}
	
	public ISet insert(IValue value){
		if(!contains(value)) {
			ShareableValuesHashSet newData = new ShareableValuesHashSet(data);
			newData.add(value);
			
			Type type = elementType.lub(value.getType());
			return new SetWriter(type, newData).done();
		} else {
			return this;
		}
	}

	public ISet delete(IValue value){
		if (contains(value)) {
			ShareableValuesHashSet newData = new ShareableValuesHashSet(data);
			newData.remove(value);
			
			Type newElementType = TypeFactory.getInstance().voidType();
			for (IValue el : newData) {
				newElementType = newElementType.lub(el.getType());
			}
			return new SetWriter(newElementType, newData).done();
		} else {
			return this;
		}
	}
	
	public ISet intersect(ISet other){
		ShareableValuesHashSet commonData = new ShareableValuesHashSet();
		Iterator<IValue> setIterator;
		
		ISet theOtherSet;
		
		if(other.size() <= size()){
			setIterator = other.iterator();
			theOtherSet = this;
		}else{
			setIterator = iterator();
			theOtherSet = other;
		}
		
		Type newElementType = TypeFactory.getInstance().voidType();
		while(setIterator.hasNext()){
			IValue value = setIterator.next();
			if(theOtherSet.contains(value)){
				newElementType = newElementType.lub(value.getType());
				commonData.add(value);
			}
		}
		
		return new SetWriter(newElementType, commonData).done();
	}
	
	public ISet subtract(ISet other){
		ShareableValuesHashSet newData = new ShareableValuesHashSet(data);
		
		Iterator<IValue> setIterator = other.iterator();
		while(setIterator.hasNext()){
			newData.remove(setIterator.next());
		}
		Type newElementType = TypeFactory.getInstance().voidType();
		for(IValue el : newData)
			newElementType = newElementType.lub(el.getType());
		return new SetWriter(newElementType, newData).done();
	}
	
	public ISet union(ISet other){
		ShareableValuesHashSet newData;
		Iterator<IValue> setIterator;
		
		Set otherSet = (Set) other;
		
		if(otherSet.size() <= size()){
			newData = new ShareableValuesHashSet(data);
			setIterator = otherSet.iterator();
		}else{
			newData = new ShareableValuesHashSet(otherSet.data);
			setIterator = iterator();
		}
		
		while(setIterator.hasNext()){
			newData.add(setIterator.next());
		}
		
		Type newElementType = elementType.lub(otherSet.elementType);
		return new SetWriter(newElementType, newData).done();
	}
	
	public ISet product(ISet other){
		ShareableValuesHashSet newData = new ShareableValuesHashSet();
		
		Type tupleType = typeFactory.tupleType(elementType, other.getElementType());

		Iterator<IValue> thisIterator = data.iterator();
		while(thisIterator.hasNext()){
			IValue left = thisIterator.next();
			
			Iterator<IValue> setIterator = other.iterator();
			while(setIterator.hasNext()){
				IValue right = setIterator.next();
				
				IValue[] tuple = new IValue[]{left, right};
				newData.add(Tuple.newTuple(tupleType, tuple));
			}
		}
		
		return new SetWriter(tupleType, newData).done();
	}
	
	public int hashCode(){
		return data.hashCode();
	}
	
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			Set otherSet = (Set) o;
			
			if (getType() != otherSet.getType()) {
			  return false;
			}
			return data.equals(otherSet.data);
		}
		
		return false;
	}
	
	public boolean isEqual(IValue value){
		if(value == this) return true;
		if(value == null) return false;
		
		if(value instanceof Set){
			Set otherSet = (Set) value;
			
			return data.isEqual(otherSet.data);
		}
		else if (value instanceof ISet) {
			return SetFunctions.isEqual(ValueFactory.getInstance(), this, (ISet) value);
		}
		
		return false;
	}

	@Override
	public boolean isRelation() {
		return getType().isRelation();
	}

	@Override
	public ISetRelation<ISet> asRelation() {
		if (!isRelation())
			throw new IllegalOperationException(
					"Cannot be viewed as a relation.", getType());

		return new RelationViewOnSet(this);
	}
	
}
