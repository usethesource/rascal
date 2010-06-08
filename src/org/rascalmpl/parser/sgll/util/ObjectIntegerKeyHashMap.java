package org.rascalmpl.parser.sgll.util;

public class ObjectIntegerKeyHashMap<K, V>{
	private Entry<K, V>[] entries;

	private int hashMask;
	private int bitSize;
	
	private int threshold;
	private int load;
	
	public ObjectIntegerKeyHashMap(){
		super();
		
		int nrOfEntries = 1 << bitSize;
		
		hashMask = nrOfEntries - 1;
		
		entries = (Entry<K, V>[]) new Entry[nrOfEntries];
		
		threshold = nrOfEntries;
		load = 0;
	}
	
	private void rehash(){
		int nrOfEntries = 1 << (++bitSize);
		int newHashMask = nrOfEntries - 1;
		
		Entry<K, V>[] oldEntries = entries;
		Entry<K, V>[] newEntries = (Entry<K, V>[]) new Entry[nrOfEntries];
		
		Entry<K, V> currentEntryRoot = new Entry<K, V>(null, 0, null, 0, null);
		Entry<K, V> shiftedEntryRoot = new Entry<K, V>(null, 0, null, 0, null);
		
		int newLoad = load;
		int oldSize = oldEntries.length;
		for(int i = oldSize - 1; i >= 0; i--){
			Entry<K, V> e = oldEntries[i];
			if(e != null){
				Entry<K, V> lastCurrentEntry = currentEntryRoot;
				Entry<K, V> lastShiftedEntry = shiftedEntryRoot;
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
		load++;
		
		return null;
	}
	
	public void unsafePut(K key, int key2, V value){
		ensureCapacity();
		
		int hash = key.hashCode() ^ (key2 << 5);
		int position = hash & hashMask;
		
		entries[position] = new Entry<K, V>(key, key2, value, hash, entries[position]);
		load++;
	}
	
	public V remove(K key, int key2){
		int hash = key.hashCode() ^ (key2 << 5);
		int position = hash & hashMask;
		
		Entry<K, V> previous = null;
		Entry<K, V> currentStartEntry = entries[position];
		if(currentStartEntry != null){
			Entry<K, V> entry = currentStartEntry;
			do{
				if(hash == entry.hash && entry.key.equals(key)){
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
			if(hash == entry.hash && key.equals(entry.key)) return entry.value;
			
			entry = entry.next;
		}
		
		return null;
	}
	
	public void clear(){
		entries = (Entry<K, V>[]) new Entry[entries.length];
		
		load = 0;
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
