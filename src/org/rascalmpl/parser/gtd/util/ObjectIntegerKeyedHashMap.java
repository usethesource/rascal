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

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class ObjectIntegerKeyedHashMap<K, V>{
	private Entry<K, V>[] entries;

	private int hashMask;
	private int bitSize = 2;
	
	private int threshold;
	private int load;
	
	
	public ObjectIntegerKeyedHashMap(){
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
		
		Entry<K, V>[] oldEntries = entries;
		Entry<K, V>[] newEntries = new Entry[nrOfEntries];
		
		Entry<K, V> currentEntryRoot = new Entry<K, V>(null, 0, null, 0, null);
		Entry<K, V> shiftedEntryRoot = new Entry<K, V>(null, 0, null, 0, null);
		
		int oldSize = oldEntries.length;
		for(int i = oldSize - 1; i >= 0; --i){
			Entry<K, V> e = oldEntries[i];
			if(e != null){
				Entry<K, V> lastCurrentEntry = currentEntryRoot;
				Entry<K, V> lastShiftedEntry = shiftedEntryRoot;
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
	
	public V put(K key, int key2, V value){
		ensureCapacity();
		
		int hash = key.hashCode() ^ (key2 << 5);
		int position = hash & hashMask;
		
		Entry<K, V> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<K, V> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.key2 == key2 && entry.key.equals(key)){
					V oldValue = entry.value;
					entry.value = value;
					return oldValue;
				}
			}while((entry = entry.next) != null);
		}
		
		entries[position] = new Entry<K, V>(key, key2, value, hash, currentStartEntry);
		++load;
		
		return null;
	}
	
	public void putUnsafe(K key, int key2, V value){
		ensureCapacity();
		
		int hash = key.hashCode() ^ (key2 << 5);
		int position = hash & hashMask;
		
		entries[position] = new Entry<K, V>(key, key2, value, hash, entries[position]);
		++load;
	}
	
	public V remove(K key, int key2){
		int hash = key.hashCode() ^ (key2 << 5);
		int position = hash & hashMask;
		
		Entry<K, V> previous = null;
		Entry<K, V> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<K, V> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.key2 == key2 && entry.key.equals(key)){
					if(previous == null){
						entries[position] = entry.next;
					}else{
						previous.next = entry.next;
					}
					load--;
					return entry.value;
				}
				
				previous = entry;
			}while((entry = entry.next) != null);
		}
		
		return null;
	}
	
	public V get(K key, int key2){
		int hash = key.hashCode() ^ (key2 << 5);
		int position = hash & hashMask;
		
		Entry<K, V> entry = entries[position];
		while(entry != null){
			if(hash == entry.hash && key2 == entry.key2 && key.equals(entry.key)) return entry.value;
			
			entry = entry.next;
		}
		
		return null;
	}
	
	public boolean contains(K key, int key2){
		int hash = key.hashCode() ^ (key2 << 5);
		int position = hash & hashMask;
		
		Entry<K, V> entry = entries[position];
		while(entry != null){
			if(hash == entry.hash && key2 == entry.key2 && key.equals(entry.key)) return true;
			
			entry = entry.next;
		}
		
		return false;
	}
	
	public int size(){
		return load;
	}
	
	public void clear(){
		entries = new Entry[entries.length];
		
		load = 0;
	}
	
	public Iterator<V> valueIterator(){
		return new ValueIterator<K, V>(this);
	}
	
	private static class ValueIterator<K, V> implements Iterator<V>{
		private final Entry<K, V>[] data;
		
		private Entry<K, V> current;
		private int index;
		
		public ValueIterator(ObjectIntegerKeyedHashMap<K, V> hashMap){
			super();
			
			data = hashMap.entries;

			index = data.length - 1;
			current = new Entry<K, V>(null, -1, null, -1, data[index]);
			locateNext();
		}
		
		private void locateNext(){
			Entry<K, V> next = current.next;
			if(next != null){
				current = next;
				return;
			}
			
			for(int i = index - 1; i >= 0 ; i--){
				Entry<K, V> entry = data[i];
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
		
		public V next(){
			if(!hasNext()) throw new UnsupportedOperationException("There are no more elements in this iterator.");
			
			Entry<K, V> entry = current;
			locateNext();
			
			return entry.value;
		}
		
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
	
	private static class Entry<K, V>{
		public final int hash;
		public final K key;
		public final int key2;
		public V value;
		public Entry<K, V> next;
		
		public Entry(K key, int key2, V value, int hash, Entry<K, V> next){
			super();
			
			this.key = key;
			this.key2 = key2;
			this.value = value;
			this.hash = hash;
			this.next = next;
		}
	}
}
