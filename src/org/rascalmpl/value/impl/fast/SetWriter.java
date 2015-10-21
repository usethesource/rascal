/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/
package org.rascalmpl.value.impl.fast;

import java.util.Iterator;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashSet;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

// TODO Add checking.
/**
 * Implementation of ISetWriter.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class SetWriter implements ISetWriter{
	protected Type elementType;
	protected final boolean inferred;
	
	protected final ShareableValuesHashSet data;
	
	protected ISet constructedSet;
	
	/*package*/ SetWriter(Type elementType){
		super();
		
		this.elementType = elementType;
		this.inferred = false;
		
		data = new ShareableValuesHashSet();
		
		constructedSet = null;
	}
	
	/*package*/ SetWriter(){
		super();
		
		this.elementType = TypeFactory.getInstance().voidType();
		this.inferred = true;
		
		data = new ShareableValuesHashSet();
		
		constructedSet = null;
	}

	/*package*/ SetWriter(Type elementType, ShareableValuesHashSet data){
		super();
		
		this.elementType = elementType;
		this.inferred = false;
		this.data = data;
		
		constructedSet = null;
	}
	
	public void insert(IValue value){
		checkMutation();
		updateType(value);
		data.add(value);
	}
	
	private void updateType(IValue value) {
		if (inferred) {
			elementType = elementType.lub(value.getType());
		}
	}

	@Override
	public void insert(IValue... elements){
		checkMutation();
		
		for(int i = elements.length - 1; i >= 0; i--){
			updateType(elements[i]);
			data.add(elements[i]);
		}
	}
	
	@Override
	public void insertAll(Iterable<? extends IValue> collection){
		checkMutation();
		
		Iterator<? extends IValue> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			IValue next = collectionIterator.next();
			updateType(next);
			data.add(next);
		}
	}

	protected void checkMutation(){
		if(constructedSet != null) throw new UnsupportedOperationException("Mutation of a finalized map is not supported.");
	}
	
	@Override
	public ISet done(){
		if (constructedSet == null) {
			constructedSet = Set.newSet(data.isEmpty() ? TypeFactory.getInstance().voidType() : elementType, data);
		}
		
		return constructedSet;
	}
}