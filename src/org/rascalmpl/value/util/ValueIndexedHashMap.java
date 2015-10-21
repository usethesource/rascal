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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashSet;

/**
 * This map is similar to the ShareableHashMap, but is indexed by a value (check with isEqual).
 * 
 * @author Arnold Lankamp
 *
 * @param <V>
 *          The value type
 */
public final class ValueIndexedHashMap<V> implements Map<IValue, V>{
	private final static int INITIAL_LOG_SIZE = 4;

	private int modSize;
	private int hashMask;
	
	private Entry<V>[] data;
	
	private int threshold;
	
	private int load;
	
	/**
	 * Constructor.
	 */
	public ValueIndexedHashMap(){
		super();
		
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<V>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param valueIndexedHashMap
	 *            The map to copy.
	 */
	public ValueIndexedHashMap(ValueIndexedHashMap<V> valueIndexedHashMap){
		super();
		
		modSize = valueIndexedHashMap.modSize;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = valueIndexedHashMap.data.clone();
		
		threshold = tableSize;
		
		load = valueIndexedHashMap.load;
	}
	
	/**
	 * Removes all the entries from this map.
	 */
	public void clear(){
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<V>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
	}
	
	/**
	 * Rehashes this map.
	 */
	private void rehash(){
		modSize++;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		Entry<V>[] newData = (Entry<V>[]) new Entry[tableSize];

		threshold = tableSize;
		
		Entry<V>[] oldData = data;
		for(int i = oldData.length - 1; i >= 0; i--){
			Entry<V> entry = oldData[i];
			
			if(entry != null){
				// Determine the last unchanged entry chain.
				Entry<V> lastUnchangedEntryChain = entry;
				int newLastUnchangedEntryChainIndex = entry.hash & hashMask;
				
				Entry<V> e = entry.next;
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
	
	/**
	 * Makes sure the size of the entry array and the load of the map stay in proper relation to
	 * eachother.
	 */
	private void ensureCapacity(){
		if(load > threshold){
			rehash();
		}
	}
	
	/**
	 * Replaces the value in the entry by the given value.
	 * 
	 * @param position
	 *            The position in the entry array where the entry is located.
	 * @param entry
	 *            The entry in which the value must be replaced.
	 * @param newValue
	 *            The value.
	 */
	private void replaceValue(int position, Entry<V> entry, V newValue){
		Entry<V> e = data[position];
		
		// Reconstruct the updated entry.
		data[position] = new Entry<>(entry.hash, entry.key, newValue, entry.next);
		
		// Reconstruct the other entries (if necessary).
		while(e != entry){
			data[position] = new Entry<>(e.hash, e.key, e.value, data[position]);
			
			e = e.next;
		}
	}
	
	/**
	 * Inserts the given key-value pair into this map. In case there already is a value associated
	 * with the given key, the value will be updated and the previous value returned.
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            The value
	 * @return The previous value that was associated with the key (if any); null otherwise.
	 */
	public V put(IValue key, V value){
		ensureCapacity();
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<V> currentStartEntry = data[position];
		// Check if the key is already in here.
		if(currentStartEntry != null){
			Entry<V> entry = currentStartEntry;
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
		
		return null;
	}
	
	/**
	 * Removes the entry from this map that is identified by the given key (if present).
	 * 
	 * @param key
	 *            The key that identifies the entry to remove.
	 * @return The value that was associated with the given key; null if the key was not present in
	 * the map.
	 */
	public V remove(Object object){
		IValue key = (IValue) object;
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<V> currentStartEntry = data[position];
		if(currentStartEntry != null){
			Entry<V> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.key.isEqual(key)){
					Entry<V> e = data[position];
					
					data[position] = entry.next;
					// Reconstruct the other entries (if necessary).
					while(e != entry){
						data[position] = new Entry<>(e.hash, e.key, e.value, data[position]);
						
						e = e.next;
					}
					
					load--;
					
					return entry.value; // Return the value.
				}
				
				entry = entry.next;
			}while(entry != null);
		}
		
		return null; // Not found.
	}
	
	/**
	 * Retrieves the value from the entry in this map which is identified by the given key
	 * (if present).
	 * 
	 * @param key
	 *            The key that identifies the entry that contains the value.
	 * @return The retrieved value; null if not present.
	 */
	public V get(Object object){
		IValue key = (IValue) object;
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<V> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && key.isEqual(entry.key)) return entry.value;
			
			entry = entry.next;
		}
		
		return null;
	}
	
	/**
	 * Checks if there is an entry present in this map, which is identified by the given key.
	 * 
	 * @param key
	 *            The key that identifies the entry.
	 * @return True if this map contains an entry which is identified by the given key;
	 * false otherwise.
	 */
	public boolean contains(IValue key){
		return (get(key) != null);
	}
	
	/**
	 * Returns the number of entries this map contains.
	 * 
	 * @return The number of entries this map contains.
	 */
	public int size(){
		return load;
	}
	
	/**
	 * Checks whether or not this map is empty.
	 * 
	 * @return True if this map was empty; false otherwise.
	 */
	public boolean isEmpty(){
		return (load == 0);
	}
	
	/**
	 * Constructs an iterator for the entries in this map.
	 * 
	 * @return An iterator for the entries in this map.
	 */
	public Iterator<Map.Entry<IValue, V>> entryIterator(){
		return new EntryIterator<>(data);
	}
	
	/**
	 * Constructs an iterator for the keys in this map.
	 * 
	 * @return An iterator for the keys in this map.
	 */
	public Iterator<IValue> keysIterator(){
		return new KeysIterator<>(data);
	}
	
	/**
	 * Constructs an iterator for the values in this map.
	 * 
	 * @return An iterator for the values in this map.
	 */
	public Iterator<V> valuesIterator(){
		return new ValuesIterator<>(data);
	}
	
	/**
	 * Copies over all entries from the given map, to this map.
	 */
	public void putAll(Map<? extends IValue, ? extends V> otherMap){
		Set<Map.Entry<IValue, V>> entrySet = (Set<Map.Entry<IValue, V>>) (Set<?>) otherMap.entrySet(); // Generics stink.
		Iterator<Map.Entry<IValue, V>> entrySetIterator = entrySet.iterator();
		while(entrySetIterator.hasNext()){
			Map.Entry<IValue, V> next = entrySetIterator.next();
			put(next.getKey(), next.getValue());
		}
	}
	
	/**
	 * Checks if this map contains an entry with the given key.
	 */
	public boolean containsKey(Object object){
		IValue key = (IValue) object;
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<V> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && key.isEqual(entry.key)) return true;
			
			entry = entry.next;
		}
		
		return false;
	}
	
	/**
	 * Checks if this map contains an entry with the given value.
	 */
	public boolean containsValue(Object value){
		Iterator<V> valuesIterator = valuesIterator();
		while(valuesIterator.hasNext()){
			V nextValue = valuesIterator.next();
			if(nextValue == value || (nextValue != null && nextValue.equals(value))){
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Constructs a set containing all entries from this map.
	 */
	public Set<Map.Entry<IValue, V>> entrySet(){
		ShareableHashSet<Map.Entry<IValue, V>> entrySet = new ShareableHashSet<>();
		
		Iterator<Map.Entry<IValue, V>> entriesIterator = entryIterator();
		while(entriesIterator.hasNext()){
			entrySet.add(entriesIterator.next());
		}
		
		return entrySet;
	}
	
	/**
	 * Constructs a set containing all keys from this map.
	 */
	public Set<IValue> keySet(){
		ShareableValuesHashSet keysSet = new ShareableValuesHashSet();
		
		Iterator<IValue> keysIterator = keysIterator();
		while(keysIterator.hasNext()){
			keysSet.add(keysIterator.next());
		}
		
		return keysSet;
	}
	
	/**
	 * Constructs a collection containing all values from this map.
	 */
	public Collection<V> values(){
		ShareableHashSet<V> valuesSet = new ShareableHashSet<>();
		
		Iterator<V> valuesIterator = valuesIterator();
		while(valuesIterator.hasNext()){
			valuesSet.add(valuesIterator.next());
		}
		
		return valuesSet;
	}
	
	/**
	 * Prints the internal representation of this map to a string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder buffer = new StringBuilder();
		
		buffer.append('{');
		for(int i = 0; i < data.length; i++){
			buffer.append('[');
			Entry<V> e = data[i];
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
	
	/**
	 * Returns the current hash code of this map.
	 * 
	 * @return The current hash code of this map.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode(){
		int hash = 0;
		
		Iterator<IValue> keysIterator = keysIterator();
		while(keysIterator.hasNext()){
			hash ^= keysIterator.next().hashCode();
		}
		
		return hash;
	}
	
	/**
	 * Check whether or not the current content of this set is equal to that of the given object / map. 
	 * 
	 * @return True if the content of this set is equal to the given object / map.
	 * 
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o){
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			ValueIndexedHashMap<V> other = (ValueIndexedHashMap<V>) o;
			
			if(other.size() != size()) return false;
		
			if(isEmpty()) return true; // No need to check if the maps are empty.
			
			Iterator<Map.Entry<IValue, V>> otherIterator = other.entryIterator();
			while(otherIterator.hasNext()){
				Map.Entry<IValue, V> entry = otherIterator.next();
				V otherValue = entry.getValue();
				V thisValue = get(entry.getKey());
				if(otherValue != thisValue && thisValue != null && !thisValue.equals(entry.getValue())) return false;
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * Entry, used for containing key-value pairs and constructing buckets.
	 *  
	 * @author Arnold Lankamp
	 *
	 * @param <K>
	 *            The key type
	 * @param <V>
	 *            The value type
	 */
	private static class Entry<V> implements Map.Entry<IValue, V>{
		public final int hash;
		public final IValue key;
		public final V value;
		
		public final Entry<V> next;
		
		/**
		 * Constructor
		 * 
		 * @param hash
		 *            The hash code of the key
		 * @param key
		 *            The key
		 * @param value
		 *            The value
		 * @param next
		 *            A reference to the next entry in the bucket (if any).
		 */
		public Entry(int hash, IValue key, V value, Entry<V> next){
			super();
			
			this.hash = hash;
			this.key = key;
			this.value = value;
			
			this.next = next;
		}
		
		/**
		 * Returns a reference to the key.
		 * 
		 * @return A reference to the key.
		 */
		public IValue getKey(){
			return key;
		}
		
		/**
		 * Returns a reference to the value.
		 * 
		 * @return A reference to the value.
		 */
		public V getValue(){
			return value;
		}
		
		/**
		 * Unsupported operation.
		 * 
		 * @param value
		 *        The value which we will not set.
		 * @return Null.
		 * @throws java.lang.UnsupportedOperationException
		 * 
		 * @see java.util.Map.Entry#setValue(Object)
		 */
		public V setValue(V value){
			throw new UnsupportedOperationException("The setting of values is not supported by this map implementation.");
		}

		/**
		 * Prints the internal representation of this entry to a string.
		 * 
		 * @see java.lang.Object#toString()
		 */
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
	
	/**
	 * Iterator for entries.
	 * 
	 * @author Arnold Lankamp
	 *
	 * @param <K>
	 *            The key type.
	 * @param <V>
	 *            The value type.
	 */
	private static class EntryIterator<V> implements Iterator<Map.Entry<IValue, V>>{
		private final Entry<V>[] data;
		
		private Entry<V> current;
		private int index;
		
		/**
		 * Constructor.
		 * 
		 * @param entries
		 *            The entries to iterator over.
		 */
		public EntryIterator(Entry<V>[] entries){
			super();
			
			data = entries;

			index = data.length - 1;
			current = new Entry<>(0, null, null, data[index]);
			locateNext();
		}
		
		/**
		 * Locates the next entry in the map.
		 */
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
		
		/**
		 * Check if there are more elements in this iteration.
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext(){
			return (current != null);
		}
		
		/**
		 * Returns the next element in this iteration.
		 * 
		 * @return The next element in this iteration.
		 * @throws NoSuchElementException
		 *            Thrown if there are no more elements in this iteration when calling this
		 *            method.
		 * 
		 * @see java.util.Iterator#next()
		 */
		public Entry<V> next(){
			if(!hasNext()) throw new UnsupportedOperationException("There are no more elements in this iterator.");
			
			Entry<V> entry = current;
			locateNext();
			
			return entry;
		}
		
		/**
		 * Removal is not supported by this iterator.
		 * 
		 * @throws java.lang.UnsupportedOperationException
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
	
	/**
	 * Iterator for keys.
	 * 
	 * @author Arnold Lankamp
	 *
	 * @param <K>
	 *            The key type.
	 * @param <V>
	 *            The value type.
	 */
	private static class KeysIterator<V> implements Iterator<IValue>{
		private final EntryIterator<V> entryIterator;
		
		/**
		 * Constructor.
		 * 
		 * @param entries
		 *            The entries to iterate over.
		 */
		public KeysIterator(Entry<V>[] entries){
			super();
			
			entryIterator = new EntryIterator<>(entries);
		}
		
		/**
		 * Check if there are more elements in this iteration.
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext(){
			return entryIterator.hasNext();
		}
		
		/**
		 * Returns the next element in this iteration.
		 * 
		 * @return The next element in this iteration.
		 * @throws NoSuchElementException
		 *            Thrown if there are no more elements in this iteration when calling this
		 *            method.
		 * 
		 * @see java.util.Iterator#next()
		 */
		public IValue next(){
			return entryIterator.next().key;
		}
		
		/**
		 * Removal is not supported by this iterator.
		 * 
		 * @throws java.lang.UnsupportedOperationException
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}

	/**
	 * Iterator for values.
	 * 
	 * @author Arnold Lankamp
	 *
	 * @param <K>
	 *            The key type.
	 * @param <V>
	 *            The value type.
	 */
	private static class ValuesIterator<V> implements Iterator<V>{
		private final EntryIterator<V> entryIterator;
		
		/**
		 * Constructor.
		 * 
		 * @param entries
		 *            The entries to iterate over.
		 */
		public ValuesIterator(Entry<V>[] entries){
			super();
			
			entryIterator = new EntryIterator<>(entries);
		}
		
		/**
		 * Check if there are more elements in this iteration.
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext(){
			return entryIterator.hasNext();
		}
		
		/**
		 * Returns the next element in this iteration.
		 * 
		 * @return The next element in this iteration.
		 * @throws NoSuchElementException
		 *            Thrown if there are no more elements in this iteration when calling this
		 *            method.
		 * 
		 * @see java.util.Iterator#next()
		 */
		public V next(){
			return entryIterator.next().value;
		}
		
		/**
		 * Removal is not supported by this iterator.
		 * 
		 * @throws java.lang.UnsupportedOperationException
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
}
