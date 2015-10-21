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
package org.rascalmpl.value.util;

/**
 * This is a hashset which assigns a unique identifier to every inserted entry. Identifiers will be
 * generated in order, starting at 0. 
 * 
 * @author Arnold Lankamp
 *
 * @param <E> The value type.
 */
public final class IndexedSet<E>{
	private final static int INITIAL_LOG_SIZE = 4;

	private int modSize;
	private int hashMask;
	
	private Entry<E>[] data;
	
	private int threshold;
	
	private int load;
	
	/**
	 * Constructor.
	 */
	public IndexedSet(){
		super();
		
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<E>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
	}
	
	private void rehash(){
		modSize++;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		Entry<E>[] newData = (Entry<E>[]) new Entry[tableSize];

		threshold = tableSize;
		
		Entry<E>[] oldData = data;
		for(int i = oldData.length - 1; i >= 0; i--){
			Entry<E> entry = oldData[i];
			
			if(entry != null){
				// Determine the last unchanged entry chain.
				Entry<E> lastUnchangedEntryChain = entry;
				int newLastUnchangedEntryChainIndex = entry.hash & hashMask;
				
				Entry<E> e = entry.next;
				while(e != null){
					int newIndex = e.hash & hashMask;
					if(newIndex != newLastUnchangedEntryChainIndex){
						lastUnchangedEntryChain = e;
						newLastUnchangedEntryChainIndex = newIndex;
					}
					
					e = e.next;
				}
	
				newData[newLastUnchangedEntryChainIndex] = lastUnchangedEntryChain;
				
				// Reconstruct the other entries (if necessary).
				while(entry != lastUnchangedEntryChain){
					int hash = entry.hash;
					int position = hash & hashMask;
					newData[position] = new Entry<>(hash, entry.key, entry.value, newData[position]);
					
					entry = entry.next;
				}
			}
		}
		
		data = newData;
	}
	
	private void ensureCapacity(){
		if(load > threshold){
			rehash();
		}
	}
	
	/**
	 * Stores the given element into this set or returns its identifier in case it was already
	 * present.
	 * 
	 * @param element
	 *            The element to store.
	 * @return The identifier associated with the given value; -1 if the element wasn't present yet.
	 */
	public int store(E element){
		int hash = element.hashCode();
		int position = hash & hashMask;
		
		Entry<E> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && element.equals(entry.key)) return entry.value;
			
			entry = entry.next;
		}
		
		// Not present
		
		ensureCapacity();
		
		position = hash & hashMask;
		
		data[position] = new Entry<>(hash, element, load++, data[position]);
		
		return -1;
	}
	
	/**
	 * Attempts to find the identifier associted with the given element.
	 * 
	 * @param element
	 *            The element to retrieve the identifier from.
	 * @return The identifier associated with the given element; -1 if not present,
	 */
	public int get(E element){
		int hash = element.hashCode();
		int position = hash & hashMask;
		
		Entry<E> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && element.equals(entry.key)) return entry.value;
			
			entry = entry.next;
		}
		
		return -1;
	}
	
	private static class Entry<E>{
		public final int hash;
		public final E key;
		public final int value;
		
		public final Entry<E> next;
		
		public Entry(int hash, E key, int value, Entry<E> next){
			super();
			
			this.hash = hash;
			this.key = key;
			this.value = value;
			
			this.next = next;
		}
	}
}
