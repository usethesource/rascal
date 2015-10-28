/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no - UiB
 *
 * Based on code by:
 *
 *   * Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *******************************************************************************/
package org.rascalmpl.value.impl.reference;

import java.util.Map.Entry;

import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnexpectedMapKeyTypeException;
import org.rascalmpl.value.exceptions.UnexpectedMapValueTypeException;
import org.rascalmpl.value.impl.AbstractWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

/*package*/ class MapWriter extends AbstractWriter implements IMapWriter {
	private Type staticMapType;
	private Type staticKeyType;
	private Type staticValueType;
	private final boolean inferred;
	private final java.util.HashMap<IValue, IValue> mapContent;
	private Map constructedMap;

	/*package*/ MapWriter(){
		super();
		
		this.staticMapType = TypeFactory.getInstance().mapType(
				TypeFactory.getInstance().voidType(),
				TypeFactory.getInstance().voidType());
		this.staticKeyType = TypeFactory.getInstance().voidType();
		this.staticValueType = TypeFactory.getInstance().voidType();
		this.inferred = true;
		
		mapContent = new java.util.HashMap<>();
	}

	/*package*/ MapWriter(Type mapType){
		super();
		
		if(mapType.isFixedWidth() && mapType.getArity() >= 2) {
			mapType = TypeFactory.getInstance().mapTypeFromTuple(mapType);
		}
		
		this.staticMapType = mapType;
		this.staticKeyType = mapType.getKeyType();
		this.staticValueType = mapType.getValueType();
		this.inferred = false;
		
		mapContent = new java.util.HashMap<>();
	}
	
	private static void check(Type key, Type value, Type keyType, Type valueType)
			throws FactTypeUseException {
		if (!key.isSubtypeOf(keyType)) {
			throw new UnexpectedMapKeyTypeException(keyType, key);
		}
		if (!value.isSubtypeOf(valueType)) {
			throw new UnexpectedMapValueTypeException(valueType, value);
		}
	}
	
	private void checkMutation() {
		if (constructedMap != null)
			throw new UnsupportedOperationException(
					"Mutation of a finalized list is not supported.");
	}
	
	@Override
	public void putAll(IMap map) throws FactTypeUseException{
		checkMutation();
		
		for(IValue key : map){
			IValue value = map.get(key);
			updateTypes(key, value);
			mapContent.put(key, value);
		}
	}
	
	private void updateTypes(IValue key, IValue value) {
		if (inferred) {
			staticKeyType = staticKeyType.lub(key.getType());
			staticValueType = staticValueType.lub(value.getType());
		}
		
	}

	@Override
	public void putAll(java.util.Map<IValue, IValue> map) throws FactTypeUseException{
		checkMutation();
		for(Entry<IValue, IValue> entry : map.entrySet()){
			IValue value = entry.getValue();
			updateTypes(entry.getKey(), value);
			check(entry.getKey().getType(), value.getType(), staticKeyType, staticValueType);
			mapContent.put(entry.getKey(), value);
		}
	}

	@Override
	public void put(IValue key, IValue value) throws FactTypeUseException{
		checkMutation();
		updateTypes(key,value);
		mapContent.put(key, value);
	}
	
	@Override
	public void insert(IValue... value) throws FactTypeUseException {
		for(IValue tuple : value){
			ITuple t = (ITuple) tuple;
			IValue key = t.get(0);
			IValue value2 = t.get(1);
			updateTypes(key,value2);
			put(key, value2);
		}
	}
	
	@Override
	public IMap done(){
		// Temporary fix of the static vs dynamic type issue
		Type dynamicKeyType = TypeFactory.getInstance().voidType();
		Type dynamicValueType = TypeFactory.getInstance().voidType();
		for (java.util.Map.Entry<IValue, IValue> entry : mapContent.entrySet()) {
			dynamicKeyType = dynamicKeyType.lub(entry.getKey().getType());
			dynamicValueType = dynamicValueType.lub(entry.getValue().getType());
		}
		// ---
		
		if (constructedMap == null) {
			Type dynamicMapType = TypeFactory.getInstance().mapType(
					dynamicKeyType, staticMapType.getKeyLabel(),
					dynamicValueType, staticMapType.getValueLabel());

			constructedMap = new Map(dynamicMapType, mapContent);
		}

		return constructedMap;
	}	
	
}
