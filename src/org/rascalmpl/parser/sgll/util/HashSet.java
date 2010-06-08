package org.rascalmpl.parser.sgll.util;

public class HashSet<E>{
	private Entry<E>[] entries;

	private int hashMask;
	private int bitSize;
	
	private int threshold;
	private int load;
	
	public HashSet(){
		super();
		
		int nrOfEntries = 1 << bitSize;
		
		hashMask = nrOfEntries - 1;
		
		entries = (Entry<E>[]) new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	private void rehash(){
		int nrOfEntries = 1 << (++bitSize);
		int newHashMask = nrOfEntries - 1;
		
		Entry<E>[] oldEntries = entries;
		Entry<E>[] newEntries = (Entry<E>[]) new Entry[nrOfEntries];
		
		Entry<E> currentEntryRoot = new Entry<E>(null, 0, null);
		Entry<E> shiftedEntryRoot = new Entry<E>(null, 0, null);
		
		int newLoad = load;
		int oldSize = oldEntries.length;
		for(int i = oldSize - 1; i >= 0; i--){
			Entry<E> e = oldEntries[i];
			if(e != null){
				Entry<E> lastCurrentEntry = currentEntryRoot;
				Entry<E> lastShiftedEntry = shiftedEntryRoot;
				do{
					int position = e.hash & newHashMask;
					
					if(position == i){
						lastCurrentEntry.next = e;
						lastCurrentEntry = e;
					}else{
						lastShiftedEntry.next = e;
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
		
		load = newLoad;
		
		threshold <<= 1;
		entries = newEntries;
		hashMask = newHashMask;
	}
	
	private void ensureCapacity(){
		if(load > threshold){
			rehash();
		}
	}
	
	public boolean put(E element){
		ensureCapacity();
		
		int hash = element.hashCode();
		int position = hash & hashMask;
		
		Entry<E> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<E> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.element.equals(element)){
					return false;
				}
			}while((entry = entry.next) != null);
		}
		
		entries[position] = new Entry<E>(element, hash, currentStartEntry);
		load++;
		
		return true;
	}
	
	public boolean remove(E element){
		int hash = element.hashCode();
		int position = hash & hashMask;
		
		Entry<E> previous = null;
		Entry<E> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<E> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.element.equals(element)){
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
	
	public boolean contains(E element){
		int hash = element.hashCode();
		int position = hash & hashMask;
		
		Entry<E> entry = entries[position];
		while(entry != null){
			if(hash == entry.hash && element.equals(entry.element)) return true;
			
			entry = entry.next;
		}
		
		return false;
	}
	
	public void clear(){
		entries = (Entry<E>[]) new Entry[entries.length];
		
		load = 0;
	}
	
	private static class Entry<E>{
		public final int hash;
		public final E element;
		public Entry<E> next;
		
		public Entry(E element, int hash, Entry<E> next){
			super();
			
			this.element = element;
			this.hash = hash;
			this.next = next;
		}
	}
}
