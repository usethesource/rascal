/*******************************************************************************
* Copyright (c) 2009, 2012 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*    Anya Helene Bagge - labels
*******************************************************************************/
package org.rascalmpl.value.impl.fast;

import java.util.Iterator;
import java.util.Map.Entry;

import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.impl.AbstractValue;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashMap;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

/**
 * Implementation of IMap.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class Map extends AbstractValue implements IMap {
	protected final static TypeFactory typeFactory = TypeFactory.getInstance();
	
	protected final Type mapType;
	
	protected final ShareableValuesHashMap data;
	
	/*package*/ static IMap newMap(Type mapType, ShareableValuesHashMap data) {
		return new Map(mapType, data);
	}
	
	private Map(Type mapType, ShareableValuesHashMap data) {
		super();
		
		this.mapType = mapType;
		
		this.data = data;
	}

	public Type getType(){
		return mapType;
	}
	
	public Type getKeyType(){
		return mapType.getKeyType();
	}
	
	public Type getValueType(){
		return mapType.getValueType();
	}
	
	public int size(){
		return data.size();
	}
	
	public int arity(){
		return size();
	}
	
	public boolean isEmpty(){
		return data.isEmpty();
	}
	
	public IValue get(IValue key){
		return data.get(key);
	}
	
	public Iterator<IValue> iterator(){
		return data.keysIterator();
	}
	
	public Iterator<Entry<IValue, IValue>> entryIterator(){
		return data.entryIterator();
	}
	
	public Iterator<IValue> valueIterator(){
		return data.valuesIterator();
	}

	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E{
		return v.visitMap(this);
	}
	
	public boolean containsKey(IValue key){
		return data.contains(key);
	}

	public boolean containsValue(IValue value){
		Iterator<IValue> valuesIterator = data.valuesIterator();
		while(valuesIterator.hasNext()){
			if(valuesIterator.next().isEqual(value)) return true;
		}
		
		return false;
	}
	
	public boolean isSubMap(IMap other) {
		Map otherMap = (Map) other;

		Iterator<IValue> keysIterator = iterator();
		while (keysIterator.hasNext()) {
			IValue key = keysIterator.next();
			if (!otherMap.data.contains(key)) {
				return false;
			}
			if (!otherMap.data.get(key).isEqual(data.get(key))) {
				return false;
			}
		}

		return true;
	}
	
	public IMap put(IValue key, IValue value){
		ShareableValuesHashMap newData = new ShareableValuesHashMap(data);
		IValue replaced = newData.put(key, value);
		
		if (replaced != null) {
			/*
			 * we might have to narrow dynamic type of value range
			 */
			Type voidType = TypeFactory.getInstance().voidType();
			
			Type newMapType = mapType;
			Type newKeyType = voidType;
			Type newValueType = voidType;
			
			for (Iterator<Entry<IValue, IValue>> it = newData.entryIterator(); it.hasNext(); ) {
				final Entry<IValue, IValue> currentEntry = it.next();
				
				newKeyType = newKeyType.lub(currentEntry.getKey().getType());
				newValueType = newValueType.lub(currentEntry.getValue().getType());
				
				if(newKeyType != mapType.getKeyType() || newValueType != mapType.getValueType()) {
					 newMapType = TypeFactory.getInstance().mapType(newKeyType, mapType.getKeyLabel(), newValueType, mapType.getValueLabel());
				}			
			}

			return new MapWriter(newMapType, newData).done();
		} else {			
			Type newMapType = mapType;
			Type newKeyType = mapType.getKeyType().lub(key.getType());
			Type newValueType = mapType.getValueType().lub(value.getType());
			
			if(newKeyType != mapType.getKeyType() || newValueType != mapType.getValueType()) {
				 newMapType = TypeFactory.getInstance().mapType(newKeyType, mapType.getKeyLabel(), newValueType, mapType.getValueLabel());
			}
	
			return new MapWriter(newMapType, newData).done();
		}
	}
	
	public IMap common(IMap other){
		ShareableValuesHashMap commonData = new ShareableValuesHashMap();
		Iterator<Entry<IValue, IValue>> entryIterator;
		
		IMap theOtherMap;
		
		if(other.size() <= size()){
			entryIterator = other.entryIterator();
			theOtherMap = this;
		}else{
			entryIterator = entryIterator();
			theOtherMap = other;
		}
		
		Type newKeyType = TypeFactory.getInstance().voidType();
		Type newValueType = TypeFactory.getInstance().voidType();
		while(entryIterator.hasNext()){
			Entry<IValue, IValue> entry = entryIterator.next();
			IValue key = entry.getKey();
			IValue value = entry.getValue();
			if(value.isEqual(theOtherMap.get(key))){
				newKeyType = newKeyType.lub(key.getType());
				newValueType = newValueType.lub(value.getType());
				commonData.put(key, value);
			}
		}
		Type lub = mapType.lub(other.getType());
		return new MapWriter(TypeFactory.getInstance().mapType(newKeyType, lub.getKeyLabel(), newValueType, lub.getValueLabel()), commonData).done();
	}
	
	public IMap compose(IMap other){
		ShareableValuesHashMap newData = new ShareableValuesHashMap();
		
		Map otherMap = (Map) other;
		
		Iterator<Entry<IValue, IValue>> entryIterator = entryIterator();
		while(entryIterator.hasNext()){
			Entry<IValue,IValue> entry = entryIterator.next();
			IValue value = otherMap.get(entry.getValue());
			if(value != null){
				newData.put(entry.getKey(), value);
			}
		}
		
		Type newMapType;
		if(mapType.hasFieldNames() && otherMap.mapType.hasFieldNames()) {
			newMapType = TypeFactory.getInstance().mapType(mapType.getKeyType(), mapType.getKeyLabel(), 
				otherMap.mapType.getValueType(), otherMap.mapType.getValueLabel());
		}
		else {
			newMapType = TypeFactory.getInstance().mapType(mapType.getKeyType(), otherMap.mapType.getValueType());
		}
		return new MapWriter(newMapType, newData).done();
	}
	
	public IMap join(IMap other){
		ShareableValuesHashMap newData;
		Iterator<Entry<IValue, IValue>> entryIterator;
		
		Map otherMap = (Map) other;
		newData = new ShareableValuesHashMap(data);
		entryIterator = otherMap.entryIterator();
		
		while(entryIterator.hasNext()){
			Entry<IValue, IValue> entry = entryIterator.next();
			newData.put(entry.getKey(), entry.getValue());
		}
		
		return new MapWriter(mapType.lub(otherMap.mapType), newData).done();
	}
	
	public IMap remove(IMap other){
		ShareableValuesHashMap newData = new ShareableValuesHashMap(data);
		
		Iterator<IValue> keysIterator = other.iterator();
		while(keysIterator.hasNext()){
			newData.remove(keysIterator.next());
		}
		
		Type newKeyType = TypeFactory.getInstance().voidType();
		Type newValueType = TypeFactory.getInstance().voidType();
		Iterator<Entry<IValue, IValue>> entryIterator = newData.entryIterator();
		while(entryIterator.hasNext()) {
			Entry<IValue, IValue> el = entryIterator.next();
			newKeyType = newKeyType.lub(el.getKey().getType());
			newValueType = newValueType.lub(el.getValue().getType());
		}
			
		return new MapWriter(TypeFactory.getInstance().mapType(newKeyType, mapType.getKeyLabel(), newValueType, mapType.getValueLabel()), newData).done();
	}
	
	public int hashCode(){
		return data.hashCode();
	}
	
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			Map otherMap = (Map) o;
			
			if (getType() != otherMap.getType()) return false;
			
			return data.equals(otherMap.data);
		}
		
		return false;
	}
	
	public boolean isEqual(IValue value){
		if(value == this) return true;
		if(value == null) return false;
		
		if(value instanceof Map){
			Map otherMap = (Map) value;
			
			return data.isEqual(otherMap.data);
		}
		
		return false;
	}

	@Override
	public IMap removeKey(IValue key) {
		ShareableValuesHashMap newData = new ShareableValuesHashMap(data);
		IValue replaced = newData.remove(key);

		if (replaced != null) {
			/*
			 * we might have to narrow dynamic type of value range
			 */
			Type voidType = TypeFactory.getInstance().voidType();

			Type newMapType = mapType;
			Type newKeyType = voidType;
			Type newValueType = voidType;

			for (Iterator<Entry<IValue, IValue>> it = newData.entryIterator(); it.hasNext();) {
				final Entry<IValue, IValue> currentEntry = it.next();

				newKeyType = newKeyType.lub(currentEntry.getKey().getType());
				newValueType = newValueType.lub(currentEntry.getValue().getType());

				if (newKeyType != mapType.getKeyType() || newValueType != mapType.getValueType()) {
					newMapType = TypeFactory.getInstance().mapType(newKeyType,
									mapType.getKeyLabel(), newValueType, mapType.getValueLabel());
				}
			}

			return new MapWriter(newMapType, newData).done();
		} else {
			return this;
		}
	}
	
}
