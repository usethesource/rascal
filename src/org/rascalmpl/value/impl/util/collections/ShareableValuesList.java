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
package org.rascalmpl.value.impl.util.collections;

import java.util.Iterator;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.util.ShareableList;

/**
 * A specialized version of the ShareableList, specifically meant for storing values.
 * 
 * @author Arnold Lankamp
 */
public class ShareableValuesList extends ShareableList<IValue>{
	
	public ShareableValuesList(){
		super();
	}
	
	public ShareableValuesList(ShareableValuesList shareableValuesList){
		super(shareableValuesList);
	}
	
	public ShareableValuesList(ShareableValuesList shareableValuesList, int offset, int length){
		super(shareableValuesList, offset, length);
	}
	
	public boolean isEqual(ShareableValuesList otherShareableValuesList){
		if(otherShareableValuesList == null) return false;
		if(otherShareableValuesList.size() != size()) return false;
		
		if(otherShareableValuesList.isEmpty()) return true;
		
		Iterator<IValue> thisListIterator = iterator();
		Iterator<IValue> otherListIterator = otherShareableValuesList.iterator();
		while(thisListIterator.hasNext()){
			IValue thisValue = thisListIterator.next();
			IValue otherValue = otherListIterator.next();
			if(!thisValue.isEqual(otherValue)){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean contains(IValue value){
		Iterator<IValue> valuesIterator = iterator();
		while(valuesIterator.hasNext()){
			IValue next = valuesIterator.next();
			if(next.isEqual(value)) return true;
		}
		
		return false;
	}
	
	public boolean remove(IValue value){
		int index = 0;
		Iterator<IValue> valuesIterator = iterator();
		while(valuesIterator.hasNext()){
			IValue next = valuesIterator.next();
			if(next.isEqual(value)) break;
			
			index++;
		}
		
		if(index < size()){
			remove(index);
			return true;
		}
		
		return false;
	}
	
	public ShareableValuesList subList(int offset, int length){
		if(offset < 0) throw new IndexOutOfBoundsException("Offset may not be smaller than 0.");
		if(length < 0) throw new IndexOutOfBoundsException("Length may not be smaller than 0.");
		if((offset + length) > size()) throw new IndexOutOfBoundsException("'offset + length' may not be larger than 'list.size()'");
		
		return new ShareableValuesList(this, offset, length);
	}
}
