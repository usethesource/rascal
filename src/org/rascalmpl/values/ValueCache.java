/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values;

public class ValueCache<E>{
	private final static int INITIAL_LOG_SIZE = 4;

	private int modSize;
	private int hashMask;
	
	private Entry<E>[] data;
	
	private int threshold;
	private int load;
	
	@SuppressWarnings("unchecked")
	public ValueCache(){
		super();
		
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
	}
	
	private void rehash(){
		int nrOfEntries = 1 << (++modSize);
		int newHashMask = nrOfEntries - 1;
		
		@SuppressWarnings("unchecked")
		Entry<E>[] newData = new Entry[nrOfEntries];
		
		Entry<E> currentEntryRoot = new Entry<E>(0, null, null);
		Entry<E> shiftedEntryRoot = new Entry<E>(0, null, null);
		
		int oldSize = data.length;
		for(int i = oldSize - 1; i >= 0; i--){
			Entry<E> e = data[i];
			if(e != null){
				Entry<E> lastCurrentEntry = currentEntryRoot;
				Entry<E> lastShiftedEntry = shiftedEntryRoot;
				do{
					int position = e.hash & newHashMask;
					
					if(position == i){
						lastCurrentEntry.next = e;
						lastCurrentEntry = e;
					}else{
						lastShiftedEntry.next = e;
						lastShiftedEntry = e;
					}
					
					e = e.next;
				}while(e != null);
				
				lastCurrentEntry.next = null;
				lastShiftedEntry.next = null;
				
				newData[i] = currentEntryRoot.next;
				newData[i | oldSize] = shiftedEntryRoot.next;
			}
		}
		
		threshold <<= 1;
		data = newData;
		hashMask = newHashMask;
	}
	
	public E cache(E element){
		if(load > threshold){
			rehash();
		}
		
		int hash = element.hashCode();
		int position = hash & hashMask;
		
		Entry<E> currentStartEntry = data[position];
		// Check if the value is already in here.
		if(currentStartEntry != null){
			Entry<E> entry = currentStartEntry;
			do{
				E currentValue = entry.element;
				if(hash == entry.hash && currentValue.equals(element)){
					return currentValue;
				}
				
				entry = entry.next;
			}while(entry != null);
		}
		
		data[position] = new Entry<E>(hash, element, currentStartEntry); // Insert the new entry.
		
		load++;
		
		return element;
	}
	
	private static class Entry<E>{
		public final int hash;
		public final E element;
		
		public Entry<E> next;
		
		public Entry(int hash, E element, Entry<E> next){
			super();
			
			this.hash = hash;
			this.element = element;
			
			this.next = next;
		}
	}
}
