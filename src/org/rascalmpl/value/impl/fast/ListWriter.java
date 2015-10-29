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

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.impl.util.collections.ShareableValuesList;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

// TODO Add checking.
/**
 * Implementation of IListWriter.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class ListWriter implements IListWriter{
	protected Type elementType;
	protected final boolean inferred;
	
	protected final ShareableValuesList data;
	
	protected IList constructedList;
	
	/*package*/ ListWriter(Type elementType){
		super();
		
		this.elementType = elementType;
		this.inferred = false;
		
		data = new ShareableValuesList();
		
		constructedList = null;
	}
	
	/*package*/ ListWriter(){
		super();
		
		this.elementType = TypeFactory.getInstance().voidType();
		this.inferred = true;
		data = new ShareableValuesList();
		
		constructedList = null;
	}
	
	/*package*/ ListWriter(Type elementType, ShareableValuesList data){
		super();
		
		this.elementType = elementType;
		this.inferred = false;
		this.data = data;
		
		constructedList = null;
	}
	
	public void append(IValue element){
		checkMutation();
		
		updateType(element);
		data.append(element);
	}
	
	private void updateType(IValue element) {
		if (inferred) {
			elementType = elementType.lub(element.getType());
		}
	}

	public void append(IValue... elems){
		checkMutation();
		
		for(IValue elem : elems){
			updateType(elem);
			data.append(elem);
		}
	}
	
	public void appendAll(Iterable<? extends IValue> collection){
		checkMutation();
		
		Iterator<? extends IValue> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			IValue next = collectionIterator.next();
			updateType(next);
			data.append(next);
		}
	}
	
	public void insert(IValue elem){
		checkMutation();
		updateType(elem);
		data.insert(elem);
	}
	
	public void insert(IValue... elements){
		insert(elements, 0, elements.length);
	}
	
	public void insert(IValue[] elements, int start, int length){
		checkMutation();
		checkBounds(elements, start, length);
		
		for(int i = start + length - 1; i >= start; i--){
			updateType(elements[i]);
			data.insert(elements[i]);
		}
	}
	
	public void insertAll(Iterable<? extends IValue> collection){
		checkMutation();
		
		Iterator<? extends IValue> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			IValue next = collectionIterator.next();
			updateType(next);
			data.insert(next);
		}
	}
	
	public void insertAt(int index, IValue element){
		checkMutation();
		
		updateType(element);
		data.insertAt(index, element);
	}
	
	public void insertAt(int index, IValue... elements){
		insertAt(index, elements, 0, 0);
	}
	
	public void insertAt(int index, IValue[] elements, int start, int length){
		checkMutation();
		checkBounds(elements, start, length);
		
		for(int i = start + length - 1; i >= start; i--){
			updateType(elements[i]);
			data.insertAt(index, elements[i]);
		}
	}
	
	public IValue replaceAt(int index, IValue element){
		checkMutation();
		
		updateType(element);
		return data.set(index, element);
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		return data.get(i);
	}
	
	@Override
	public int length() {
		return data.size();
	}
	
	protected void checkMutation(){
		if(constructedList != null) throw new UnsupportedOperationException("Mutation of a finalized list is not supported.");
	}
	
	private void checkBounds(IValue[] elems, int start, int length){
		if(start < 0) throw new ArrayIndexOutOfBoundsException("start < 0");
		if((start + length) > elems.length) throw new ArrayIndexOutOfBoundsException("(start + length) > elems.length");
	}
	
	@Override
	public IList done() {
		if (constructedList == null) {
			constructedList = List.newList(data.isEmpty() ? TypeFactory.getInstance().voidType() : elementType, data);
		}
		
		return constructedList;
	}
}