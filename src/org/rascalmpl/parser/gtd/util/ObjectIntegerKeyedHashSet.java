/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.util;

@SuppressWarnings("unchecked")
public class ObjectIntegerKeyedHashSet<E>{
	private Entry<E>[] entries;

	private int hashMask;
	private int bitSize = 2;
	
	private int threshold;
	private int load;
	
	public ObjectIntegerKeyedHashSet(){
		super();
		
		int nrOfEntries = 1 << bitSize;
		
		hashMask = nrOfEntries - 1;
		
		entries = new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	private void rehash(){
		int nrOfEntries = 1 << (++bitSize);
		int newHashMask = nrOfEntries - 1;
		
		Entry<E>[] oldEntries = entries;
		Entry<E>[] newEntries = new Entry[nrOfEntries];
		
		Entry<E> currentEntryRoot = new Entry<E>(null, 0, 0, null);
		Entry<E> shiftedEntryRoot = new Entry<E>(null, 0, 0, null);
		
		int oldSize = oldEntries.length;
		for(int i = oldSize - 1; i >= 0; --i){
			Entry<E> e = oldEntries[i];
			if(e != null){
				Entry<E> lastCurrentEntry = currentEntryRoot;
				Entry<E> lastShiftedEntry = shiftedEntryRoot;
				int lastPosition = -1;
				do{
					int position = e.hash & newHashMask;
					
					if(position == i){
						if(position != lastPosition) lastCurrentEntry.next = e;
						lastCurrentEntry = e;
					}else{
						if(position != lastPosition) lastShiftedEntry.next = e;
						lastShiftedEntry = e;
					}
					
					e = e.next;
				}while(e != null);
				
				lastCurrentEntry.next = null;
				lastShiftedEntry.next = null;
				
				newEntries[i] = currentEntryRoot.next;
				newEntries[i | oldSize] = shiftedEntryRoot.next;
			}
		}
		
		threshold <<= 1;
		entries = newEntries;
		hashMask = newHashMask;
	}
	
	private void ensureCapacity(){
		if(load > threshold){
			rehash();
		}
	}
	
	public boolean put(E element, int element2){
		ensureCapacity();
		
		int hash = element.hashCode() ^ (element2 << 5);
		int position = hash & hashMask;
		
		Entry<E> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<E> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.element2 == element2 && entry.element.equals(element)){
					return false;
				}
			}while((entry = entry.next) != null);
		}
		
		entries[position] = new Entry<E>(element, element2, hash, currentStartEntry);
		++load;
		
		return true;
	}
	
	public void putUnsafe(E element, int element2){
		ensureCapacity();
		
		int hash = element.hashCode() ^ (element2 << 5);
		int position = hash & hashMask;
		
		entries[position] = new Entry<E>(element, element2, hash, entries[position]);
		++load;
	}
	
	public boolean remove(E element, int element2){
		int hash = element.hashCode() ^ (element2 << 5);
		int position = hash & hashMask;
		
		Entry<E> previous = null;
		Entry<E> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<E> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.element2 == element2 && entry.element.equals(element)){
					if(previous == null){
						entries[position] = entry.next;
					}else{
						previous.next = entry.next;
					}
					load--;
					return true;
				}
				
				previous = entry;
			}while((entry = entry.next) != null);
		}
		
		return false;
	}
	
	public boolean contains(E element, int element2){
		int hash = element.hashCode() ^ (element2 << 5);
		int position = hash & hashMask;
		
		Entry<E> entry = entries[position];
		while(entry != null){
			if(hash == entry.hash && element2 == entry.element2 && element.equals(entry.element)) return true;
			
			entry = entry.next;
		}
		
		return false;
	}
	
	public E getEquivalent(E element, int element2){
		int hash = element.hashCode() ^ (element2 << 5);
		int position = hash & hashMask;
		
		Entry<E> entry = entries[position];
		while(entry != null){
			if(hash == entry.hash && element2 == entry.element2 && element.equals(entry.element)) return entry.element;
			
			entry = entry.next;
		}
		
		return null;
	}
	
	public void clear(){
		entries = new Entry[entries.length];
		
		load = 0;
	}
	
	private static class Entry<E>{
		public final int hash;
		public final E element;
		public final int element2;
		public Entry<E> next;
		
		public Entry(E element, int element2, int hash, Entry<E> next){
			super();
			
			this.element = element;
			this.element2 = element2;
			this.hash = hash;
			this.next = next;
		}
	}
}
