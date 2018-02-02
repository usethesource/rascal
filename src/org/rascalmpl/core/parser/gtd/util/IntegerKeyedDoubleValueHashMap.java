/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.core.parser.gtd.util;

import java.util.Iterator;

@SuppressWarnings({"unchecked", "cast"})
public class IntegerKeyedDoubleValueHashMap<V1, V2> {
	private final static int DEFAULT_BIT_SIZE = 2;
	
	private Entry<V1, V2>[] entries;

	private int hashMask;
	private int bitSize;
	
	private int threshold;
	private int load;
	
	public IntegerKeyedDoubleValueHashMap(){
		super();
		
		int nrOfEntries = 1 << (bitSize = DEFAULT_BIT_SIZE);
		
		hashMask = nrOfEntries - 1;
		
		entries = (Entry<V1, V2>[]) new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	private void rehash(){
		int nrOfEntries = 1 << (++bitSize);
		int newHashMask = nrOfEntries - 1;
		
		Entry<V1, V2>[] oldEntries = entries;
		Entry<V1, V2>[] newEntries = (Entry<V1, V2>[]) new Entry[nrOfEntries];
		
		Entry<V1, V2> currentEntryRoot = new Entry<V1, V2>(-1, null, null, null);
		Entry<V1, V2> shiftedEntryRoot = new Entry<V1, V2>(-1, null, null, null);
		
		int oldSize = oldEntries.length;
		for(int i = oldSize - 1; i >= 0; --i){
			Entry<V1, V2> e = oldEntries[i];
			if(e != null){
				Entry<V1, V2> lastCurrentEntry = currentEntryRoot;
				Entry<V1, V2> lastShiftedEntry = shiftedEntryRoot;
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
	
	public boolean put(int key, V1 value1, V2 value2){
		ensureCapacity();
		
		int position = key & hashMask;
		
		Entry<V1, V2> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<V1, V2> entry = currentStartEntry;
			do{
				if(entry.key == key){
					entry.value1 = value1;
					entry.value2 = value2;
					return false;
				}
			}while((entry = entry.next) != null);
		}
		
		entries[position] = new Entry<V1, V2>(key, value1, value2, currentStartEntry);
		++load;
		
		return true;
	}
	
	public void putUnsafe(int key, V1 value1, V2 value2){
		ensureCapacity();
		
		int position = key & hashMask;
		entries[position] = new Entry<V1, V2>(key, value1, value2, entries[position]);
		++load;
	}
	
	public Entry<V1, V2> get(int key){
		int position = key & hashMask;
		
		Entry<V1, V2> entry = entries[position];
		while(entry != null){
			if(entry.key == key) return entry;
			
			entry = entry.next;
		}
		
		return null;
	}
	
	public Entry<V1, V2> remove(int key){
		int position = key & hashMask;
		
		Entry<V1, V2> previous = null;
		Entry<V1, V2> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<V1, V2> entry = currentStartEntry;
			do{
				if(entry.key == key){
					if(previous == null){
						entries[position] = entry.next;
					}else{
						previous.next = entry.next;
					}
					--load;
					return entry;
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
		
		entries = (Entry<V1, V2>[]) new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	public Iterator<Entry<V1, V2>> entryIterator(){
		return new EntryIterator<V1, V2>(this);
	}
	
	private static class EntryIterator<V1, V2> implements Iterator<Entry<V1, V2>>{
		private final Entry<V1, V2>[] data;
		
		private Entry<V1, V2> current;
		private int index;
		
		public EntryIterator(IntegerKeyedDoubleValueHashMap<V1, V2> integerKeyedDoubleValueHashMap){
			super();
			
			data = integerKeyedDoubleValueHashMap.entries;

			index = data.length - 1;
			current = new Entry<V1, V2>(-1, null, null, data[index]);
			locateNext();
		}
		
		private void locateNext(){
			Entry<V1, V2> next = current.next;
			if(next != null){
				current = next;
				return;
			}
			
			for(int i = index - 1; i >= 0 ; i--){
				Entry<V1, V2> entry = data[i];
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
		
		public Entry<V1, V2> next(){
			if(!hasNext()) throw new UnsupportedOperationException("There are no more elements in this iterator.");
			
			Entry<V1, V2> entry = current;
			locateNext();
			
			return entry;
		}
		
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
	
	public static class Entry<V1, V2>{
		public final int key;
		public V1 value1;
		public V2 value2;
		public Entry<V1, V2> next;
		
		public Entry(int key, V1 value1, V2 value2, Entry<V1, V2> next){
			super();
			
			this.key = key;
			this.value1 = value1;
			this.value2 = value2;
			this.next = next;
		}
	}
}
