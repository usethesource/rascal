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

public class IntegerMap{
	private final static int DEFAULT_BIT_SIZE = 2;
	
	private Entry[] entries;

	private int hashMask;
	private int bitSize;
	
	private int threshold;
	private int load;
	
	public IntegerMap(){
		super();
		
		int nrOfEntries = 1 << (bitSize = DEFAULT_BIT_SIZE);
		
		hashMask = nrOfEntries - 1;
		
		entries = new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	private void rehash(){
		int nrOfEntries = 1 << (++bitSize);
		int newHashMask = nrOfEntries - 1;
		
		Entry[] oldEntries = entries;
		Entry[] newEntries = new Entry[nrOfEntries];
		
		Entry currentEntryRoot = new Entry(-1, -1, null);
		Entry shiftedEntryRoot = new Entry(-1, -1, null);
		
		int oldSize = oldEntries.length;
		for(int i = oldSize - 1; i >= 0; --i){
			Entry e = oldEntries[i];
			if(e != null){
				Entry lastCurrentEntry = currentEntryRoot;
				Entry lastShiftedEntry = shiftedEntryRoot;
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
	
	public int put(int key, int value){
		ensureCapacity();
		
		int position = key & hashMask;
		
		Entry currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry entry = currentStartEntry;
			do{
				if(entry.key == key){
					int oldValue = entry.value;
					entry.value = value;
					return oldValue;
				}
			}while((entry = entry.next) != null);
		}
		
		entries[position] = new Entry(key, value, currentStartEntry);
		++load;
		
		return -1;
	}
	
	public void putUnsafe(int key, int value){
		ensureCapacity();
		
		int position = key & hashMask;
		entries[position] = new Entry(key, value, entries[position]);
		++load;
	}
	
	public int get(int key){
		int position = key & hashMask;
		
		Entry entry = entries[position];
		while(entry != null){
			if(entry.key == key) return entry.value;
			
			entry = entry.next;
		}
		
		return -1;
	}
	
	public int remove(int key){
		int position = key & hashMask;
		
		Entry previous = null;
		Entry currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry entry = currentStartEntry;
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
		
		return -1;
	}

	public int size() {
		return load;
	}
	
	public void clear(){
		int nrOfEntries = 1 << (bitSize = DEFAULT_BIT_SIZE);
		
		hashMask = nrOfEntries - 1;
		
		entries = new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	private static class Entry{
		public final int key;
		public int value;
		public Entry next;
		
		public Entry(int key, int value, Entry next){
			super();
			
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}
}
