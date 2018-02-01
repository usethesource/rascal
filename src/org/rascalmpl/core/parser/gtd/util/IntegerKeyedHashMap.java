/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.util;

import java.util.Iterator;

@SuppressWarnings({"unchecked", "cast"})
public class IntegerKeyedHashMap<V>{
	private final static int DEFAULT_BIT_SIZE = 2;
	
	private Entry<V>[] entries;

	private int hashMask;
	private int bitSize;
	
	private int threshold;
	private int load;
	
	public IntegerKeyedHashMap(){
		super();
		
		int nrOfEntries = 1 << (bitSize = DEFAULT_BIT_SIZE);
		
		hashMask = nrOfEntries - 1;
		
		entries = (Entry<V>[]) new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	private void rehash(){
		int nrOfEntries = 1 << (++bitSize);
		int newHashMask = nrOfEntries - 1;
		
		Entry<V>[] oldEntries = entries;
		Entry<V>[] newEntries = (Entry<V>[]) new Entry[nrOfEntries];
		
		Entry<V> currentEntryRoot = new Entry<V>(-1, null, null);
		Entry<V> shiftedEntryRoot = new Entry<V>(-1, null, null);
		
		int oldSize = oldEntries.length;
		for(int i = oldSize - 1; i >= 0; --i){
			Entry<V> e = oldEntries[i];
			if(e != null){
				Entry<V> lastCurrentEntry = currentEntryRoot;
				Entry<V> lastShiftedEntry = shiftedEntryRoot;
				int lastPosition = -1;
				do{
					int position = e.key & newHashMask;
					
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
	
	public V put(int key, V value){
		ensureCapacity();
		
		int position = key & hashMask;
		
		Entry<V> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<V> entry = currentStartEntry;
			do{
				if(entry.key == key){
					V oldValue = entry.value;
					entry.value = value;
					return oldValue;
				}
			}while((entry = entry.next) != null);
		}
		
		entries[position] = new Entry<V>(key, value, currentStartEntry);
		++load;
		
		return null;
	}
	
	public void putAll(IntegerKeyedHashMap<V> all) {
		for (Entry<V> e : all.entries) {
			if (e != null) {
				put(e.key, e.value);
			}
		}
	}
	
	public void putUnsafe(int key, V value){
		ensureCapacity();
		
		int position = key & hashMask;
		entries[position] = new Entry<V>(key, value, entries[position]);
		++load;
	}
	
	public V get(int key){
		int position = key & hashMask;
		
		Entry<V> entry = entries[position];
		while(entry != null){
			if(entry.key == key) return entry.value;
			
			entry = entry.next;
		}
		
		return null;
	}
	
	public V remove(int key){
		int position = key & hashMask;
		
		Entry<V> previous = null;
		Entry<V> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<V> entry = currentStartEntry;
			do{
				if(entry.key == key){
					if(previous == null){
						entries[position] = entry.next;
					}else{
						previous.next = entry.next;
					}
					--load;
					return entry.value;
				}
				
				previous = entry;
			}while((entry = entry.next) != null);
		}
		
		return null;
	}

	public int size() {
		return load;
	}
	
	public void clear(){
		int nrOfEntries = 1 << (bitSize = DEFAULT_BIT_SIZE);
		
		hashMask = nrOfEntries - 1;
		
		entries = (Entry<V>[]) new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	public Iterator<Entry<V>> entryIterator(){
		return new EntryIterator<V>(this);
	}
	
	private static class EntryIterator<V> implements Iterator<Entry<V>>{
		private final Entry<V>[] data;
		
		private Entry<V> current;
		private int index;
		
		public EntryIterator(IntegerKeyedHashMap<V> integerKeyedHashMap){
			super();
			
			data = integerKeyedHashMap.entries;

			index = data.length - 1;
			current = new Entry<V>(-1, null, data[index]);
			locateNext();
		}
		
		private void locateNext(){
			Entry<V> next = current.next;
			if(next != null){
				current = next;
				return;
			}
			
			for(int i = index - 1; i >= 0 ; i--){
				Entry<V> entry = data[i];
				if(entry != null){
					current = entry;
					index = i;
					return;
				}
			}
			
			current = null;
			index = 0;
		}
		
		public boolean hasNext(){
			return (current != null);
		}
		
		public Entry<V> next(){
			if(!hasNext()) throw new UnsupportedOperationException("There are no more elements in this iterator.");
			
			Entry<V> entry = current;
			locateNext();
			
			return entry;
		}
		
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
	
	public static class Entry<V>{
		public final int key;
		public V value;
		public Entry<V> next;
		
		public Entry(int key, V value, Entry<V> next){
			super();
			
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}
}
