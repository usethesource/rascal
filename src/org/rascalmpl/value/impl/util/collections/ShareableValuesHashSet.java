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
import java.util.NoSuchElementException;
import java.util.Set;

import org.rascalmpl.value.IValue;

/**
 * A specialized version of the ShareableSet, specifically meant for storing values.
 * 
 * @author Arnold Lankamp
 */
public final class ShareableValuesHashSet implements Set<IValue>, Iterable<IValue>{
	private final static int INITIAL_LOG_SIZE = 4;

	private int modSize;
	private int hashMask;
	
	private Entry<IValue>[] data;
	
	private int threshold;
	
	private int load;
	
	private int currentHashCode;
	
	@SuppressWarnings("unchecked")
	public ShareableValuesHashSet(){
		super();
		
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<IValue>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
		
		currentHashCode = 0;
	}
	
	public ShareableValuesHashSet(ShareableValuesHashSet shareableValuesHashSet){
		super();
		
		modSize = shareableValuesHashSet.modSize;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = shareableValuesHashSet.data.clone();
		
		threshold = tableSize;
		
		load = shareableValuesHashSet.load;
		
		currentHashCode = shareableValuesHashSet.currentHashCode;
	}
	
	@SuppressWarnings("unchecked")
	public void clear(){
		modSize = INITIAL_LOG_SIZE;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		data = (Entry<IValue>[]) new Entry[tableSize];
		
		threshold = tableSize;
		
		load = 0;
		
		currentHashCode = 0;
	}
	
	@SuppressWarnings("unchecked")
	private void rehash(){
		modSize++;
		int tableSize = 1 << modSize;
		hashMask = tableSize - 1;
		Entry<IValue>[] newData = (Entry<IValue>[]) new Entry[tableSize];

		threshold = tableSize;
		
		Entry<IValue>[] oldData = data;
		for(int i = oldData.length - 1; i >= 0; i--){
			Entry<IValue> entry = oldData[i];
			
			if(entry != null){
				// Determine the last unchanged entry chain.
				Entry<IValue> lastUnchangedEntryChain = entry;
				int newLastUnchangedEntryChainIndex = entry.hash & hashMask;
				
				Entry<IValue> e = entry.next;
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
	
	private void ensureCapacity(){
		if(load > threshold){
			rehash();
		}
	}
	
	public boolean add(IValue value){
		ensureCapacity();
		
		int hash = value.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue> currentStartEntry = data[position];
		// Check if the value is already in here.
		if(currentStartEntry != null){
			Entry<IValue> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.value.isEqual(value)){
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
	
	public boolean contains(Object object){
		IValue value = (IValue) object;
		
		int hash = value.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && value.isEqual(entry.value)) return true;
			
			entry = entry.next;
		}
		
		return false;
	}
	
	public boolean remove(Object object){
		IValue value = (IValue) object;
		
		int hash = value.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue> currentStartEntry = data[position];
		if(currentStartEntry != null){
			Entry<IValue> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.value.isEqual(value)){
					Entry<IValue> e = data[position];
					
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
	
	public int size(){
		return load;
	}
	
	public boolean isEmpty(){
		return (load == 0);
	}
	
	public Iterator<IValue> iterator(){
		return new SetIterator(data);
	}
	
	public boolean addAll(Collection<? extends IValue> collection){
		boolean changed = false;
		
		Iterator<? extends IValue> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			changed |= add(collectionIterator.next());
		}
		
		return changed;
	}

	public boolean containsAll(Collection<?> collection){
		Iterator<?> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			if(!contains(collectionIterator.next())) return false;
		}
		
		return true;
	}

	public boolean retainAll(Collection<?> collection){
		boolean changed = false;
		
		Iterator<IValue> valuesIterator = iterator();
		while(valuesIterator.hasNext()){
			IValue value = valuesIterator.next();
			if(!collection.contains(value)){
				remove(value);
				
				changed = true;
			}
		}
		
		return changed;
	}
	
	public boolean removeAll(Collection<?> collection){
		boolean changed = false;
		
		Iterator<?> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			Object value = collectionIterator.next();
			changed |= remove(value);
		}
		
		return changed;
	}

	public Object[] toArray(){
		Object[] values = new Object[load];
		
		Iterator<IValue> valuesIterator = iterator();
		int i = 0;
		while(valuesIterator.hasNext()){
			values[i++] = valuesIterator.next();
		}
		
		return values;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] array){
		if(array.length < load) return (T[]) toArray();
		
		Iterator<IValue> valuesIterator = iterator();
		int i = 0;
		while(valuesIterator.hasNext()){
			array[i++] = (T) valuesIterator.next();
		}
		
		for(; i < load; i++){
			array[i] = null;
		}
		
		return array;
	}
	
	public String toString(){
		StringBuilder buffer = new StringBuilder();
		
		buffer.append('{');
		for(int i = 0; i < data.length; i++){
			buffer.append('[');
			Entry<IValue> e = data[i];
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
	
	public boolean isEqual(ShareableValuesHashSet other){
		if(other == null) return false;
		
		if(other.currentHashCode != currentHashCode) return false;
		if(other.size() != size()) return false;
		
		if(isEmpty()) return true; // No need to check if the sets are empty.
		
		Iterator<IValue> otherIterator = other.iterator();
		while(otherIterator.hasNext()){
			if(!contains(otherIterator.next())) return false;
		}
		return true;
	}
	
	private boolean containsTruelyEqual(IValue value){
		int hash = value.hashCode();
		int position = hash & hashMask;
		
		Entry<IValue> entry = data[position];
		while(entry != null){
			if(hash == entry.hash && value.equals(entry.value)) return true;
			
			entry = entry.next;
		}
		
		return false;
	}
	
	public boolean equals(Object o){
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			ShareableValuesHashSet other = (ShareableValuesHashSet) o;
			
			if(other.currentHashCode != currentHashCode) return false;
			if(other.size() != size()) return false;
			
			if(isEmpty()) return true; // No need to check if the sets are empty.
			
			Iterator<IValue> otherIterator = other.iterator();
			while(otherIterator.hasNext()){
				if(!containsTruelyEqual(otherIterator.next())) return false;
			}
			return true;
		}
		
		return false;
	}
	
	private static class Entry<V>{
		public final int hash;
		public final V value;
		
		public final Entry<V> next;
		
		public Entry(int hash, V value, Entry<V> next){
			super();
			
			this.hash = hash;
			this.value = value;
			
			this.next = next;
		}
		
		public String toString(){
			StringBuilder buffer = new StringBuilder();
			
			buffer.append('<');
			buffer.append(value);
			buffer.append('>');
			
			return buffer.toString();
		}
	}
	
	private static class SetIterator implements Iterator<IValue>{
		private final Entry<IValue>[] data;
		
		private Entry<IValue> current;
		private int index;
		
		public SetIterator(Entry<IValue>[] entries){
			super();
			
			data = entries;

			index = data.length - 1;
			current = new Entry<>(0, null, data[index]);
			locateNext();
		}
		
		private void locateNext(){
			Entry<IValue> next = current.next;
			if(next != null){
				current = next;
				return;
			}
			
			for(int i = index - 1; i >= 0 ; i--){
				Entry<IValue> entry = data[i];
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
		
		public IValue next(){
			if(!hasNext()) throw new NoSuchElementException("There are no more elements in this iteration");
			
			IValue value = current.value;
			locateNext();
			
			return value;
		}
		
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
}
