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
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This set implementation is shareable and can be easily cloned
 * (simple arraycopy of the entries array).
 * 
 * @author Arnold Lankamp
 *
 * @param <V>
 *            The value type.
 */
public final class ShareableHashSet<V> implements Set<V>, Iterable<V>{
	private final static int INITIAL_LOG_SIZE = 4;

	private int modSize;
	private int hashMask;
	
	private Entry<V>[] data;
	
	private int threshold;
	
	private int load;
	
	private int currentHashCode;
	
	/**
	 * Default constructor.
	 */
	public ShareableHashSet(){
		super();
		
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<V>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
		
		currentHashCode = 0;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param sharedHashSet
	 *            The set to copy.
	 */
	public ShareableHashSet(ShareableHashSet<V> sharedHashSet){
		super();
		
		modSize = sharedHashSet.modSize;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = sharedHashSet.data.clone();
		
		threshold = tableSize;
		
		load = sharedHashSet.load;
		
		currentHashCode = sharedHashSet.currentHashCode;
	}
	
	/**
	 * Removes all the entries from this set.
	 */
	public void clear(){
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<V>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
		
		currentHashCode = 0;
	}
	
	/**
	 * Rehashes this set.
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
					newData[position] = new Entry<>(hash, entry.value, newData[position]);
					
					entry = entry.next;
				}
			}
		}
		
		data = newData;
	}
	
	/**
	 * Makes sure the size of the entry array and the load of the set stay in proper relation to
	 * eachother.
	 */
	private void ensureCapacity(){
		if(load > threshold){
			rehash();
		}
	}
	
	/**
	 * Inserts the given value into this set.
	 * 
	 * @param value
	 *            The value to insert.
	 * @return Returns true if this set didn't contain the given value yet; false if it did.
	 */
	public boolean add(V value){
		ensureCapacity();
		
		int hash = value.hashCode();
		int position = hash & hashMask;
		
		Entry<V> currentStartEntry = data[position];
		// Check if the value is already in here.
		if(currentStartEntry != null){
			Entry<V> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.value.equals(value)){
					return false; // Return false if it's already present.
				}
				
				entry = entry.next;
			}while(entry != null);
		}
		
		data[position] = new Entry<>(hash, value, currentStartEntry); // Insert the new entry.
		
		load++;
		
		currentHashCode ^= hash; // Update the current hashcode of this map.
		
		return true;
	}
	
	/**
	 * Checks if this set contains the given value.
	 * 
	 * @param value
	 *            The value to check for.
	 * @return True if this set contains the given value; false otherwise.
	 */
	public boolean contains(Object value){
		int hash = value.hashCode();
		int position = hash & hashMask;
		
		Entry<V> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && value.equals(entry.value)) return true;
			
			entry = entry.next;
		}
		
		return false;
	}
	
	/**
	 * Removes the given object from this set (if present.)
	 * 
	 * @param value
	 *            The value to remove.
	 * @return True if this set contained the given object; false otherwise.
	 */
	public boolean remove(Object value){
		int hash = value.hashCode();
		int position = hash & hashMask;
		
		Entry<V> currentStartEntry = data[position];
		if(currentStartEntry != null){
			Entry<V> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.value.equals(value)){
					Entry<V> e = data[position];
					
					data[position] = entry.next;
					// Reconstruct the other entries (if necessary).
					while(e != entry){
						data[position] = new Entry<>(e.hash, e.value, data[position]);
						
						e = e.next;
					}
					
					load--;
					
					currentHashCode ^= hash; // Update the current hashcode of this set.
					
					return true;
				}
				
				entry = entry.next;
			}while(entry != null);
		}
		
		return false;
	}
	
	/**
	 * Returns the number of values this set currently contains.
	 * 
	 * @return The number of values this set currently contains.
	 */
	public int size(){
		return load;
	}
	
	/**
	 * Checks whether or not this set is empty.
	 * 
	 * @return True is this set is empty; false otherwise.
	 */
	public boolean isEmpty(){
		return (load == 0);
	}
	
	/**
	 * Constructs an iterator for this set.
	 * 
	 * @return An iterator for this set.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<V> iterator(){
		return new SetIterator<>(data);
	}
	
	/**
	 * Adds all the elements from the given collection to this set.
	 * 
	 * @param collection
	 *            The collection that contains the elements to add.
	 * @return True if this set changed; false if it didn't.
	 */
	public boolean addAll(Collection<? extends V> collection){
		boolean changed = false;
		
		Iterator<? extends V> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			changed |= add(collectionIterator.next());
		}
		
		return changed;
	}
	
	/**
	 * Checks if the collection contains all the elements in the given collection.
	 * 
	 * @param collection
	 *            The collection that contains the elements to check for.
	 * @return True if this set contains all the elements in the given collections; false if it
	 * didn't.
	 */
	public boolean containsAll(Collection<?> collection){
		Iterator<?> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			if(!contains(collectionIterator.next())) return false;
		}
		
		return true;
	}
	
	/**
	 * Removes all the elements from this set which are not present in the given collection.
	 * 
	 * @param collection
	 *            The collection that contains the elements which need to be retained.
	 * @return True if this set changed; false if it didn't.
	 */
	public boolean retainAll(Collection<?> collection){
		boolean changed = false;
		
		Iterator<V> valuesIterator = iterator();
		while(valuesIterator.hasNext()){
			V value = valuesIterator.next();
			if(!collection.contains(value)){
				remove(value);
				
				changed = true;
			}
		}
		
		return changed;
	}
	
	/**
	 * Removes all the elements in the given collection from this set.
	 * 
	 * @param collection
	 *            The collection that contains the elements to remove from this set.
	 * @return True if this set change; false if it didn't.
	 */
	public boolean removeAll(Collection<?> collection){
		boolean changed = false;
		
		Iterator<?> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			Object value = collectionIterator.next();
			changed |= remove(value);
		}
		
		return changed;
	}
	
	/**
	 * Returns all the elements from this set in an array.
	 * 
	 * @return All the elements from this set in an array.
	 */
	public Object[] toArray(){
		Object[] values = new Object[load];
		
		Iterator<V> valuesIterator = iterator();
		int i = 0;
		while(valuesIterator.hasNext()){
			values[i++] = valuesIterator.next();
		}
		
		return values;
	}
	

	/**
	 * Returns all the elements from this set in an array.
	 * 
	 * @param array
	 *            The array to use; in case it isn't large enough a new one will be allocated.
	 * @return All the elements from this set in an array.
	 */
	public <T> T[] toArray(T[] array){
		if(array.length < load) return (T[]) toArray();
		
		Iterator<V> valuesIterator = iterator();
		int i = 0;
		while(valuesIterator.hasNext()){
			array[i++] = (T) valuesIterator.next();
		}
		
		for(; i < load; i++){
			array[i] = null;
		}
		
		return array;
	}
	
	/**
	 * Prints the internal representation of this set to a string.
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
	 * Returns the current hash code of this set.
	 * 
	 * @return The current hash code of this set.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode(){
		return currentHashCode;
	}

	/**
	 * Check whether or not the current content of this set is equal to that of the given object / set. 
	 * 
	 * @return True if the content of this set is equal to the given object / set.
	 * 
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o){
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			ShareableHashSet<V> other = (ShareableHashSet<V>) o;
			
			if(other.currentHashCode != currentHashCode) return false;
			if(other.size() != size()) return false;
			
			if(isEmpty()) return true; // No need to check if the sets are empty.
			
			Iterator<V> otherIterator = other.iterator();
			while(otherIterator.hasNext()){
				if(!contains(otherIterator.next())) return false;
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * Entry, used for containing values and constructing buckets.
	 * 
	 * @author Arnold Lankamp
	 *
	 * @param <V>
	 *            The value type.
	 */
	private static class Entry<V>{
		public final int hash;
		public final V value;
		
		public final Entry<V> next;
		
		/**
		 * Constructor.
		 * 
		 * @param hash
		 *            The hash code of the value
		 * @param value
		 *            The value
		 * @param next
		 *            A reference to the next entry in the bucket (if any).
		 */
		public Entry(int hash, V value, Entry<V> next){
			super();
			
			this.hash = hash;
			this.value = value;
			
			this.next = next;
		}
		
		/**
		 * Prints the internal representation of this entry to a string.
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString(){
			StringBuilder buffer = new StringBuilder();
			
			buffer.append('<');
			buffer.append(value);
			buffer.append('>');
			
			return buffer.toString();
		}
	}
	
	/**
	 * Iterator for this set.
	 * 
	 * @author Arnold Lankamp
	 *
	 * @param <V>
	 *            The value type.
	 */
	private static class SetIterator<V> implements Iterator<V>{
		private final Entry<V>[] data;
		
		private Entry<V> current;
		private int index;
		
		/**
		 * Constructor.
		 * 
		 * @param entries
		 *            The entries to iterator over.
		 */
		public SetIterator(Entry<V>[] entries){
			super();
			
			data = entries;

			index = data.length - 1;
			current = new Entry<>(0, null, data[index]);
			locateNext();
		}
		
		/**
		 * Locates the next value in the set.
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
		 * Checks if there are more elements in this iteration.
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
		public V next(){
			if(!hasNext()) throw new NoSuchElementException("There are no more elements in this iteration");
			
			V value = current.value;
			locateNext();
			
			return value;
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
