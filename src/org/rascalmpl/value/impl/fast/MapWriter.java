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
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashMap;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

// TODO Add checking.
/**
 * Implementation of IMapWriter.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class MapWriter implements IMapWriter{
	protected Type keyType;
	protected Type valueType;
	protected Type mapType;
	
	protected final ShareableValuesHashMap data;
	
	protected IMap constructedMap;
	protected final boolean inferred;
	protected boolean inferredTypeinvalidated;
	
	/*package*/ MapWriter(){
		super();
		
		this.mapType = null;
		this.keyType = TypeFactory.getInstance().voidType();
		this.valueType =  TypeFactory.getInstance().voidType();
		this.inferred = true;
		
		data = new ShareableValuesHashMap();
		
		constructedMap = null;
	}
	
	
	/*package*/ MapWriter(Type mapType) {
		super();
		
		if(mapType.isFixedWidth() && mapType.getArity() >= 2) {
			mapType = TypeFactory.getInstance().mapTypeFromTuple(mapType);
		}
		
		this.mapType = mapType;
		this.keyType = mapType.getKeyType();
		this.valueType = mapType.getValueType();
		this.inferred = false;
		
		data = new ShareableValuesHashMap();
		
		constructedMap = null;
	}
	
	/*package*/ MapWriter(Type mapType, ShareableValuesHashMap data){
		super();
		
		this.mapType = mapType;
		this.keyType = mapType.getKeyType();
		this.valueType = mapType.getValueType();
		this.data = data;
		this.inferred = false;
		
		constructedMap = null;
	}

	@Override
	public void put(IValue key, IValue value){
		checkMutation();
		updateTypes(key,value);
		
		IValue replaced = data.put(key, value);
		
		if (replaced != null) {
			inferredTypeinvalidated = true;
		}
	}
	
	private void updateTypes(IValue key, IValue value) {
		if (inferred) {
			keyType = keyType.lub(key.getType());
			valueType = valueType.lub(value.getType());
		}
	}

	@Override
	public void putAll(IMap map){
		checkMutation();
		
		Iterator<Entry<IValue, IValue>> entryIterator = map.entryIterator();
		while(entryIterator.hasNext()){
			Entry<IValue, IValue> entry = entryIterator.next();
			IValue key = entry.getKey();
			IValue value = entry.getValue();
			updateTypes(key,value);
			data.put(key, value);
		}
	}
	
	@Override
	public void putAll(java.util.Map<IValue, IValue> map){
		checkMutation();
		
		Iterator<Entry<IValue, IValue>> entryIterator = map.entrySet().iterator();
		while(entryIterator.hasNext()){
			Entry<IValue, IValue> entry = entryIterator.next();
			IValue key = entry.getKey();
			IValue value = entry.getValue();
			updateTypes(key,value);
			data.put(key,value);
		}
	}
	
	@Override
	public void insert(IValue... values){
		checkMutation();
		
		for(int i = values.length - 1; i >= 0; i--){
			IValue value = values[i];
			
			if(!(value instanceof ITuple)) throw new IllegalArgumentException("Argument must be of ITuple type.");
			
			ITuple tuple = (ITuple) value;
			
			if(tuple.arity() != 2) throw new IllegalArgumentException("Tuple must have an arity of 2.");
			
			IValue key = tuple.get(0);
			IValue value2 = tuple.get(1);
			updateTypes(key,value2);
			put(key, value2);
		}
	}
	
	@Override
	public void insertAll(Iterable<? extends IValue> collection){
		checkMutation();
		
		Iterator<? extends IValue> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			IValue value = collectionIterator.next();
			
			if(!(value instanceof ITuple)) throw new IllegalArgumentException("Argument must be of ITuple type.");
			
			ITuple tuple = (ITuple) value;
			
			if(tuple.arity() != 2) throw new IllegalArgumentException("Tuple must have an arity of 2.");
			
			IValue key = tuple.get(0);
			IValue value2 = tuple.get(1);
			updateTypes(key,value2);
			put(key, value2);
		}
	}
	
	protected void checkMutation() {
		if (constructedMap != null)
			throw new UnsupportedOperationException(
					"Mutation of a finalized map is not supported.");
	}
	
	@Override
	public IMap done(){
		if(constructedMap == null) {
			if (mapType == null) {
				mapType = TypeFactory.getInstance().mapType(keyType, valueType);
			}
			if (data.isEmpty()) {
				Type voidType = TypeFactory.getInstance().voidType();
				Type voidMapType = TypeFactory.getInstance().mapType(voidType, mapType.getKeyLabel(), voidType, mapType.getValueLabel());

				constructedMap = Map.newMap(voidMapType, data);
			} else {
				if (inferred && inferredTypeinvalidated) {
					Type voidType = TypeFactory.getInstance().voidType();
					
					keyType = voidType;
					valueType = voidType;
					
					for (Iterator<Entry<IValue, IValue>> it = data.entryIterator(); it.hasNext(); ) {
						final Entry<IValue, IValue> currentEntry = it.next();
						
						keyType = keyType.lub(currentEntry.getKey().getType());
						valueType = valueType.lub(currentEntry.getValue().getType());
						
						mapType = TypeFactory.getInstance().mapType(keyType, mapType.getKeyLabel(), valueType, mapType.getValueLabel());
					}
				}
				
				constructedMap = Map.newMap(mapType, data);
			}
		}

		return constructedMap;
	}
}
