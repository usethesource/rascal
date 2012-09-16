package org.rascalmpl.parser.gtd.util;

@SuppressWarnings("unchecked")
public class ObjectKeyedIntegerMap<K>{
	private Entry<K>[] entries;

	private int hashMask;
	private int bitSize = 2;
	
	private int threshold;
	private int load;
	
	public ObjectKeyedIntegerMap(){
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
		
		Entry<K>[] oldEntries = entries;
		Entry<K>[] newEntries = new Entry[nrOfEntries];
		
		Entry<K> currentEntryRoot = new Entry<K>(null, -1, -1, null);
		Entry<K> shiftedEntryRoot = new Entry<K>(null, -1, -1, null);
		
		int oldSize = oldEntries.length;
		for(int i = oldSize - 1; i >= 0; --i){
			Entry<K> e = oldEntries[i];
			if(e != null){
				Entry<K> lastCurrentEntry = currentEntryRoot;
				Entry<K> lastShiftedEntry = shiftedEntryRoot;
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
	
	public void put(K key, int value){
		ensureCapacity();
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<K> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<K> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.key.equals(key)){
					entry.value = value;
				}
			}while((entry = entry.next) != null);
		}
		
		entries[position] = new Entry<K>(key, value, hash, currentStartEntry);
		++load;
	}
	
	public void putUnsafe(K key, int value){
		ensureCapacity();
		
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		entries[position] = new Entry<K>(key, value, hash, entries[position]);
		++load;
	}
	
	public boolean remove(K key){
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<K> previous = null;
		Entry<K> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<K> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.key.equals(key)){
					if(previous == null){
						entries[position] = entry.next;
					}else{
						previous.next = entry.next;
					}
					--load;
					return true;
				}
				
				previous = entry;
			}while((entry = entry.next) != null);
		}
		
		return false;
	}
	
	public int get(K key){
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<K> entry = entries[position];
		while(entry != null){
			if(hash == entry.hash && key.equals(entry.key)) return entry.value;
			
			entry = entry.next;
		}
		
		return -1;
	}
	
	public boolean contains(K key){
		int hash = key.hashCode();
		int position = hash & hashMask;
		
		Entry<K> entry = entries[position];
		while(entry != null){
			if(hash == entry.hash && key.equals(entry.key)) return true;
			
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
	
	public static class Entry<K>{
		public final int hash;
		public final K key;
		public int value;
		public Entry<K> next;
		
		public Entry(K key, int value, int hash, Entry<K> next){
			super();
			
			this.key = key;
			this.value = value;
			this.hash = hash;
			this.next = next;
		}
	}
}
