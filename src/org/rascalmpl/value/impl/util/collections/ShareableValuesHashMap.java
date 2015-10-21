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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.util.ShareableHashSet;

/**
 * A specialized version of the ShareableHashMap, specifically meant for storing values.
 * 
 * @author Arnold Lankamp
 */
public final class ShareableValuesHashMap implements Map<IValue, IValue>{
	private final static int INITIAL_LOG_SIZE = 4;

	private int modSize;
	private int hashMask;
	
	private Entry<IValue, IValue>[] data; // protected for easier access in inner class
	
	private int threshold;
	
	private int load;
	
	private int currentHashCode;
	
	@SuppressWarnings("unchecked")
	public ShareableValuesHashMap(){
		super();
		
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<IValue, IValue>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
		
		currentHashCode = 0;
	}
	
	public ShareableValuesHashMap(ShareableValuesHashMap shareableValuesHashMap){
		super();
		
		modSize = shareableValuesHashMap.modSize;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = shareableValuesHashMap.data.clone();
		
		threshold = tableSize;
		
		load = shareableValuesHashMap.load;
		
		currentHashCode = shareableValuesHashMap.currentHashCode;
	}
	
	@SuppressWarnings("unchecked")
	public void clear(){
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<IValue, IValue>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
		
		currentHashCode = 0;
	}
	
	@SuppressWarnings("unchecked")
	private void rehash(){
		modSize++;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		Entry<IValue, IValue>[] newData = (Entry<IValue, IValue>[]) new Entry[tableSize];

		threshold = tableSize;
		
		Entry<IValue, IValue>[] oldData = data;
		for(int i = oldData.length - 1; i >= 0; i--){
			Entry<IValue, IValue> entry = oldData[i];
			
			if(entry != null){
				// Determine the last unchanged entry chain.
				Entry<IValue, IValue> lastUnchangedEntryChain = entry;
				int newLastUnchangedEntryChainIndex = entry.hash & hashMask;
				
				Entry<IValue, IValue> e = entry.next;
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
	
	private void replaceValue(int position, Entry<IValue, IValue> entry, IValue newValue){
		Entry<IValue, IValue> e = data[position];
		
		// Reconstruct the updated entry.
		data[position] = new Entry<>(entry.hash, entry.key, newValue, entry.next);
		
		// Reconstruct the other entries (if necessary).
		while(e != entry){
			data[position] = new Entry<>(e.hash, e.key, e.value, data[position]);
			
			e = e.next;
		}
	}
	
	public IValue put(IValue key, IValue value){
		ensureCapacity();
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue, IValue> currentStartEntry = data[position];
		// Check if the key is already in here.
		if(currentStartEntry != null){
			Entry<IValue, IValue> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.key.isEqual(key)){ // Replace if present.
					replaceValue(position, entry, value);
					
					return entry.value; // Return the old value.
				}
				
				entry = entry.next;
			}while(entry != null);
		}
		
		data[position] = new Entry<>(hash, key, value, currentStartEntry); // Insert the new entry.
		
		load++;
		
		currentHashCode ^= hash; // Update the current hashcode of this map.
		
		return null;
	}
	
	public IValue remove(Object object){
		IValue key = (IValue) object;
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue, IValue> currentStartEntry = data[position];
		if(currentStartEntry != null){
			Entry<IValue, IValue> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.key.isEqual(key)){
					Entry<IValue, IValue> e = data[position];
					
					data[position] = entry.next;
					// Reconstruct the other entries (if necessary).
					while(e != entry){
						data[position] = new Entry<>(e.hash, e.key, e.value, data[position]);
						
						e = e.next;
					}
					
					load--;
					
					currentHashCode ^= hash; // Update the current hashcode of this map.
					
					return entry.value; // Return the value.
				}
				
				entry = entry.next;
			}while(entry != null);
		}
		
		return null; // Not found.
	}
	
	public IValue get(Object object){
		IValue key = (IValue) object;
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue, IValue> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && key.isEqual(entry.key)) return entry.value;
			
			entry = entry.next;
		}
		
		return null;
	}
	
	public boolean contains(IValue key){
		return (get(key) != null);
	}
	
	public int size(){
		return load;
	}
	
	public boolean isEmpty(){
		return (load == 0);
	}
	
	public Iterator<Map.Entry<IValue, IValue>> entryIterator(){
		return new EntryIterator(data);
	}
	
	public Iterator<IValue> keysIterator(){
		return new KeysIterator(data);
	}
	
	public Iterator<IValue> valuesIterator(){
		return new ValuesIterator(data);
	}
	
	@SuppressWarnings("unchecked")
	public void putAll(Map<? extends IValue, ? extends IValue> otherMap){
		Set<Map.Entry<IValue, IValue>> entrySet = (Set<Map.Entry<IValue, IValue>>) (Set<?>) otherMap.entrySet(); // Generics stink.
		Iterator<Map.Entry<IValue, IValue>> entrySetIterator = entrySet.iterator();
		while(entrySetIterator.hasNext()){
			Map.Entry<IValue, IValue> next = entrySetIterator.next();
			put(next.getKey(), next.getValue());
		}
	}
	
	public boolean containsKey(Object object){
		IValue key = (IValue) object;
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue, IValue> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && key.isEqual(entry.key)) return true;
			
			entry = entry.next;
		}
		
		return false;
	}
	
	public boolean containsValue(Object object){
		IValue value = (IValue) object;
		
		Iterator<IValue> valuesIterator = valuesIterator();
		while(valuesIterator.hasNext()){
			IValue nextValue = valuesIterator.next();
			if(nextValue == value || (nextValue != null && nextValue.isEqual(value))){
				return true;
			}
		}

		return false;
	}
	
	public Set<Map.Entry<IValue, IValue>> entrySet(){
		ShareableHashSet<Map.Entry<IValue, IValue>> entrySet = new ShareableHashSet<>();
		
		Iterator<Map.Entry<IValue, IValue>> entriesIterator = entryIterator();
		while(entriesIterator.hasNext()){
			entrySet.add(entriesIterator.next());
		}
		
		return entrySet;
	}
	
	public Set<IValue> keySet(){
		ShareableHashSet<IValue> keysSet = new ShareableHashSet<>();
		
		Iterator<IValue> keysIterator = keysIterator();
		while(keysIterator.hasNext()){
			keysSet.add(keysIterator.next());
		}
		
		return keysSet;
	}
	
	public Collection<IValue> values(){
		ShareableHashSet<IValue> valuesSet = new ShareableHashSet<>();
		
		Iterator<IValue> valuesIterator = valuesIterator();
		while(valuesIterator.hasNext()){
			valuesSet.add(valuesIterator.next());
		}
		
		return valuesSet;
	}
	
	public String toString(){
		StringBuilder buffer = new StringBuilder();
		
		buffer.append('{');
		for(int i = 0; i < data.length; i++){
			buffer.append('[');
			Entry<IValue, IValue> e = data[i];
			if(e != null){
				buffer.append(e);
				
				e = e.next;
				
				while(e != null){
					buffer.append(',');
					buffer.append(e);
					
					e = e.next;
				}
			}
			buffer.append(']');
		}
		buffer.append('}');
		
		return buffer.toString();
	}
	
	public int hashCode(){
		return currentHashCode;
	}
	
	public boolean isEqual(ShareableValuesHashMap other){
		if(other == null) {
			return false;
		}
		if(other.currentHashCode != currentHashCode) { 
			return false;
		}
		if(other.size() != size()) {
			return false;
		}
		if(isEmpty()) {
			return true; // No need to check if the maps are empty.
		}
		
		Iterator<Map.Entry<IValue, IValue>> otherIterator = other.entryIterator();
		while (otherIterator.hasNext()) {
			Map.Entry<IValue, IValue> entry = otherIterator.next();
			IValue otherValue = entry.getValue();
			IValue thisValue = get(entry.getKey());
			
			if (thisValue == null) {
				// Means the key from other is not present in this
				return false; 
			}
			
			if(otherValue != thisValue 
					&& !thisValue.isEqual(otherValue)) {
				return false;
			}
		}
		
		return true;
	}
	
	private IValue getTruelyEqual(IValue key){
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue, IValue> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && key.equals(entry.key)) return entry.value;
			
			entry = entry.next;
		}
		
		return null;
	}
	
	public boolean equals(Object o){
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			ShareableValuesHashMap other = (ShareableValuesHashMap) o;
			
			if(other.currentHashCode != currentHashCode) return false;
			if(other.size() != size()) return false;
		
			if(isEmpty()) return true; // No need to check if the maps are empty.
			
			Iterator<Map.Entry<IValue, IValue>> otherIterator = other.entryIterator();
			while(otherIterator.hasNext()){
				Map.Entry<IValue, IValue> entry = otherIterator.next();
				IValue otherValue = entry.getValue();
				IValue thisValue = getTruelyEqual(entry.getKey());
				
				if(otherValue != thisValue && (thisValue == null || !thisValue.equals(otherValue))) return false;
			}
			return true;
		}
		
		return false;
	}
	
	private static class Entry<K, V> implements Map.Entry<K, V>{
		public final int hash;
		public final K key;
		public final V value;
		
		public final Entry<K, V> next;
		
		public Entry(int hash, K key, V value, Entry<K, V> next){
			super();
			
			this.hash = hash;
			this.key = key;
			this.value = value;
			
			this.next = next;
		}
		
		public K getKey(){
			return key;
		}
		
		public V getValue(){
			return value;
		}
		
		public V setValue(V value){
			throw new UnsupportedOperationException("The setting of values is not supported by this map implementation.");
		}
		
		public String toString(){
			StringBuilder buffer = new StringBuilder();
			
			buffer.append('<');
			buffer.append(key);
			buffer.append(':');
			buffer.append(value);
			buffer.append('>');
			
			return buffer.toString();
		}
	}
	
	private static class EntryIterator implements Iterator<Map.Entry<IValue, IValue>>{
		private final Entry<IValue, IValue>[] data;
		
		private Entry<IValue, IValue> current;
		private int index;
		
		public EntryIterator(Entry<IValue, IValue>[] entries){
			super();
			
			data = entries;

			index = data.length - 1;
			current = new Entry<>(0, null, null, data[index]);
			locateNext();
		}
		
		private void locateNext(){
			Entry<IValue, IValue> next = current.next;
			if(next != null){
				current = next;
				return;
			}
			
			for(int i = index - 1; i >= 0 ; i--){
				Entry<IValue, IValue> entry = data[i];
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
		
		public Entry<IValue, IValue> next(){
			if(!hasNext()) throw new UnsupportedOperationException("There are no more elements in this iterator.");
			
			Entry<IValue, IValue> entry = current;
			locateNext();
			
			return entry;
		}
		
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
	
	private static class KeysIterator implements Iterator<IValue>{
		private final EntryIterator entryIterator;
		
		public KeysIterator(Entry<IValue, IValue>[] entries){
			super();
			
			entryIterator = new EntryIterator(entries);
		}
		
		public boolean hasNext(){
			return entryIterator.hasNext();
		}
		
		public IValue next(){
			return entryIterator.next().key;
		}
		
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
	
	private static class ValuesIterator implements Iterator<IValue>{
		private final EntryIterator entryIterator;
		
		public ValuesIterator(Entry<IValue,IValue>[] entries){
			super();
			
			entryIterator = new EntryIterator(entries);
		}
		
		public boolean hasNext(){
			return entryIterator.hasNext();
		}
		
		public IValue next(){
			return entryIterator.next().value;
		}
		
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
}
